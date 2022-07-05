package com.imwj.msg.handler.config;

import cn.hutool.core.thread.ExecutorBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置类
 * @author langao_q
 * @since 2021-12-30 15:36
 */
public class ThreadPoolConfig {

    /**
     * @param coreSize
     * @param maxSize
     * @param queueSize
     * 阻塞队列满了，也不丢弃任务  CallerRunsPolicy 策略
     * @return
     */
    public static ExecutorService getThreadPool(Integer coreSize, Integer maxSize, Integer queueSize) {
        return ExecutorBuilder.create()
                .setCorePoolSize(coreSize)
                .setMaxPoolSize(maxSize)
                .setKeepAliveTime(60, TimeUnit.SECONDS)
                .setWorkQueue(new LinkedBlockingQueue<>(queueSize))
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .build();
    }

}
