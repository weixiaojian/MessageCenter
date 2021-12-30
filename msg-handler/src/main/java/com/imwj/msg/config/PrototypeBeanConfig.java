package com.imwj.msg.config;

import com.imwj.msg.pending.Task;
import com.imwj.msg.receiver.MsgReceiver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Handler模块的配置信息
 * @author langao_q
 * @since 2021-12-30 15:36
 */
@Configuration
public class PrototypeBeanConfig {

    /**
     * 定义多例的Receiver
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MsgReceiver receiver() {
        return new MsgReceiver();
    }

    /**
     * 定义多例的Task
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Task task() {
        return new Task();
    }

}
