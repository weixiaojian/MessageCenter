package com.imwj.msg.web.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imwj.msg.common.constant.MessageCenterConstant;
import com.imwj.msg.common.enums.AuditStatus;
import com.imwj.msg.common.enums.MessageStatus;
import com.imwj.msg.common.enums.RespStatusEnum;
import com.imwj.msg.common.enums.TemplateType;
import com.imwj.msg.common.page.PageVo;
import com.imwj.msg.common.page.RetPage;
import com.imwj.msg.common.vo.BasicResultVO;
import com.imwj.msg.cron.entity.XxlJobInfo;
import com.imwj.msg.cron.service.CronTaskService;
import com.imwj.msg.cron.utils.XxlJobUtils;
import com.imwj.msg.support.dao.MessageTemplateDao;
import com.imwj.msg.support.domain.MessageTemplate;
import com.imwj.msg.web.service.messageTemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 消息模板实现类
 * @author wj
 * @create 2022-05-07 11:42
 */
@Service
public class MessageTemplateServiceImpl implements messageTemplateService {

    @Resource
    private MessageTemplateDao messageTemplateDao;

    @Resource
    private CronTaskService cronTaskService;

    @Resource
    private XxlJobUtils xxlJobUtils;

    @Override
    public List<MessageTemplate> queryList(PageVo pageVo) {
        //1.执行查询
        IPage<MessageTemplate> page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
        page = messageTemplateDao.findMessageTemplatePage(page, pageVo);
        //2.封装返回结果RetPage
        RetPage retPage = new RetPage(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), page.getRecords());
        //3.返回给前端
        return retPage.getData();
    }

    @Override
    public Long count() {
        QueryWrapper<MessageTemplate> queryWrapper = new QueryWrapper<>();
        return messageTemplateDao.selectCount(queryWrapper).longValue();
    }

    @Override
    public MessageTemplate saveOrUpdate(MessageTemplate messageTemplate) {
        MessageTemplate messageTemplateDb = messageTemplateDao.selectById(messageTemplate.getId());
        if(messageTemplateDb == null){
            //初始化模板数据（状态、创建人相关信息）
            initStatus(messageTemplate);
            messageTemplateDao.insert(messageTemplate);
        }else{
            //重置模板的状态、修改定时任务信息
            resetStatus(messageTemplate);
            messageTemplate.setUpdated(Math.toIntExact(DateUtil.currentSeconds()));
            messageTemplateDao.updateById(messageTemplate);
        }
        return messageTemplate;
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        //如果模板下有定时任务 则也需要删除
        List<MessageTemplate> messageTemplates = messageTemplateDao.selectBatchIds(ids);
        for(MessageTemplate messageTemplate : messageTemplates){
            if(messageTemplate.getCronTaskId() != null && messageTemplate.getCronTaskId() > 0){
                cronTaskService.deleteCronTask(messageTemplate.getCronTaskId());
            }
        }
        messageTemplateDao.deleteBatchIds(ids);
    }

    @Override
    public MessageTemplate queryById(Long id) {
        return messageTemplateDao.selectById(id);
    }

    @Override
    public void copy(Long id) {
        MessageTemplate messageTemplateDb = messageTemplateDao.selectById(id);
        MessageTemplate clone = ObjectUtil.clone(messageTemplateDb).setId(null).setCronTaskId(null);
        messageTemplateDao.insert(clone);
    }

    /**
     * 启动模板的定时任务
     * @param id
     */
    public BasicResultVO startCronTask(Long id) {
        //1.获取模板详情数据
        MessageTemplate messageTemplate = messageTemplateDao.selectById(id);

        //2.动态创建定时任务并启动
        XxlJobInfo xxlJobInfo = xxlJobUtils.buildXxlJobInfo(messageTemplate);
        // 3.获取taskId(如果本身存在则复用原有任务，如果不存在则得到新建后任务ID)
        Integer taskId = messageTemplate.getCronTaskId();
        BasicResultVO basicResultVO = cronTaskService.saveCronTask(xxlJobInfo);
        if (taskId == null && RespStatusEnum.SUCCESS.getCode().equals(basicResultVO.getStatus()) && basicResultVO.getData() != null) {
            taskId = Integer.valueOf(String.valueOf(basicResultVO.getData()));
        }
        // 4. 启动定时任务
        if (taskId != null) {
            cronTaskService.startCronTask(taskId);
            MessageTemplate clone = ObjectUtil.clone(messageTemplate).setMsgStatus(MessageStatus.RUN.getCode()).setCronTaskId(taskId).setUpdated(Math.toIntExact(DateUtil.currentSeconds()));
            messageTemplateDao.updateById(clone);
            return BasicResultVO.success();
        }
        return BasicResultVO.fail();
    }

    /**
     * 暂停模板的定时任务
     * @param id
     */
    public BasicResultVO stopCronTask(Long id) {
        // 1.修改模板状态
        MessageTemplate messageTemplate = messageTemplateDao.selectById(id);
        MessageTemplate clone = ObjectUtil.clone(messageTemplate).setMsgStatus(MessageStatus.STOP.getCode()).setUpdated(Math.toIntExact(DateUtil.currentSeconds()));
        messageTemplateDao.updateById(clone);
        // 2.暂停定时任务
        return cronTaskService.stopCronTask(clone.getCronTaskId());
    }

    /**
     * 初始化状态信息
     *
     * @param messageTemplate
     */
    private void initStatus(MessageTemplate messageTemplate) {
        messageTemplate.setFlowId(StrUtil.EMPTY)
                .setMsgStatus(MessageStatus.INIT.getCode()).setAuditStatus(AuditStatus.WAIT_AUDIT.getCode())
                .setCreator("imwj").setUpdator("imwj").setTeam("imwj").setAuditor("imwj")
                .setCreated(Math.toIntExact(DateUtil.currentSeconds()))
                .setIsDeleted(MessageCenterConstant.FALSE);
    }

    /**
     * 1. 重置模板的状态
     * 2. 修改定时任务信息
     *
     * @param messageTemplate
     */
    private void resetStatus(MessageTemplate messageTemplate) {
        messageTemplate.setUpdator(messageTemplate.getUpdator())
                .setMsgStatus(MessageStatus.INIT.getCode()).setAuditStatus(AuditStatus.WAIT_AUDIT.getCode());
        if (messageTemplate.getCronTaskId() != null && TemplateType.CLOCKING.getCode().equals(messageTemplate.getTemplateType())) {
            XxlJobInfo xxlJobInfo = xxlJobUtils.buildXxlJobInfo(messageTemplate);
            cronTaskService.saveCronTask(xxlJobInfo);
            cronTaskService.stopCronTask(messageTemplate.getCronTaskId());
        }
    }
}
