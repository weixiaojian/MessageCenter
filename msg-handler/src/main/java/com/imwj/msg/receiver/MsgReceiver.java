package com.imwj.msg.receiver;

import com.alibaba.fastjson.JSON;
import com.imwj.msg.domain.AnchorInfo;
import com.imwj.msg.domain.LogParam;
import com.imwj.msg.domain.TaskInfo;
import com.imwj.msg.enums.AnchorState;
import com.imwj.msg.pending.Task;
import com.imwj.msg.pending.TaskPendingHolder;
import com.imwj.msg.util.LogUtils;
import com.imwj.msg.utils.GroupIdMappingUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.util.List;
import java.util.Optional;

/**
 * kafka消费者（发送消息监听器）
 * @author langao_q
 * @since 2021-12-30 10:43
 */
@Slf4j
public class MsgReceiver {

    private static final String LOG_BIZ_TYPE = "Receiver#consumer";

    @Autowired
    private ApplicationContext context;

    @Autowired
    private TaskPendingHolder taskPendingHolder;

    @KafkaListener(topics = "#{'${austin.topic.name}'}")
    public void consumer(ConsumerRecord<?, String> consumerRecord, @Header(KafkaHeaders.GROUP_ID) String groupId) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if(kafkaMessage.isPresent()){
            List<TaskInfo> taskInfos = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);
            String messageGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(taskInfos.get(0));
            //只有消息中的groupId 与 当前消费者的groupId相等才去发送短信
            if(groupId.equals(messageGroupId)){
                log.info("【"+groupId+"】消费开始：" + JSON.toJSONString(taskInfos));
                for(TaskInfo taskInfo : taskInfos){
                    LogUtils.print(LogParam.builder().bizType(LOG_BIZ_TYPE).object(taskInfo).build(), AnchorInfo.builder().ids(taskInfo.getReceiver()).businessId(taskInfo.getBusinessId()).state(AnchorState.RECEIVE.getCode()).build());
                    Task task = context.getBean(Task.class).setTaskInfo(taskInfo);
                    taskPendingHolder.route(groupId).execute(task);
                }
            }
        }
    }
}
