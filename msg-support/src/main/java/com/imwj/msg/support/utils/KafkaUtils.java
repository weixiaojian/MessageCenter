package com.imwj.msg.support.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * kafka工具类
 * @author wj
 * @create 2022-07-13 11:17
 */
@Component
@Slf4j
public class KafkaUtils {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${msg.business.tagId.key}")
    private String tagIdKey;

    /**
     * 发送kafka消息
     *
     * @param topicName
     * @param jsonMessage
     */
    public void send(String topicName, String jsonMessage) {
        kafkaTemplate.send(topicName, jsonMessage);
    }

    /**
     * 发送kafka消息
     * 支持tag过滤
     *
     * @param topicName
     * @param jsonMessage
     * @param tagId
     */
    public void send(String topicName, String jsonMessage, String tagId) {
        if (StrUtil.isNotBlank(tagId)) {
            List<Header> headers = Arrays.asList(new RecordHeader(tagIdKey, tagId.getBytes(StandardCharsets.UTF_8)));
            kafkaTemplate.send(new ProducerRecord(topicName, null, null, null, jsonMessage, headers));
        } else {
            kafkaTemplate.send(topicName, jsonMessage);
        }

    }
}
