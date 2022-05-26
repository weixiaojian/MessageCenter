package com.imwj.msg.handler;

import com.imwj.msg.service.TaskHandler;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wj
 * @create 2022-05-07 17:46
 */
@Slf4j
@Service
public class CronTaskHandler {

    @Autowired
    private TaskHandler taskHandler;

    /**
     * 简单任务
     */
    @XxlJob("msgCenterJob")
    public void execute() {
        log.info("XXL-JOB, Hello World.");
        Long messageTemplateId = Long.valueOf(XxlJobHelper.getJobParam());
        taskHandler.handle(messageTemplateId);
    }

}
