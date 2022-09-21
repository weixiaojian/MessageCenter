package com.imwj.msg.support.constans;

/**
 * 消息队列常量
 * @author wj
 * @create 2022-09-20 10:50
 */
public interface MessageQueuePipeline {

    /**
     * 事件总线
     */
    String EVENT_BUS = "eventBus";
    /**
     * kafka
     */
    String KAFKA = "kafka";
    /**
     * rocketMq
     */
    String ROCKET_MQ = "rocketMq";
    /**
     * rabbitMq
     */
    String RABBIT_MQ = "rabbitMq";
}
