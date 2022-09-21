package com.imwj.msg.handler.receiver.rocket;

import com.alibaba.fastjson.JSON;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.handler.receiver.service.ConsumeService;
import com.imwj.msg.support.constans.MessageQueuePipeline;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Rocket发送消息监听器
 * @author wj
 * @create 2022-09-21 17:21
 */
@Component
@ConditionalOnProperty(name = "msg-mq-pipeline", havingValue = MessageQueuePipeline.ROCKET_MQ)
@RocketMQMessageListener(topic = "${msg.business.topic.name}",
        consumerGroup = "${msg-rocketmq-biz-consumer-group}",
        selectorType = SelectorType.TAG,
        selectorExpression = "${msg.business.tagId.value}"
)
public class RocketMqBizReceiver implements RocketMQListener<String> {

    @Autowired
    private ConsumeService consumeService;

    @Override
    public void onMessage(String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        List<TaskInfo> taskInfoLists = JSON.parseArray(message, TaskInfo.class);
        consumeService.consume2Send(taskInfoLists);
    }
}

