package com.imwj.msg.cron.handler;

import com.dtp.core.thread.DtpExecutor;
import com.imwj.msg.cron.config.CronAsyncThreadPoolConfig;
import com.imwj.msg.cron.service.TaskHandler;
import com.imwj.msg.support.utils.ThreadPoolUtils;
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

    @Autowired
    private ThreadPoolUtils threadPoolUtils;

    private DtpExecutor dtpExecutor = CronAsyncThreadPoolConfig.getXxlCronExecutor();

    /**
     * 处理所有的定时任务
     */
    @XxlJob("msgCenterJob")
    public void execute() {
        log.info("CronTaskHandler#execute messageTemplateId:{} cron exec!", XxlJobHelper.getJobParam());
        //注册线程池（注册为动态线程池 + 优雅关闭）
        threadPoolUtils.register(dtpExecutor);
        //获取模板id
        Long messageTemplateId = Long.valueOf(XxlJobHelper.getJobParam());
        //线程池去执行job任务
        dtpExecutor.execute(() ->{taskHandler.handle(messageTemplateId);});
    }
}
