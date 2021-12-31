package com.imwj.msg.service.deduplication;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.imwj.msg.constant.AustinConstant;
import com.imwj.msg.domain.DeduplicationParam;
import com.imwj.msg.domain.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 去重服务（真正的去重业务类）
 * @author langao_q
 * @since 2021-12-31 15:20
 */
@Service
public class DeduplicationRuleService {

    /**
     * 配置样例：{"contentDeduplication":{"num":1,"time":300},"frequencyDeduplication":{"num":5}}
     */
    private static final String DEDUPLICATION_RULE_KEY = "deduplication";
    private static final String CONTENT_DEDUPLICATION = "contentDeduplication";
    private static final String FREQUENCY_DEDUPLICATION = "frequencyDeduplication";
    private static final String TIME = "time";
    private static final String NUM = "num";

    @Autowired
    private ContentDeduplicationService contentDeduplicationService;

    @Autowired
    private FrequencyDeduplicationService frequencyDeduplicationService;

    /**
     * 从aplllo配置中获取配置
     */
    @ApolloConfig("message.center")
    private Config config;

    public void duplication(TaskInfo taskInfo) {
        JSONObject property = JSON.parseObject(config.getProperty(DEDUPLICATION_RULE_KEY, AustinConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT));
        JSONObject contentDeduplication = property.getJSONObject(CONTENT_DEDUPLICATION);
        JSONObject frequencyDeduplication = property.getJSONObject(FREQUENCY_DEDUPLICATION);

        //文案去重
        DeduplicationParam contentParams = DeduplicationParam.builder()
                .deduplicationTime(contentDeduplication.getLong(TIME))
                .countNum(contentDeduplication.getInteger(NUM)).taskInfo(taskInfo)
                .build();
        contentDeduplicationService.deduplication(contentParams);

        //运营规则去重
        Long seconds = (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000;
        DeduplicationParam businessParams = DeduplicationParam.builder()
                .deduplicationTime(seconds)
                .countNum(frequencyDeduplication.getInteger(NUM)).taskInfo(taskInfo)
                .build();
        frequencyDeduplicationService.deduplication(businessParams);
    }

}
