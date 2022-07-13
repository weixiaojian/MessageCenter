package com.imwj.msg.handler.receiver;

import com.alibaba.fastjson.JSON;
import com.imwj.msg.common.domain.AnchorInfo;
import com.imwj.msg.common.domain.LogParam;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.enums.AnchorState;
import com.imwj.msg.handler.pending.Task;
import com.imwj.msg.handler.pending.TaskPendingHolder;
import com.imwj.msg.handler.utils.GroupIdMappingUtils;
import com.imwj.msg.support.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * kafka消费者（发送消息监听器）
 * @author langao_q
 * @since 2021-12-30 10:43
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MsgReceiver {

    private static final String LOG_BIZ_TYPE = "Receiver#consumer";

    @Autowired
    private ApplicationContext context;
    @Autowired
    private TaskPendingHolder taskPendingHolder;
    @Autowired
    private LogUtils logUtils;

    @KafkaListener(topics = "#{'${msg.business.topic.name}'}")
    public void consumer(ConsumerRecord<?, String> consumerRecord, @Header(KafkaHeaders.GROUP_ID) String groupId) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if(kafkaMessage.isPresent()){
            List<TaskInfo> taskInfos = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);
            String messageGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(taskInfos.get(0));
            //只有消息中的groupId 与 当前消费者的groupId相等才去发送短信
            if(groupId.equals(messageGroupId)){
                log.info("【"+groupId+"】消费开始：" + JSON.toJSONString(taskInfos));
                for(TaskInfo taskInfo : taskInfos){
                    logUtils.print(LogParam.builder().bizType(LOG_BIZ_TYPE).object(taskInfo).build(), AnchorInfo.builder().ids(taskInfo.getReceiver()).businessId(taskInfo.getBusinessId()).state(AnchorState.RECEIVE.getCode()).build());
                    Task task = context.getBean(Task.class).setTaskInfo(taskInfo);
                    taskPendingHolder.route(groupId).execute(task);
                }
            }
        }
    }
}
