package com.imwj.msg.api.impl.action;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.imwj.msg.api.domain.MessageParam;
import com.imwj.msg.api.impl.domain.SendTaskModel;
import com.imwj.msg.common.enums.RespStatusEnum;
import com.imwj.msg.common.vo.BasicResultVO;
import com.imwj.msg.support.pipeline.BusinessProcess;
import com.imwj.msg.support.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 责任链-前置参数校验
 * @author langao_q
 * @since 2021-12-29 17:44
 */
@Slf4j
@Service
public class PreParamCheckAction implements BusinessProcess<SendTaskModel> {

    /**
     * 最大的收件人数
     */
    private static final Integer BATCH_RECEIVER_SIZE = 100;

    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel sendTaskModel = context.getProcessModel();

        Long messageTemplateId = sendTaskModel.getMessageTemplateId();
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();
        // 1.校验模板id和请求参数集合是否为空
        if(messageTemplateId == null || messageParamList == null){
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS));
            return;
        }

        // 2.校验手机号是否为空
        List<MessageParam> resultCollect = messageParamList.stream()
                .filter(messageParam -> StrUtil.isNotBlank(messageParam.getReceiver()))
                .collect(Collectors.toList());
        if(CollectionUtil.isEmpty(resultCollect)){
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS));
            return;
        }

        // 3.过滤receiver大于100的请求
        if (messageParamList.stream().anyMatch(messageParam -> messageParam.getReceiver().split(StrUtil.COMMA).length > BATCH_RECEIVER_SIZE)) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.TOO_MANY_RECEIVER));
            return;
        }

        // 将正确结果的手机号集合带到下个处理类
        sendTaskModel.setMessageParamList(resultCollect);
    }
}
