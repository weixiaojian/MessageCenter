package com.imwj.msg.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.dto.model.SmsContentModel;
import com.imwj.msg.common.enums.ChannelType;
import com.imwj.msg.handler.domain.sms.SmsParam;
import com.imwj.msg.handler.handler.BaseHandler;
import com.imwj.msg.handler.handler.Handler;
import com.imwj.msg.handler.script.SmsScript;
import com.imwj.msg.support.dao.SmsRecordDao;
import com.imwj.msg.support.domain.SmsRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * 短信处理Handler
 * @author langao_q
 * @since 2021-12-30 15:48
 */
@Slf4j
@Component
public class SmsHandler extends BaseHandler implements Handler {

    /**
     * 初始化渠道和handler关系
     */
    public SmsHandler() {
        channelCode = ChannelType.SMS.getCode();
    }

    @Resource
    private SmsRecordDao smsRecordDao;

    @Resource
    private SmsScript tencentSmsScript;

    @Override
    public boolean handler(TaskInfo taskInfo) {
        //1.调用腾讯接口发送短信
        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(getSmsContent(taskInfo))
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .sendAccount(taskInfo.getSendAccount())
                .build();

        try {
            List<SmsRecord> recordList = tencentSmsScript.send(smsParam);
            if(CollUtil.isNotEmpty(recordList)){
                for(SmsRecord smsRecord : recordList){
                    smsRecordDao.insert(smsRecord);
                }
            }
            return true;
        }catch (Exception e){
            log.error("SmsHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
        }
        return false;
    }

    /**
     * 短信内容参数拼接
     * @param taskInfo
     * @return
     */
    private String getSmsContent(TaskInfo taskInfo) {
        SmsContentModel smsContentModel = (SmsContentModel) taskInfo.getContentModel();
        if (StrUtil.isNotBlank(smsContentModel.getUrl())) {
            return smsContentModel.getContent() + " " + smsContentModel.getUrl();
        } else {
            return smsContentModel.getContent();
        }
    }
}
