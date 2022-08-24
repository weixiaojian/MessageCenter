package com.imwj.msg.handler.script.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.common.constant.SendAccountConstant;
import com.imwj.msg.common.enums.SmsStatus;
import com.imwj.msg.handler.domain.sms.SmsParam;
import com.imwj.msg.common.dto.account.TencentSmsAccount;
import com.imwj.msg.handler.script.SmsScript;
import com.imwj.msg.support.domain.SmsRecord;
import com.imwj.msg.support.utils.AccountUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 腾讯短信发送类
 * 文档地址：https://cloud.tencent.com/document/api/382/55981
 *
 * @author langao_q
 * @since 2021-12-30 16:01
 */
@Slf4j
@Service
public class TencentSmsScript implements SmsScript {

    private static final Integer PHONE_NUM = 11;

    @Autowired
    private AccountUtils accountUtils;

    @Override
    public List<SmsRecord> send(SmsParam smsParam) throws Exception {
        //获取apollo中的腾讯账号配置
        TencentSmsAccount tencentSmsAccount = accountUtils.getAccount(smsParam.getSendAccount(), SendAccountConstant.SMS_ACCOUNT_KEY, SendAccountConstant.SMS_PREFIX, TencentSmsAccount.class);
        SmsClient client = init(tencentSmsAccount);
        SendSmsRequest request = assembleReq(tencentSmsAccount, smsParam);
        log.info("发送短信开始：{}", JSON.toJSONString(request));
        SendSmsResponse response = client.SendSms(request);
        log.info("发送短信结束：{}", JSON.toJSONString(response));

        return assembleSmsRecord(smsParam, response, tencentSmsAccount);
    }

    /**
     * 解析短信发送结果 返回短信发送记录实体
     */
    private List<SmsRecord> assembleSmsRecord(SmsParam smsParam, SendSmsResponse response, TencentSmsAccount tencentSmsParam) {
        if (response == null || ArrayUtil.isEmpty(response.getSendStatusSet())) {
            return null;
        }
        List<SmsRecord> smsRecordList = new ArrayList<>();
        for (SendStatus sendStatus : response.getSendStatusSet()) {
            // 腾讯返回的电话号有前缀，这里取巧直接翻转获取手机号
            String phone = new StringBuilder(new StringBuilder(sendStatus.getPhoneNumber())
                    .reverse().substring(0, PHONE_NUM)).reverse().toString();
            SmsRecord smsRecord = SmsRecord.builder()
                    .sendDate(Integer.valueOf(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)))
                    .messageTemplateId(smsParam.getMessageTemplateId())
                    .phone(Long.valueOf(phone))
                    .supplierId(tencentSmsParam.getSupplierId())
                    .supplierName(tencentSmsParam.getSupplierName())
                    .msgContent(smsParam.getContent())
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
    private SendSmsRequest assembleReq(TencentSmsAccount tencentSmsParam, SmsParam smsParam) {
        SendSmsRequest req = new SendSmsRequest();
        String[] phoneNumberSet1 = smsParam.getPhones().toArray(new String[smsParam.getPhones().size() - 1]);
        req.setPhoneNumberSet(phoneNumberSet1);
        req.setSmsSdkAppId(tencentSmsParam.getSmsSdkAppId());
        req.setSignName(tencentSmsParam.getSignName());
        req.setTemplateId(tencentSmsParam.getTemplateId());
        String[] templateParamSet1 = {smsParam.getContent()};
        req.setTemplateParamSet(templateParamSet1);
        req.setSessionContext(IdUtil.fastSimpleUUID());
        return req;
    }

    /**
     * 初始化client
     */
    private SmsClient init(TencentSmsAccount tencentSmsParam) {
        Credential cred = new Credential(tencentSmsParam.getSecretId(), tencentSmsParam.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(tencentSmsParam.getUrl());
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        SmsClient client = new SmsClient(cred, tencentSmsParam.getRegion(), clientProfile);
        return client;
    }
}
