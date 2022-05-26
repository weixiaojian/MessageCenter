package com.imwj.msg.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.imwj.common.constant.MessageCenterConstant;
import org.springframework.stereotype.Component;

/**
 * 获取apollo配置的账号信息（腾讯短信/qq邮箱）
 * @author langao_q
 * @since 2022-01-12 10:57
 */
@Component
public class AccountUtils {

    @ApolloConfig("message.center")
    private Config config;

    /**
     * 获取apollo配置账号信息
     * 腾讯云：smsAccount：[{"sms_66":{"url":"sms.tencentcloudapi.com","region":"ap-guangzhou","secretId":"","secretKey":"","smsSdkAppId":"","templateId":"","signName":"","supplierId":10,"supplierName":"腾讯云"}}]
     * qq邮箱：emailAccount：[{"email_66":{"host":"smtp.qq.com","port":465,"user":"langao_q@qq.com","pass":"","from":"langao_q@qq.com"}}]
     * @param sendAccount 发送账户
     * @param apolloKey apollo中的key
     * @param prefix    前缀
     * @param t         返回实体类型
     * @return
     */
    public <T> T getAccount(Integer sendAccount, String apolloKey, String prefix, T t) {
        String accountValues = config.getProperty(apolloKey, MessageCenterConstant.APOLLO_DEFAULT_VALUE_JSON_ARRAY);
        JSONArray jsonArray = JSON.parseArray(accountValues);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Object object = jsonObject.getObject(prefix + sendAccount, t.getClass());
            if (object != null) {
                return (T) object;
            }
        }
        return null;
    }

}
