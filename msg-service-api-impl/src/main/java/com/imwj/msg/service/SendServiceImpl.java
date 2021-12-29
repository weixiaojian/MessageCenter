package com.imwj.msg.service;

import com.imwj.msg.domain.*;
import com.imwj.msg.pipeline.ProcessController;
import com.imwj.msg.vo.BasicResultVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;

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
    public SendResponse send(SendRequest sendRequest) {
        //发送消息任务模型封装
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .messageParamList(Arrays.asList(sendRequest.getMessageParam()))
                .build();
        //责任链上下文封装
        ProcessContext context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(BasicResultVO.success()).build();

        ProcessContext process = processController.process(context);

        return new SendResponse(process.getResponse().getCode(), process.getResponse().getMsg());
    }

    @Override
    public SendResponse batchSend(BatchSendRequest batchSendRequest) {
        return null;
    }
}
