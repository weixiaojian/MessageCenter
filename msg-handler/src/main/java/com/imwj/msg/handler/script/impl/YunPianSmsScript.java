package com.imwj.msg.handler.script.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.common.constant.SendAccountConstant;
import com.imwj.msg.common.dto.account.YunPianSmsAccount;
import com.imwj.msg.common.enums.SmsStatus;
import com.imwj.msg.handler.domain.sms.SmsParam;
import com.imwj.msg.handler.domain.sms.YunPianSendResult;
import com.imwj.msg.handler.script.SmsScript;
import com.imwj.msg.support.domain.SmsRecord;
import com.imwj.msg.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 云片发送短信类
 * 接入文档：https://www.yunpian.com/official/document/sms/zh_CN/domestic_list
 * @author wj
 * @create 2022-08-31 15:25
 */
@Slf4j
@Service
public class YunPianSmsScript implements SmsScript {

    @Autowired
    private AccountUtils accountUtils;

    @Override
    public List<SmsRecord> send(SmsParam smsParam) throws Exception {
        // 获取云片账号
        YunPianSmsAccount account = accountUtils.getAccount(smsParam.getSendAccount(), SendAccountConstant.SMS_ACCOUNT_KEY,
                SendAccountConstant.SMS_PREFIX, YunPianSmsAccount.class);
        // 封装请求参数
        Map<String, String> params = assembleParam(smsParam, account);
        String result = HttpRequest.post(account.getUrl())
                .header(Header.CONTENT_TYPE.getValue(), ContentType.FORM_URLENCODED.getValue())
                .header(Header.ACCEPT.getValue(), ContentType.JSON.getValue())
                .body(JSON.toJSONString(params))
                .timeout(2000)
                .execute().body();
        YunPianSendResult yunPianSendResult = JSONUtil.toBean(result, YunPianSendResult.class);
        return assembleSmsRecord(smsParam, yunPianSendResult, account);
    }

    /**
     * 组装请求参数
     * @param smsParam
     * @param account
     * @return
     */
    private Map<String, String> assembleParam(SmsParam smsParam, YunPianSmsAccount account) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("apikey", account.getApikey());
        paramMap.put("mobile", StringUtils.join(smsParam.getPhones(), StrUtil.C_COMMA));
        paramMap.put("tpl_id", account.getTplId());
        paramMap.put("tpl_value", "");
        return paramMap;
    }

    /**
     * 组装发送记录
     * @param smsParam
     * @param yunPianSendResult
     * @param account
     * @return
     */
    private List<SmsRecord> assembleSmsRecord(SmsParam smsParam, YunPianSendResult yunPianSendResult, YunPianSmsAccount account) {
        if(yunPianSendResult == null || CollectionUtil.isEmpty(yunPianSendResult.getData())){
            return null;
        }
        ArrayList<SmsRecord> smsRecordList = new ArrayList<>();
        yunPianSendResult.getData().forEach(datum -> {
            SmsRecord smsRecord = SmsRecord.builder()
                    .sendDate(Integer.valueOf(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)))
                    .messageTemplateId(smsParam.getMessageTemplateId())
                    .phone(Long.valueOf(datum.getMobile()))
                    .supplierId(account.getSupplierId())
                    .supplierName(account.getSupplierName())
                    .msgContent(smsParam.getContent())
                    .seriesId(String.valueOf(datum.getSid()))
                    .chargingNum(Math.toIntExact(datum.getCount()))
                    .status("0".equals(datum.getCode())? SmsStatus.SEND_SUCCESS.getCode():SmsStatus.SEND_FAIL.getCode())
                    .reportContent(datum.getMsg())
                    .created(Math.toIntExact(DateUtil.currentSeconds()))
                    .updated(Math.toIntExact(DateUtil.currentSeconds()))
                    .build();
            smsRecordList.add(smsRecord);
        });
        return smsRecordList;
    }
}
