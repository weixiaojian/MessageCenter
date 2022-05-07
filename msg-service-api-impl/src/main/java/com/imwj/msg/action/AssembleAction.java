package com.imwj.msg.action;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.imwj.msg.constant.MessageCenterConstant;
import com.imwj.msg.dao.MessageTemplateDao;
import com.imwj.msg.domain.*;
import com.imwj.msg.dto.ContentModel;
import com.imwj.msg.enums.ChannelType;
import com.imwj.msg.enums.RespStatusEnum;
import com.imwj.msg.pipeline.BusinessProcess;
import com.imwj.msg.pipeline.ProcessContext;
import com.imwj.msg.utils.ContentHolderUtil;
import com.imwj.msg.utils.TaskInfoUtils;
import com.imwj.msg.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 责任链-参数拼接
 *
 * @author langao_q
 * @since 2021-12-29 17:46
 */
@Slf4j
public class AssembleAction implements BusinessProcess {

    @Resource
    private MessageTemplateDao messageTemplateDao;

    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();
        Long messageTemplateId = sendTaskModel.getMessageTemplateId();
        try {
            MessageTemplate messageTemplate = messageTemplateDao.selectById(messageTemplateId);
            if (messageTemplate == null || messageTemplate.getIsDeleted().equals(MessageCenterConstant.TRUE)) {
                context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.TEMPLATE_NOT_FOUND));
                return;
            }
            List<TaskInfo> taskInfos = assembleTaskInfo(sendTaskModel, messageTemplate);
            sendTaskModel.setTaskInfo(taskInfos);
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR));
            log.error("assemble task fail! templateId:{}, e:{}", messageTemplateId, Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 拼接TaskInfo任务消息
     *
     * @param sendTaskModel
     * @param messageTemplate
     * @return
     */
    private List<TaskInfo> assembleTaskInfo(SendTaskModel sendTaskModel, MessageTemplate messageTemplate) {
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();
        List<TaskInfo> taskInfoList = new ArrayList<>();
        for (MessageParam messageParam : messageParamList) {
            TaskInfo taskInfo = TaskInfo.builder()
                    .messageTemplateId(messageTemplate.getId())
                    .businessId(TaskInfoUtils.generateBusinessId(messageTemplate.getId(), messageTemplate.getTemplateType()))
                    .receiver(new HashSet<>(Arrays.asList(messageParam.getReceiver().split(String.valueOf(StrUtil.C_COMMA)))))
                    .idType(messageTemplate.getIdType())
                    .sendChannel(messageTemplate.getSendChannel())
                    .msgType(messageTemplate.getMsgType())
                    .sendAccount(messageTemplate.getSendAccount())
                    .contentModel(getContentModelValue(messageTemplate, messageParam))
                    .deduplicationTime(messageTemplate.getDeduplicationTime())
                    .isNightShield(messageTemplate.getIsNightShield())
                    .build();
            taskInfoList.add(taskInfo);
        }
        return taskInfoList;
    }

    /**
     * 获取发送内容的模型
     * @param messageTemplate
     * @param messageParam
     * @return
     */
    private ContentModel getContentModelValue(MessageTemplate messageTemplate, MessageParam messageParam) {
        //得到真正的内容模型
        Integer sendChannel = messageTemplate.getSendChannel();
        Class chanelModelClass = ChannelType.getChanelModelClassByCode(sendChannel);

        //得到模板的 msgContext 和 入参
        Map<String, String> variables = messageParam.getVariables();
        JSONObject jsonObject = JSON.parseObject(messageTemplate.getMsgContent());

        //通过反射组装出内容模型
        Field[] fields = ReflectUtil.getFields(chanelModelClass);
        ContentModel contentModel = (ContentModel) ReflectUtil.newInstance(chanelModelClass);
        for(Field field : fields){
            String originValue = jsonObject.getString(field.getName());

            if(StrUtil.isNotBlank(originValue)){
                String resultValue = ContentHolderUtil.replacePlaceHolder(originValue, variables);
                ReflectUtil.setFieldValue(contentModel, field, resultValue);
            }
        }
        // 如果 url 字段存在，则在url拼接对应的埋点参数
        String url = (String) ReflectUtil.getFieldValue(contentModel, "url");
        if (StrUtil.isNotBlank(url)) {
            String resultUrl = TaskInfoUtils.generateUrl(url, messageTemplate.getId(), messageTemplate.getTemplateType());
            ReflectUtil.setFieldValue(contentModel, "url", resultUrl);
        }
        return contentModel;
    }

}
