package com.imwj.msg.support.mq.eventbus;

import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.support.domain.MessageTemplate;

import java.util.List;

/**
 * 监听器
 * @author wj
 * @create 2022-09-20 11:00
 */
public interface EventBusListener {

    /**
     * 消费消息
     * @param lists
     */
    void consume(List<TaskInfo> lists);

    /**
     * 撤回消息
     * @param messageTemplate
     */
    void recall(MessageTemplate messageTemplate);
}
