package com.imwj.msg.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Throwables;
import com.imwj.msg.api.impl.domain.SendTaskModel;
import com.imwj.msg.common.enums.RespStatusEnum;
import com.imwj.msg.common.vo.BasicResultVO;
import com.imwj.msg.support.pipeline.BusinessProcess;
import com.imwj.msg.support.pipeline.ProcessContext;
import com.imwj.msg.support.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private KafkaUtils kafkaUtils;

    @Value("${msg.business.topic.name}")
    private String topicName;

    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();
        String message = JSON.toJSONString(sendTaskModel.getTaskInfo(), new SerializerFeature[]{SerializerFeature.WriteClassName});

        try {
            kafkaUtils.send(topicName, message);
        }catch (Exception e){
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR));
            log.error("send kafka fail! e:{},params:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(CollUtil.getFirst(sendTaskModel.getTaskInfo().listIterator())));
        }
    }
}
