package com.imwj.msg.handler.receiver.kafka;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.handler.receiver.service.ConsumeService;
import com.imwj.msg.handler.utils.GroupIdMappingUtils;
import com.imwj.msg.support.constans.MessageQueuePipeline;
import com.imwj.msg.support.domain.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * kafka消费者（发送/撤回消息监听器）
 * @author langao_q
 * @since 2021-12-30 10:43
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ConditionalOnProperty(name = "msg.mq.pipeline", havingValue = MessageQueuePipeline.KAFKA)
public class MsgReceiver {

    @Autowired
    private ConsumeService consumeService;

    @KafkaListener(topics = "#{'${msg.business.topic.name}'}", containerFactory = "filterContainerFactory")
    public void consumer(ConsumerRecord<?, String> consumerRecord, @Header(KafkaHeaders.GROUP_ID) String groupId) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if(kafkaMessage.isPresent()){
            List<TaskInfo> taskInfos = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);
            String messageGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfos.iterator()));
            /**
             * 只有消息中的groupId 与 当前消费者的groupId相等才去发送短信
             */
            if(groupId.equals(messageGroupId)){
                consumeService.consume2Send(taskInfos);
            }
        }
    }

    /**
     * 撤回消息
     * @param consumerRecord
     */
    @KafkaListener(topics = "#{'${msg.business.recall.topic.name}'}",groupId = "#{'${msg.business.recall.group.name}'}",
            containerFactory = "filterContainerFactory")
    public void recall(ConsumerRecord<?,String> consumerRecord){
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if(kafkaMessage.isPresent()){
            MessageTemplate messageTemplate = JSON.parseObject(kafkaMessage.get(), MessageTemplate.class);
            consumeService.consume2recall(messageTemplate);
        }
    }
}
