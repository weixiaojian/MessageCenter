package com.imwj.msg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imwj.msg.dao.MessageTemplateDao;
import com.imwj.msg.domain.MessageTemplate;
import com.imwj.msg.page.PageVo;
import com.imwj.msg.page.RetPage;
import com.imwj.msg.service.messageTemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wj
 * @create 2022-05-07 11:42
 */
@Service
public class MessageTemplateServiceImpl implements messageTemplateService {

    @Resource
    private MessageTemplateDao messageTemplateDao;

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
            messageTemplateDao.insert(messageTemplate);
        }else{
            messageTemplateDao.updateById(messageTemplate);
        }
        return messageTemplate;
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        messageTemplateDao.deleteBatchIds(ids);
    }

    @Override
    public MessageTemplate queryById(Long id) {
        return messageTemplateDao.selectById(id);
    }

    @Override
    public void copy(Long id) {
        MessageTemplate messageTemplateDb = messageTemplateDao.selectById(id);
        messageTemplateDb.setId(null);
        messageTemplateDao.insert(messageTemplateDb);
    }
}
