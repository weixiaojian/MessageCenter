package com.imwj.msg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.imwj.msg.constant.AustinConstant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author langao_q
 * @since 2021-12-28 10:48
 */
@SpringBootTest
class MsgApplicationTest {

    private static final String DEDUPLICATION_RULE_KEY = "deduplication";

    /**
     * 从aplllo配置中获取配置
     */
    @ApolloConfig("message.center")
    private Config config;

    @Test
    public void test(){
        JSONObject property = JSON.parseObject(config.getProperty(DEDUPLICATION_RULE_KEY, AustinConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT));
        System.out.println(property.toJSONString());
    }

}
