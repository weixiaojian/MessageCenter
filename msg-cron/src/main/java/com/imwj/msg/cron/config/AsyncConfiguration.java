package com.imwj.msg.cron.config;



import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @author wj
 * 处理定时任务的线程池配置信息，为@Async注解服务
 * @create 2022-07-04 15:55
 */
@Slf4j
@Configuration
@EnableAsync
@EnableConfigurationProperties(AsyncExecutionProperties.class)
public class AsyncConfiguration implements AsyncConfigurer {

    @Primary
    @Bean("msgCenterExecutor")
    public ThreadPoolTaskExecutor executor(AsyncExecutionProperties properties) {
        log.info("funExecutor -- init ");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(properties.getCoreSize());
        // 最大线程数
        executor.setMaxPoolSize(properties.getMaxSize());
        // 最大存活时间
        executor.setKeepAliveSeconds(properties.getKeepAlive());
        // 阻塞队列容量
        executor.setQueueCapacity(properties.getQueueCapacity());
        // 设置名称前缀
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(properties.getRejectedHandler().getHandler());
        // 是否允许核心线程超时
        executor.setAllowCoreThreadTimeOut(properties.isAllowCoreThreadTimeout());
        executor.setWaitForTasksToCompleteOnShutdown(properties.isWaitForTasksToCompleteOnShutDown());
        executor.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());
        log.info("austinExecutor: {} ", executor);
        executor.initialize();
        return executor;
    }

    /**
     * 在使用void返回类型的异步方法执行期间抛出异常时要使用的实例。
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
