package com.imwj.msg.cron.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.cron.constant.PendingConstant;
import com.imwj.msg.cron.domain.CrowdInfoVo;
import com.imwj.msg.cron.pending.CrowdBatchTaskPending;
import com.imwj.msg.cron.service.TaskHandler;
import com.imwj.msg.cron.utils.ReadFileUtils;
import com.imwj.msg.support.dao.MessageTemplateDao;
import com.imwj.msg.support.domain.MessageTemplate;
import com.imwj.msg.support.pending.PendingParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author wj
 * @create 2022-05-26 16:52
 */
@Slf4j
@Service
public class TaskHandlerImpl implements TaskHandler {

    @Resource
    private MessageTemplateDao messageTemplateDao;
    @Autowired
    private ApplicationContext context;

    @Async
    @Override
    public void handle(Long messageTemplateId) {
        log.info("TaskHandler handle:{}", Thread.currentThread().getName());
        //查询模板数据中的文件相关信息
        MessageTemplate messageTemplate = messageTemplateDao.selectById(messageTemplateId);
        if(messageTemplate == null || StrUtil.isBlank(messageTemplate.getCronCrowdPath())){
            log.error("TaskHandler#handle crowdPath empty!");
            return;
        }

        CrowdBatchTaskPending crowdBatchTaskPending = context.getBean(CrowdBatchTaskPending.class);

        //读取文件得到每一行记录给到队列做batch处理
        ReadFileUtils.getCsvRow(messageTemplate.getCronCrowdPath(), row -> {
            if (CollUtil.isEmpty(row.getFieldMap())
                    || StrUtil.isBlank(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))) {
                return;
            }
            HashMap<String, String> params = ReadFileUtils.getParamFromLine(row.getFieldMap());
            CrowdInfoVo crowdInfoVo = CrowdInfoVo.builder().receiver(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))
                    .params(params).messageTemplateId(messageTemplateId).build();
            crowdBatchTaskPending.pending(crowdInfoVo);
        });
    }
}
