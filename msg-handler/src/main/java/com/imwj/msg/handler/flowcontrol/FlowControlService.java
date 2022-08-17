package com.imwj.msg.handler.flowcontrol;

import com.imwj.msg.common.domain.TaskInfo;

/**
 * 限流服务
 * @author wj
 * @create 2022-08-17 17:22
 */
public interface FlowControlService {

    /**
     * 根据渠道进行流量控制
     *
     * @param taskInfo
     * @param flowControlParam
     */
    void flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam);
}
