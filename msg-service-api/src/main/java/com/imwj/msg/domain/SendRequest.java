package com.imwj.msg.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 发送接口请求实体
 * @author langao_q
 * @since 2021-12-29 16:47
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SendRequest {

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
    private MessageParam messageParam;
}
