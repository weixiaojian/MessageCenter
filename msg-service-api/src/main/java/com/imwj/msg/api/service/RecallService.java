package com.imwj.msg.api.service;

import com.imwj.msg.api.domain.SendRequest;
import com.imwj.msg.api.domain.SendResponse;

/**
 * 撤回接口
 * @author wj
 * @create 2022-09-08 17:53
 */
public interface RecallService {

    /**
     * 根据模板ID撤回消息
     *
     * @param sendRequest
     * @return
     */
    SendResponse recall(SendRequest sendRequest);
}
