package com.imwj.msg.support.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.imwj.msg.common.constant.MessageCenterConstant;
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
     * 获取apollo配置账号信息（应用：key：value）
     * 腾讯云：smsAccount：[{"sms_66":{"url":"sms.tencentcloudapi.com","region":"ap-guangzhou","secretId":"","secretKey":"","smsSdkAppId":"","templateId":"","signName":"","supplierId":10,"supplierName":"腾讯云"}},{"sms_76":{"url":"https://sms.yunpian.com/v2/sms/tpl_batch_send.json","apikey":"ca55d4c856d72e5d589361c4511b5cd7","tpl_id":"5236082","supplierId":20,"supplierName":"云片"}}]
     * qq邮箱：emailAccount：[{"email_66":{"host":"smtp.qq.com","port":465,"user":"langao_q@qq.com","pass":"","from":"langao_q@qq.com"}}]
     * 企业微信参数示例：enterpriseWechatAccount：[{"enterprise_wechat_66":{"corpId":"wwf87603333e00069c","corpSecret":"-IFWxS2222QxzPIorNVUQn144444D915DM","agentId":10044442,"token":"rXROB3333Kf6i","aesKey":"MKZtoFxHIM44444M7ieag3r9ZPUsl"}}]
     * 钉钉自定义机器人参数实例：dingDingRobotAccount：[{"ding_ding_robot_10":{"secret":"SEC996d8d9d4768aded74114faae924f229229de444475a1c295d64fedf","webhook":"https://oapi.dingtalk.com/robot/send?access_token=8d03b644ffb6534b203d87333367328b0c3003d164715d2c6c6e56"}}]
     * 钉钉工作消息参数示例：dingDingWorkNoticeAccount：[{"ding_ding_work_notice_10":{"appKey":"dingh6yyyyyyycrlbx","appSecret":"tQpvmkR863333yyyyyHP3QHyyyymy9Ao1yoL1oQX5NsdfsWHvWKbTu","agentId":"1523123123183622"}}]
     * 微信公众号模板消息：officialAccount：[{"official_66":{"appId":"wx6447c1fe6b20697b","secret":"4b912ad95576639faa948011bfce311f","token":"langao","aesKey":"2"}}]
     * 微信小程序订阅消息：miniProgramAccount：[{"mini_program_66":{"appId":"wxacdb39ee66e9f4dc","appSecret":"a1399286f82fe0f2608edfe8d313ac1d"}}]
     * app个推：geTuiAccount：[{"ge_tui_account_66":{"appId":"7ytre3Mc5k9ePdz8InAwG8","appKey":"l7y3zWkP4pAI0rky5IHUg3","masterSecret":"sXRFC1QNgJA5OYfPqGMAD3"}}]
     * @param sendAccount 发送账户
     * @param apolloKey apollo中的key
     * @param prefix    前缀
     * @param clazz         返回实体类型
     * @return
     */
    public <T> T getAccount(Integer sendAccount, String apolloKey, String prefix, Class<T> clazz) {
        String accountValues = config.getProperty(apolloKey, MessageCenterConstant.APOLLO_DEFAULT_VALUE_JSON_ARRAY);
        JSONArray jsonArray = JSON.parseArray(accountValues);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            T object = jsonObject.getObject(prefix + sendAccount, clazz);
            if (object != null) {
                return object;
            }
        }
        return null;
    }

}
