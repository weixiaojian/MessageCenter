package com.imwj.msg.web.controller;

import cn.hutool.core.thread.ThreadUtil;
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

    private final int  forSize = 10;

    @GetMapping("/tp")
    public void tp() {
        DtpExecutor dtpExecutor2 = DtpRegistry.getExecutor("execute-xxl-thread-pool");
        DtpExecutor dtpExecutor1 = DtpRegistry.getExecutor("imwj.im.notice");

        for(int i=0; i<forSize; i++){
            log.info("dtpExecutor1：{}   {}",dtpExecutor1.getCorePoolSize(), dtpExecutor1.getMaximumPoolSize());
            log.info("dtpExecutor2：{}   {}",dtpExecutor2.getCorePoolSize(), dtpExecutor2.getMaximumPoolSize());

            dtpExecutor1.execute(() -> log.info("test1"));
            dtpExecutor2.execute(() -> log.info("test2"));
            ThreadUtil.sleep(1000 * 10);
        }
    }
}