package com.imwj.msg.support.mq.rabbitmq;

import com.imwj.msg.support.constans.MessageQueuePipeline;
import com.imwj.msg.support.mq.SendMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ消息发送实现类
 * @author wj
 * @create 2022-09-21 16:00
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "msg.mq.pipeline", havingValue = MessageQueuePipeline.RABBIT_MQ)
public class RabbitSendMqServiceImpl implements SendMqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * TODO topic.name
     */
    private String confTopic = "topic.name";
    /**
     * TODO exchange.name
     */
    private String exchangeName = "exchange.name";


    @Override
    public void send(String topic, String jsonValue, String tagId) {
        if (topic.equals(confTopic)) {
            rabbitTemplate.convertAndSend(exchangeName, confTopic, jsonValue);
        } else {
            log.error("RabbitSendMqServiceImpl send topic error! topic:{},confTopic:{}", topic, confTopic);
        }
    }

    @Override
    public void send(String topic, String jsonValue) {
        send(topic, jsonValue, null);
    }
}
