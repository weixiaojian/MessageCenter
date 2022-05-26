package com.imwj.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 短信状态信息
 * @author langao_q
 * @since 2021-12-29 11:39
 */
@Getter
@ToString
@AllArgsConstructor
public enum SmsStatus {

    /**
     * 各个枚举值
     */
    SEND_SUCCESS(10,"调用渠道接口发送成功"),
    RECEIVE_SUCCESS(20,"用户收到短信(收到渠道短信回执，状态成功)"),
    RECEIVE_FAIL(30, "用户收不到短信(收到渠道短信回执，状态失败)");

    private Integer code;
    private String description;


}
