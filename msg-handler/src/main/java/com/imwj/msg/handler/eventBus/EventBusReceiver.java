package com.imwj.msg.handler.eventBus;

import com.google.common.eventbus.Subscribe;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.handler.receiver.service.ConsumeService;
import com.imwj.msg.support.constans.MessageQueuePipeline;
import com.imwj.msg.support.domain.MessageTemplate;
import com.imwj.msg.support.mq.eventbus.EventBusListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息事件总线
 * @author wj
 * @create 2022-09-20 10:48
 */
@Component
@ConditionalOnProperty(name = "msg.mq.pipeline", havingValue = MessageQueuePipeline.EVENT_BUS)
public class EventBusReceiver implements EventBusListener {

    @Autowired
    private ConsumeService consumeService;

    @Override
    @Subscribe
    public void consume(List<TaskInfo> lists) {
        consumeService.consume2Send(lists);
    }

    @Override
    @Subscribe
    public void recall(MessageTemplate messageTemplate) {
        consumeService.consume2recall(messageTemplate);
    }
}
