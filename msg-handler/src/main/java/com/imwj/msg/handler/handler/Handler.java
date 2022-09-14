package com.imwj.msg.handler.handler;

import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.support.domain.MessageTemplate;

/**
 * 发送各个渠道的handler
 * @author langao_q
 * @since 2021-12-30 15:50
 */
public interface Handler{

    /**
     * 处理器
     * @param taskInfo
     */
    void doHandler(TaskInfo taskInfo);

    /**
     * 撤回消息  TODO 待实现
     *
     * @param messageTemplate
     * @return
     */
    void recall(MessageTemplate messageTemplate);



}
