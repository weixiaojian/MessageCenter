package com.imwj.msg.action;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.imwj.msg.domain.MessageParam;
import com.imwj.msg.pipeline.ProcessContext;
import com.imwj.msg.domain.SendTaskModel;
import com.imwj.msg.enums.RespStatusEnum;
import com.imwj.msg.pipeline.BusinessProcess;
import com.imwj.msg.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 责任链-前置参数校验
 * @author langao_q
 * @since 2021-12-29 17:44
 */
@Slf4j
public class PreParamCheckAction implements BusinessProcess {
    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();

        Long messageTemplateId = sendTaskModel.getMessageTemplateId();
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();
        //校验模板id和请求参数集合是否为空
        if(messageTemplateId == null || messageParamList == null){
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS));
            return;
        }

        //校验手机号是否为空
        List<MessageParam> resultCollect = messageParamList.stream()
                .filter(messageParam -> StrUtil.isNotBlank(messageParam.getReceiver()))
                .collect(Collectors.toList());
        if(CollectionUtil.isEmpty(resultCollect)){
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS));
            return;
        }
        //将正确结果的手机号集合带到下个处理类
        sendTaskModel.setMessageParamList(resultCollect);
    }
}
