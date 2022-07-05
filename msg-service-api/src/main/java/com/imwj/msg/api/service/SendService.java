package com.imwj.msg.api.service;


import com.imwj.msg.api.domain.BatchSendRequest;
import com.imwj.msg.api.domain.SendRequest;
import com.imwj.msg.api.domain.SendResponse;

/**
 * @author langa.o_q
 * @since 2021-12-29 16:47
 */
public interface SendService {

    /**
     * 单文案发送接口
     * @param sendRequest
     * @return
     */
    SendResponse send(SendRequest sendRequest);


    /**
     * 多文案发送接口
     * @param batchSendRequest
     * @return
     */
    SendResponse batchSend(BatchSendRequest batchSendRequest);

}
