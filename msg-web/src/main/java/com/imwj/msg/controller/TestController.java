package com.imwj.msg.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.imwj.msg.constant.AustinConstant;
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

    @ApolloConfig("message.center")
    private Config config;

    @Value("${test:666666}")
    private String test;

    @RequestMapping("/getApollo")
    public RetResult getApollo(){

        log.info("test的值：{}" , test);

        JSONObject property = JSON.parseObject(config.getProperty(DEDUPLICATION_RULE_KEY, AustinConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT));
        return RetResult.success(property);
    }

}
