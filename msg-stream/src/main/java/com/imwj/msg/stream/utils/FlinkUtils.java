package com.imwj.msg.stream.utils;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * flink工具类
 * @author wj
 * @create 2022-07-13 10:57
 */
public class FlinkUtils {

    /**
     * 获取kafkaConsumer
     * @param topicName
     * @param groupId
     * @return
     */
    public KafkaSource<String> getKafkaConsumer(String topicName, String groupId, String broker) {
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(broker)
                .setTopics(topicName)
                .setGroupId(groupId)
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
        return source;
    }
}
