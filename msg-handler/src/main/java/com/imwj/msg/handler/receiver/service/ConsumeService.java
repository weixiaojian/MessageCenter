package com.imwj.msg.handler.receiver.service;

import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.support.domain.MessageTemplate;

import java.util.List;

/**
 * 消费消息服务
 * @author wj
 * @create 2022-09-20 11:02
 */
public interface ConsumeService {

    /**
     * 从MQ拉到消息进行消费，发送消息
     *
     * @param taskInfoLists
     */
    void consume2Send(List<TaskInfo> taskInfoLists);


    /**
     * 从MQ拉到消息进行消费，撤回消息
     *
     * @param messageTemplate
     */
    void consume2recall(MessageTemplate messageTemplate);

}
