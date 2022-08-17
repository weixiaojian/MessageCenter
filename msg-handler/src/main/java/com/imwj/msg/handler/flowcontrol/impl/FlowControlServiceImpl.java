package com.imwj.msg.handler.flowcontrol.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.google.common.util.concurrent.RateLimiter;
import com.imwj.msg.common.constant.MessageCenterConstant;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.enums.ChannelType;
import com.imwj.msg.handler.eunms.RateLimitStrategy;
import com.imwj.msg.handler.flowcontrol.FlowControlParam;
import com.imwj.msg.handler.flowcontrol.FlowControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 限流服务类
 * @author wj
 * @create 2022-08-17 17:22
 */
@Service
@Slf4j
public class FlowControlServiceImpl implements FlowControlService {

    private static final String FLOW_CONTROL_KEY = "flowControl";

    private static final String FLOW_CONTROL_PREFIX = "flow_control_";

    /**
     * apollo配置示例：key：flowControl value：{"flow_control_40":1}
     */
    @ApolloConfig("message.center")
    private Config config;


    @Override
    public void flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
        RateLimiter rateLimiter = flowControlParam.getRateLimiter();
        Double rateInitValue = flowControlParam.getRateInitValue();

        double costTime = 0;

        // 对比 初始限流值 与 配置限流值，以 配置中心的限流值为准
        Double rateLimitConfig = getRateLimitConfig(taskInfo.getSendChannel());
        if (rateLimitConfig != null && !rateInitValue.equals(rateLimitConfig)) {
            rateLimiter = RateLimiter.create(rateLimitConfig);
            flowControlParam.setRateInitValue(rateLimitConfig);
            flowControlParam.setRateLimiter(rateLimiter);
        }
        if (RateLimitStrategy.REQUEST_RATE_LIMIT.equals(flowControlParam.getRateLimitStrategy())) {
            costTime = rateLimiter.acquire(1);
        }
        if (RateLimitStrategy.SEND_USER_NUM_RATE_LIMIT.equals(flowControlParam.getRateLimitStrategy())) {
            costTime = rateLimiter.acquire(taskInfo.getReceiver().size());
        }

        if (costTime > 0) {
            log.info("consumer {} flow control time {}",
                    ChannelType.getEnumByCode(taskInfo.getSendChannel()).getDescription(), costTime);
        }
    }

    /**
     * 得到限流值的配置
     * <p>
     * apollo配置样例     key：flowControl value：{"flow_control_40":1}
     * <p>
     * 渠道枚举可看：com.imwj.msg.common.enums.ChannelType
     * @param channelCode
     */
    private Double getRateLimitConfig(Integer channelCode) {
        String flowControlConfig = config.getProperty(FLOW_CONTROL_KEY, MessageCenterConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT);
        JSONObject jsonObject = JSON.parseObject(flowControlConfig);
        if (jsonObject.getDouble(FLOW_CONTROL_PREFIX + channelCode) == null) {
            return null;
        }
        return jsonObject.getDouble(FLOW_CONTROL_PREFIX + channelCode);
    }
}