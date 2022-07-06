package com.imwj.msg.cron.handler;

import com.imwj.msg.cron.service.TaskHandler;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wj
 * @create 2022-05-07 17:46
 */
@Slf4j
@Service
public class CronTaskHandler {

    @Resource
    private TaskHandler taskHandler;

    /**
     * 处理所有的定时任务
     */
    @XxlJob("msgCenterJob")
    public void execute() {
        log.info("CronTaskHandler#execute messageTemplateId:{} cron exec!", XxlJobHelper.getJobParam());
        Long messageTemplateId = Long.valueOf(XxlJobHelper.getJobParam());
        taskHandler.handle(messageTemplateId);
    }
}
