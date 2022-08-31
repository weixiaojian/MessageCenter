package com.imwj.msg.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.api.impl.domain.SendTaskModel;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.enums.IdType;
import com.imwj.msg.common.enums.RespStatusEnum;
import com.imwj.msg.common.vo.BasicResultVO;
import com.imwj.msg.support.pipeline.BusinessProcess;
import com.imwj.msg.support.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
@Service
public class AfterParamCheckAction implements BusinessProcess<SendTaskModel> {

    /**
     * 手机号正则校验
     * 邮箱正则校验
     */
    public static final String PHONE_REGEX_EXP = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$";
    public static final String EMAIL_REGEX_EXP = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\\\.[a-zA-Z0-9]+";

    public static final HashMap<Integer, String> CHANNEL_REGEX_EXP = new HashMap<>();
    static {
        CHANNEL_REGEX_EXP.put(IdType.PHONE.getCode(), PHONE_REGEX_EXP);
        CHANNEL_REGEX_EXP.put(IdType.EMAIL.getCode(), EMAIL_REGEX_EXP);
    }

    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel sendTaskModel = context.getProcessModel();
        List<TaskInfo> taskInfo = sendTaskModel.getTaskInfo();

        //1.过滤掉不合法的手机号、邮件
        filterIllegalReceiver(taskInfo);

        //2.过滤掉数据库中黑名单的手机号...（略）

        //3.如果业务参数为空 终止程序
        if (CollUtil.isEmpty(taskInfo)) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS));
            return;
        }
    }

    /**
     * 过滤不合法的手机号
     * 过滤不合法的邮件
     * @param taskInfo
     */
    private void filterIllegalReceiver(List<TaskInfo> taskInfo) {
        Integer idType = CollUtil.getFirst(taskInfo.iterator()).getIdType();
        filter(taskInfo, CHANNEL_REGEX_EXP.get(idType));
    }

    /**
     * 根据指的正则来校验收件人的合法性(手机号、邮箱)
     * @param taskInfo
     * @param regexExp
     */
    private void filter(List<TaskInfo> taskInfo, String regexExp) {
        Iterator<TaskInfo> iterator = taskInfo.iterator();
        while (iterator.hasNext()){
            TaskInfo task = iterator.next();
            Set<String> illegalPhone = task.getReceiver().stream().filter(phone -> !ReUtil.isMatch(PHONE_REGEX_EXP, phone))
                    .collect(Collectors.toSet());
            //将不合法的手机号清除
            if(CollUtil.isNotEmpty(illegalPhone)){
                task.getReceiver().remove(illegalPhone);
                log.error("messageTemplateId{} find illegal receiver!{}", task.getMessageTemplateId(), JSON.toJSONString(illegalPhone));
            }
            //将手机号为空的清除（在删除元素时不破坏遍历）
            if (CollUtil.isEmpty(task.getReceiver())) {
                iterator.remove();
            }
        }
    }
}
