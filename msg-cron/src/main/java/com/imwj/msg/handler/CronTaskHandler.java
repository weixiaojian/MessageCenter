package com.imwj.msg.handler;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wj
 * @create 2022-05-07 17:46
 */
@Slf4j
@Service
public class CronTaskHandler {

    /**
     * 简单任务
     */
    @XxlJob("msgTestJobHandler")
    public void execute() {
        log.info("XXL-JOB, Hello World.");
    }

}
