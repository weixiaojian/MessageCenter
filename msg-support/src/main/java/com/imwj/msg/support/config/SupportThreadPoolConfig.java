package com.imwj.msg.support.config;

import cn.hutool.core.thread.ExecutorBuilder;
import com.imwj.msg.common.constant.ThreadPoolConstant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * cron读取csv文件推送消息线程池配置类
 * @author wj
 * @create 2022-07-21 14:31
 */
public class SupportThreadPoolConfig {
    /**
     * 业务：实现pending队列的单线程池
     * 配置：核心线程可以被回收，当线程池无被引用且无核心线程数，应当被回收
     * 动态线程池且被Spring管理：false
     */
    public static ExecutorService getPendingSingleThreadPool() {
        return ExecutorBuilder.create()
                .setCorePoolSize(ThreadPoolConstant.SINGLE_CORE_POOL_SIZE)
                .setMaxPoolSize(ThreadPoolConstant.SINGLE_MAX_POOL_SIZE)
                .setWorkQueue(ThreadPoolConstant.BIG_BLOCKING_QUEUE)
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .setAllowCoreThreadTimeOut(true)
                .setKeepAliveTime(ThreadPoolConstant.SMALL_KEEP_LIVE_TIME, TimeUnit.SECONDS)
                .build();
    }
}
