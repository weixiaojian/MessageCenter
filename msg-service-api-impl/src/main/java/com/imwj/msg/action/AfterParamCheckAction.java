package com.imwj.msg.action;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.pipeline.ProcessContext;
import com.imwj.msg.domain.SendTaskModel;
import com.imwj.common.domain.TaskInfo;
import com.imwj.common.enums.ChannelType;
import com.imwj.common.enums.IdType;
import com.imwj.common.enums.RespStatusEnum;
import com.imwj.msg.pipeline.BusinessProcess;
import com.imwj.common.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 责任链-后置参数校验
 *
 * @author langao_q
 * @since 2021-12-29 17:45
 */
@Slf4j
public class AfterParamCheckAction implements BusinessProcess {

    /**
     * 手机号正则校验
     */
    public static final String PHONE_REGEX_EXP = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$";

    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();
        List<TaskInfo> taskInfo = sendTaskModel.getTaskInfo();

        //1.过滤掉不合法的手机号
        filterIllegalPhoneNum(taskInfo);

        //2.过滤掉数据库中黑名单的手机号...（略）

        //3.如果业务参数为空 终止程序
        if (CollUtil.isEmpty(taskInfo)) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS));
            return;
        }
    }

    /**
     * 过滤不合法的手机号
     *
     * @param taskInfo
     */
    private void filterIllegalPhoneNum(List<TaskInfo> taskInfo) {
        Integer idType = taskInfo.get(0).getIdType();
        Integer sendChannel = taskInfo.get(0).getSendChannel();
        if (IdType.PHONE.getCode().equals(idType) && ChannelType.SMS.getCode().equals(sendChannel)) {
            Iterator<TaskInfo> iterator = taskInfo.iterator();
            while (iterator.hasNext()){
                TaskInfo task = iterator.next();
                Set<String> illegalPhone = task.getReceiver().stream().filter(phone -> !ReUtil.isMatch(PHONE_REGEX_EXP, phone))
                        .collect(Collectors.toSet());
                //将不合法的手机号清除
                if(CollUtil.isNotEmpty(illegalPhone)){
                    task.getReceiver().remove(illegalPhone);
                    log.error("{} find illegal phone!{}", task.getMessageTemplateId(), JSON.toJSONString(illegalPhone));
                }
                //将手机号为空的清除（在删除元素时不破坏遍历）
                if (CollUtil.isEmpty(task.getReceiver())) {
                    iterator.remove();
                }
            }
        }
    }
}
