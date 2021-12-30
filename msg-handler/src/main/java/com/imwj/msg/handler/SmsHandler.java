package com.imwj.msg.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.imwj.msg.dao.SmsRecordDao;
import com.imwj.msg.domain.SmsParam;
import com.imwj.msg.domain.SmsRecord;
import com.imwj.msg.domain.TaskInfo;
import com.imwj.msg.dto.SmsContentModel;
import com.imwj.msg.enums.ChannelType;
import com.imwj.msg.script.SmsScript;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author langao_q
 * @since 2021-12-30 15:48
 */
@Slf4j
@Component
public class SmsHandler extends Handler {

    public SmsHandler() {
        channelCode = ChannelType.SMS.getCode();
    }

    @Autowired
    private SmsRecordDao smsRecordDao;

    @Autowired
    private SmsScript tencentSmsScript;

    @Override
    public void handler(TaskInfo taskInfo) {
        //1.调用腾讯接口发送短信
        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(getSmsContent(taskInfo))
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .supplierId(10)
                .supplierName("腾讯云通知类消息渠道")
                .build();

        List<SmsRecord> recordList = tencentSmsScript.send(smsParam);

        //2.记录短信发送日志
        if (!CollUtil.isEmpty(recordList)) {
            for(SmsRecord smsRecord : recordList){
                smsRecordDao.insert(smsRecord);
            }
        }
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
