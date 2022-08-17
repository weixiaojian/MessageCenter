package com.imwj.msg.handler.flowcontrol;

import com.google.common.util.concurrent.RateLimiter;
import com.imwj.msg.handler.eunms.RateLimitStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 限流参数
 * @author wj
 * @create 2022-08-17 17:21
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowControlParam {

    /**
     * 限流器
     * 子类初始化的时候指定
     */
    protected RateLimiter rateLimiter;

    /**
     * 限流器初始限流大小
     * 子类初始化的时候指定
     */
    protected Double rateInitValue;

    /**
     * 限流的策略
     * 子类初始化的时候指定
     */
    protected RateLimitStrategy rateLimitStrategy;
}

