package com.imwj.msg.api.impl.domain;

import com.imwj.msg.api.domain.MessageParam;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.support.domain.MessageTemplate;
import com.imwj.msg.support.pipeline.ProcessModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发送消息任务模型
 * @author langao_q
 * @since 2021-12-29 17:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendTaskModel implements ProcessModel {
    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 请求参数
     */
    private List<MessageParam> messageParamList;

    /**
     * 发送任务的信息
     */
    private List<TaskInfo> taskInfo;

    /**
     * 撤回任务的信息
     */
    private MessageTemplate messageTemplate;
}
