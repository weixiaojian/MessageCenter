package com.imwj.msg.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Throwables;
import com.imwj.msg.domain.ProcessContext;
import com.imwj.msg.domain.SendTaskModel;
import com.imwj.msg.enums.RespStatusEnum;
import com.imwj.msg.pipeline.BusinessProcess;
import com.imwj.msg.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.Resource;

/**
 * 责任链-发送消息到MQ
 * @author langao_q
 * @since 2021-12-29 17:46
 */
@Slf4j
public class SendMqAction implements BusinessProcess {

    @Resource
    private KafkaTemplate kafkaTemplate;

    @Value("${austin.topic.name}")
    private String topicName;

    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();
        try {
            kafkaTemplate.send(topicName, JSON.toJSONString(sendTaskModel.getTaskInfo(), new SerializerFeature[] {SerializerFeature.WriteClassName}));
        }catch (Exception e){
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR));
            log.error("send kafka fail! e:{},params:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(sendTaskModel.getTaskInfo().get(0)));
        }
    }
}
