package com.imwj.msg.script;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.imwj.msg.constant.AustinConstant;
import com.imwj.msg.domain.SmsParam;
import com.imwj.msg.domain.SmsRecord;
import com.imwj.msg.enums.SmsStatus;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 腾讯短信发送类
 * 文档地址：https://cloud.tencent.com/document/api/382/55981
 * @author langao_q
 * @since 2021-12-30 16:01
 */
@Slf4j
@Service
public class TencentSmsScript implements SmsScript{

    private static final Integer PHONE_NUM = 11;

    /**
     * api相关
     */
    private static final String URL = "sms.tencentcloudapi.com";
    private static final String REGION = "ap-guangzhou";

    /**
     * 账号相关
     */
    @Value("${tencent.sms.account.secret-id}")
    private String SECRET_ID;

    @Value("${tencent.sms.account.secret-key}")
    private String SECRET_KEY;

    @Value("${tencent.sms.account.sms-sdk-app-id}")
    private String SMS_SDK_APP_ID;

    @Value("${tencent.sms.account.template-id}")
    private String TEMPLATE_ID;

    @Value("${tencent.sms.account.sign_name}")
    private String SIGN_NAME;


    @Override
    public List<SmsRecord> send(SmsParam smsParam) {
        try {
            SmsClient client = init();
            SendSmsRequest request = assembleReq(smsParam);
            log.info("发送短信开始：{}", JSON.toJSONString(request));
            SendSmsResponse response = client.SendSms(request);
            log.info("发送短信结束：{}", JSON.toJSONString(response));

            return assembleSmsRecord(smsParam,response);
        } catch (Exception e) {
            log.error("send tencent sms fail!{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
            return null;
        }
    }

    /**
     * 解析短信发送结果 返回短信发送记录实体
     */
    private List<SmsRecord> assembleSmsRecord(SmsParam smsParam, SendSmsResponse response) {
        if (response == null || ArrayUtil.isEmpty(response.getSendStatusSet())) {
            return null;
        }
        List<SmsRecord> smsRecordList = new ArrayList<>();
        for (SendStatus sendStatus : response.getSendStatusSet()) {
            // 腾讯返回的电话号有前缀，这里取巧直接翻转获取手机号
            String phone = new StringBuilder(new StringBuilder(sendStatus.getPhoneNumber())
                    .reverse().substring(0, PHONE_NUM)).reverse().toString();
            SmsRecord smsRecord = SmsRecord.builder()
                    .sendDate(Integer.valueOf(DateUtil.format(new Date(), AustinConstant.YYYYMMDD)))
                    .messageTemplateId(smsParam.getMessageTemplateId())
                    .phone(Long.valueOf(phone))
                    .supplierId(smsParam.getSupplierId())
                    .supplierName(smsParam.getSupplierName())
                    .seriesId(sendStatus.getSerialNo())
                    .chargingNum(Math.toIntExact(sendStatus.getFee()))
                    .status(SmsStatus.SEND_SUCCESS.getCode())
                    .reportContent(sendStatus.getCode())
                    .created(Math.toIntExact(DateUtil.currentSeconds()))
                    .updated(Math.toIntExact(DateUtil.currentSeconds()))
                    .build();

            smsRecordList.add(smsRecord);
        }
        return smsRecordList;
    }

    /**
     * 组装短信发送参数
     */
    private SendSmsRequest assembleReq(SmsParam smsParam){
        SendSmsRequest req = new SendSmsRequest();
        String[] phoneNumberSet1 = smsParam.getPhones().toArray(new String[smsParam.getPhones().size() - 1]);
        req.setPhoneNumberSet(phoneNumberSet1);
        req.setSmsSdkAppId(SMS_SDK_APP_ID);
        req.setSignName(SIGN_NAME);
        req.setTemplateId(TEMPLATE_ID);
        String[] templateParamSet1 = {smsParam.getContent()};
        req.setTemplateParamSet(templateParamSet1);
        req.setSessionContext(IdUtil.fastSimpleUUID());
        return req;
    }

    /**
     * 初始化client
     */
    private SmsClient init(){
        Credential cred = new Credential(SECRET_ID, SECRET_KEY);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(URL);
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        SmsClient client = new SmsClient(cred, REGION, clientProfile);
        return client;
    }
}
