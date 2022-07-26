package com.imwj.msg.handler.shield;

import com.imwj.msg.common.domain.TaskInfo;

/**
 * 屏蔽服务接口
 * @author wj
 * @create 2022-07-26 16:53
 */
public interface ShieldService {

    /**
     * 消息夜间屏蔽
     * @param taskInfo
     */
    void shield(TaskInfo taskInfo);
}
