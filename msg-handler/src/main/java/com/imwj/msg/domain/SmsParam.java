package com.imwj.msg.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * 短信发送实体
 * @author langao_q
 * @since 2021-12-30 16:00
 */
@Data
@Builder
public class SmsParam {

    /**
     * 业务Id
     */
    private Long messageTemplateId;

    /**
     * 需要发送的手机号
     */
    private Set<String> phones;

    /**
     * 发送文案
     */
    private String content;

    /**
     * 渠道商Id
     */
    private Integer supplierId;

    /**
     * 渠道商名字
     */
    private String supplierName;
}
