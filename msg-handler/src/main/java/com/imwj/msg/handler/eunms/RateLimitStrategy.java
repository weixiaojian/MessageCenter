package com.imwj.msg.handler.eunms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 限流枚举
 * @author wj
 * @create 2022-08-17 17:20
 */
@Getter
@ToString
@AllArgsConstructor
public enum RateLimitStrategy {


    REQUEST_RATE_LIMIT(10, "根据真实请求数限流"),
    SEND_USER_NUM_RATE_LIMIT(20, "根据发送用户数请求数限流"),
    ;

    private Integer code;
    private String description;


}
