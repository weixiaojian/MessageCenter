package com.imwj.msg.handler.flowcontrol.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.handler.eunms.RateLimitStrategy;
import com.imwj.msg.handler.flowcontrol.FlowControlParam;
import com.imwj.msg.handler.flowcontrol.FlowControlService;
import com.imwj.msg.handler.flowcontrol.annotaions.LocalRateLimit;

/**
 * 请求限流服务
 * @author wj
 * @create 2022-09-22 17:36
 */
@LocalRateLimit(rateLimitStrategy = RateLimitStrategy.REQUEST_RATE_LIMIT)
public class RequestRateLimitService implements FlowControlService {

    /**
     * 根据渠道进行流量控制
     *
     * @param taskInfo
     * @param flowControlParam
     */
    @Override
    public Double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
        RateLimiter rateLimiter = flowControlParam.getRateLimiter();
        return rateLimiter.acquire(1);
    }
}
