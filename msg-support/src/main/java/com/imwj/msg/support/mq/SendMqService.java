package com.imwj.msg.support.mq;

/**
 * 消息发送service
 * @author wj
 * @create 2022-09-20 11:34
 */
public interface SendMqService {

    /**
     * 发送消息
     *
     * @param topic
     * @param jsonValue
     * @param tagId
     */
    void send(String topic, String jsonValue, String tagId);


    /**
     * 发送消息
     *
     * @param topic
     * @param jsonValue
     */
    void send(String topic, String jsonValue);

}
