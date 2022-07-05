package com.imwj.msg.cron.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.cron.domain.CrowdInfoVo;
import com.imwj.msg.cron.service.TaskHandler;
import com.imwj.msg.cron.utils.ReadFileUtils;
import com.imwj.msg.support.dao.MessageTemplateDao;
import com.imwj.msg.support.domain.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public void handle(Long messageTemplateId) {
        MessageTemplate messageTemplate = messageTemplateDao.selectById(messageTemplateId);
        if(messageTemplate == null || StrUtil.isBlank(messageTemplate.getCronCrowdPath())){
            log.error("TaskHandler#handle crowdPath empty!");
            return;
        }
        List<CrowdInfoVo> csvRowList = ReadFileUtils.getCsvRowList(messageTemplate.getCronCrowdPath());
        //读取文件内容不为空 则对数据进行处理
        if(CollUtil.isNotEmpty(csvRowList)){

        }
        //打印数据内容
        log.info("csv info:", JSON.toJSONString(csvRowList));
    }
}
