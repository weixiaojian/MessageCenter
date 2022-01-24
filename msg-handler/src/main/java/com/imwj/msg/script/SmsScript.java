package com.imwj.msg.script;

import com.imwj.msg.domain.SmsParam;
import com.imwj.msg.domain.SmsRecord;

import java.util.List;

/**
 * 短信脚本 接口
 * @author langao_q
 * @since 2021-12-30 16:00
 */
public interface SmsScript {

    /**
     * 发送短信
     * @param smsParam
     * @return
     * @throws Exception
     */
    List<SmsRecord> send(SmsParam smsParam) throws Exception;

}
