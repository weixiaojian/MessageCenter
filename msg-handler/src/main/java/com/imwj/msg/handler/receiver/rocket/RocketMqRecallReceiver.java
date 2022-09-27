package com.imwj.msg.handler.receiver.rocket;

import com.alibaba.fastjson.JSON;
import com.imwj.msg.handler.receiver.service.ConsumeService;
import com.imwj.msg.support.constans.MessageQueuePipeline;
import com.imwj.msg.support.domain.MessageTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Rocket撤回消息监听器
 * @author wj
 * @create 2022-09-21 17:21
 */
@Component
@ConditionalOnProperty(name = "msg.mq.pipeline", havingValue = MessageQueuePipeline.ROCKET_MQ)
@RocketMQMessageListener(topic = "${msg.business.recall.topic.name}",
        consumerGroup = "${msg.rocketmq.recall.consumer.group}",
        selectorType = SelectorType.TAG,
        selectorExpression = "${msg.business.tagId.value}"
)
public class RocketMqRecallReceiver implements RocketMQListener<String> {

    @Autowired
    private ConsumeService consumeService;

    @Override
    public void onMessage(String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        MessageTemplate messageTemplate = JSON.parseObject(message, MessageTemplate.class);
        consumeService.consume2recall(messageTemplate);
    }
}
