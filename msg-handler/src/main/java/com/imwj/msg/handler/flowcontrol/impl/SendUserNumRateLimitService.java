package com.imwj.msg.handler.flowcontrol.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.handler.eunms.RateLimitStrategy;
import com.imwj.msg.handler.flowcontrol.FlowControlParam;
import com.imwj.msg.handler.flowcontrol.FlowControlService;
import com.imwj.msg.handler.flowcontrol.annotaions.LocalRateLimit;

/**
 * @author wj
 * @create 2022-09-22 17:38
 */
@LocalRateLimit(rateLimitStrategy = RateLimitStrategy.SEND_USER_NUM_RATE_LIMIT)
public class SendUserNumRateLimitService implements FlowControlService {

    /**
     * 根据渠道进行流量控制
     *
     * @param taskInfo
     * @param flowControlParam
     */
    @Override
    public Double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
        RateLimiter rateLimiter = flowControlParam.getRateLimiter();
        return rateLimiter.acquire(taskInfo.getReceiver().size());
    }
}
