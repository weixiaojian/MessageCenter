package com.imwj.msg.support.mq.kafka.impl;

import cn.hutool.core.util.StrUtil;
import com.imwj.msg.support.constans.MessageQueuePipeline;
import com.imwj.msg.support.mq.kafka.SendMqService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * kafka消息发送实现类
 * @author wj
 * @create 2022-09-20 11:35
 */
@Service
@ConditionalOnProperty(name = "msg.mq.pipeline", havingValue = MessageQueuePipeline.KAFKA)
public class KafkaSendMqServiceImpl implements SendMqService {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${msg.business.tagId.key}")
    private String tagIdKey;

    @Override
    public void send(String topic, String jsonValue, String tagId) {
        if (StrUtil.isNotBlank(tagId)) {
            List<Header> headers = Arrays.asList(new RecordHeader(tagIdKey, tagId.getBytes(StandardCharsets.UTF_8)));
            kafkaTemplate.send(new ProducerRecord(topic, null, null, null, jsonValue, headers));
        } else {
            kafkaTemplate.send(topic, jsonValue);
        }
    }

    @Override
    public void send(String topic, String jsonValue) {
        send(topic, jsonValue, null);
    }
}
