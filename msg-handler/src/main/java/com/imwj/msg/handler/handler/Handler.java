package com.imwj.msg.handler.handler;

import com.imwj.msg.common.domain.TaskInfo;

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

}
