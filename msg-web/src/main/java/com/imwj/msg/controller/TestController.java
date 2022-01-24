package com.imwj.msg.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.imwj.msg.constant.MessageCenterConstant;
import com.imwj.msg.domain.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author langao_q
 * @since 2021-12-31 16:49
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    private static final String DEDUPLICATION_RULE_KEY = "deduplication";
    private static final String DISCARD_MESSAGE_KEY = "discard";

    @ApolloConfig("message.center")
    private Config config;

    @Value("${discard:000}")
    private String discard_Val;

    @RequestMapping("/getApollo")
    public RetResult getApollo(){
        JSONObject property = JSON.parseObject(config.getProperty(DEDUPLICATION_RULE_KEY, MessageCenterConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT));
        JSONArray discard = JSON.parseArray(config.getProperty(DISCARD_MESSAGE_KEY, MessageCenterConstant.APOLLO_DEFAULT_VALUE_JSON_ARRAY));
        return RetResult.success(discard_Val + property + discard.toJSONString());
    }

}
