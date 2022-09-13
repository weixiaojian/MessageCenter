package com.imwj.msg.api.impl.service;

import com.imwj.msg.api.domain.SendRequest;
import com.imwj.msg.api.domain.SendResponse;
import com.imwj.msg.api.impl.domain.SendTaskModel;
import com.imwj.msg.api.service.RecallService;
import com.imwj.msg.common.vo.BasicResultVO;
import com.imwj.msg.support.pipeline.ProcessContext;
import com.imwj.msg.support.pipeline.ProcessController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wj
 * @create 2022-09-08 17:54
 */
@Service
public class RecallServiceImpl implements RecallService {

    @Autowired
    private ProcessController processController;

    @Override
    public SendResponse recall(SendRequest sendRequest) {
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .build();
        ProcessContext context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(BasicResultVO.success()).build();
        ProcessContext process = processController.process(context);
        return new SendResponse(process.getResponse().getStatus(), process.getResponse().getMsg());
    }
}
