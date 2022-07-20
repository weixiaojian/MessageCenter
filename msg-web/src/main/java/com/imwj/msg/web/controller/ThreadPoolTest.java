package com.imwj.msg.web.controller;

import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 动态线程池测试
 * @author wj
 * @create 2022-07-20 14:02
 */
@Slf4j
@RestController
public class ThreadPoolTest {


    @GetMapping("/tp")
    public void send() {
        DtpExecutor dtpExecutor1 = DtpRegistry.getExecutor("dynamic-tp-test-1");
        DtpExecutor dtpExecutor2 = DtpRegistry.getExecutor("dynamic-tp-test-2");

        log.info("dtpExecutor1：{}   {}",dtpExecutor1.getCorePoolSize(), dtpExecutor1.getMaximumPoolSize());
        log.info("dtpExecutor2：{}   {}",dtpExecutor2.getCorePoolSize(), dtpExecutor2.getMaximumPoolSize());

        dtpExecutor1.execute(() -> log.info("test1"));
        dtpExecutor2.execute(() -> log.info("test2"));

    }
}