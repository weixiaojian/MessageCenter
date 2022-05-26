package com.imwj.msg.service;

import cn.monitor4all.logRecord.annotation.OperationLog;
import com.imwj.msg.domain.BatchSendRequest;
import com.imwj.msg.domain.SendRequest;
import com.imwj.msg.domain.SendResponse;
import com.imwj.msg.domain.SendTaskModel;
import com.imwj.msg.pipeline.ProcessContext;
import com.imwj.msg.pipeline.ProcessController;
import com.imwj.common.vo.BasicResultVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * 消息发送实现类
 * @author langao_q
 * @since 2021-12-29 16:52
 */
@Service
public class SendServiceImpl implements SendService{

    @Resource
    private ProcessController processController;

    @Override
    @OperationLog(bizType = "SendService#send", bizId = "#sendRequest.messageTemplateId", msg = "#sendRequest")
    public SendResponse send(SendRequest sendRequest) {
        //发送消息任务模型封装
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .messageParamList(Collections.singletonList(sendRequest.getMessageParam()))
                .build();
        //责任链上下文封装
        ProcessContext context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(BasicResultVO.success()).build();

        ProcessContext process = processController.process(context);
        return new SendResponse(process.getResponse().getStatus(), process.getResponse().getMsg());
    }

    @Override
    public SendResponse batchSend(BatchSendRequest batchSendRequest) {
        return null;
    }
}
