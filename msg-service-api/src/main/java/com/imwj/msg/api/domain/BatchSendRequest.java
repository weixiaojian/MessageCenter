package com.imwj.msg.api.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author langao_q
 * @since 2021-12-29 16:49
 */
@Data
@Accessors(chain = true)
public class BatchSendRequest {

    /**
     * 执行业务类型
     */
    private String code;

    /**
     * 消息模板Id
     */
    private Long messageTemplateId;


    /**
     * 消息相关的参数
     */
    private List<MessageParam> messageParams;
}
