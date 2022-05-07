package com.imwj.msg.handler;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.imwj.msg.domain.TaskInfo;
import com.imwj.msg.dto.EmailContentModel;
import com.imwj.msg.enums.ChannelType;
import com.imwj.msg.util.AccountUtils;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author langao_q
 * @since 2022-01-11 10:32
 */
@Slf4j
@Component
public class EmailHandler extends BaseHandler implements Handler {

    private static final String SMS_ACCOUNT_KEY = "emailAccount";
    private static final String PREFIX = "email_";

    @Autowired
    private AccountUtils accountUtils;

    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount account = getAccount(taskInfo.getSendAccount());
        try {
            log.info("发送邮件开始：{}", JSON.toJSONString(taskInfo));
            String result = MailUtil.send(account, taskInfo.getReceiver(), emailContentModel.getTitle(),
                    emailContentModel.getContent(), true, null);
            log.info("发送邮件结束：{}", JSON.toJSONString(result));

        } catch (Exception e) {
            log.error("EmailHandler#handler fail!{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
            return false;
        }
        return true;
    }

    /**
     * 获取账号信息和配置
     * @return
     */
    private MailAccount getAccount(Integer sendAccount) {
        //获取apollo中的qq邮箱账号配置
        MailAccount account = accountUtils.getAccount(sendAccount, SMS_ACCOUNT_KEY, PREFIX, new MailAccount());
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            account.setAuth(true).setStarttlsEnable(true).setSslEnable(true).setCustomProperty("mail.smtp.ssl.socketFactory", sf);

            account.setTimeout(25000).setConnectionTimeout(25000);
        } catch (Exception e) {
            log.error("EmailHandler#getAccount fail!{}", Throwables.getStackTraceAsString(e));
        }
        return account;
    }
}
