package com.imwj.msg.handler.receiver;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.imwj.msg.common.constant.SendAccountConstant;
import com.imwj.msg.common.dto.account.TencentSmsAccount;
import com.imwj.msg.common.enums.SmsStatus;
import com.imwj.msg.support.config.SupportThreadPoolConfig;
import com.imwj.msg.support.dao.SmsRecordDao;
import com.imwj.msg.support.domain.SmsRecord;
import com.imwj.msg.support.utils.AccountUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatus;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatusRequest;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 拉取腾讯云短信回执信息
 * @author wj
 * @create 2022-08-30 17:09
 */
@Slf4j
@Component
public class TencentSmsReceipt {

    @Autowired
    private AccountUtils accountUtils;

    @Autowired
    private SmsRecordDao smsRecordDao;

    @PostConstruct
    private void init(){
        // 获取腾讯云账号信息
        TencentSmsAccount account = accountUtils.getAccount(66, SendAccountConstant.SMS_ACCOUNT_KEY, SendAccountConstant.SMS_PREFIX,
                TencentSmsAccount.class);
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            while (true){
                try {
                    SmsClient client = getSmsClient(account);
                    // 每次拉取十条
                    PullSmsSendStatusRequest req = new PullSmsSendStatusRequest();
                    req.setLimit(10L);
                    req.setSmsSdkAppId(account.getSmsSdkAppId());

                    PullSmsSendStatusResponse resp = client.PullSmsSendStatus(req);

                    List<SmsRecord> smsRecordList = new ArrayList<>();
                    if(resp != null && resp.getPullSmsSendStatusSet() != null && resp.getPullSmsSendStatusSet().length > 0){
                        log.debug("receipt sms:{}", JSON.toJSONString(resp.getPullSmsSendStatusSet()));
                        for(PullSmsSendStatus pullSmsSendStatus : resp.getPullSmsSendStatusSet()){
                            SmsRecord smsRecord = SmsRecord.builder()
                                    .sendDate(Integer.valueOf(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)))
                                    .messageTemplateId(0L)
                                    .phone(Long.valueOf(pullSmsSendStatus.getPhoneNumber()))
                                    .supplierId(account.getSupplierId())
                                    .supplierName(account.getSupplierName())
                                    .msgContent("")
                                    .seriesId(pullSmsSendStatus.getSerialNo())
                                    .chargingNum(0)
                                    .status("SUCCESS".equals(pullSmsSendStatus.getReportStatus()) ? SmsStatus.RECEIVE_SUCCESS.getCode() : SmsStatus.RECEIVE_FAIL.getCode())
                                    .reportContent(pullSmsSendStatus.getDescription())
                                    .updated(Math.toIntExact(pullSmsSendStatus.getUserReceiveTime()))
                                    .created(Math.toIntExact(DateUtil.currentSeconds()))
                                    .build();
                            smsRecordList.add(smsRecord);
                        }
                    }
                    smsRecordList.forEach(d -> {
                        smsRecordDao.insert(d);
                    });
                    Thread.sleep(200);
                }catch (Exception e){
                    log.error("TencentSmsReceipt#init fail!{}", Throwables.getStackTraceAsString(e));
                }
            }
        });
    }

    /**
     * 构造Ciient
     * @param account
     * @return
     */
    private SmsClient getSmsClient(TencentSmsAccount account) {
        Credential cred = new Credential(account.getSecretId(), account.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(account.getUrl());
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(cred, account.getRegion(), clientProfile);
    }
}
