package com.imwj.msg.email;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;

/**
 * @author langao_q
 * @since 2022-01-11 11:23
 */
public class Main {

    public static void main(String[] args) throws GeneralSecurityException {
        //初始化邮箱配置
        MailAccount account = new MailAccount();
        account.setHost("smtp.qq.com").setPort(465);
        account.setUser("寄件人邮箱").setPass("寄件人邮箱密码").setAuth(true);
        account.setFrom("寄件人邮箱");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        account.setStarttlsEnable(true).setSslEnable(true).setCustomProperty("mail.smtp.ssl.socketFactory", sf);
        account.setTimeout(25000).setConnectionTimeout(25000);
        //发送邮件
        String send = MailUtil.send(account, "收件人邮箱", "测试邮件title", "测试邮件内容", true, null);
        System.out.println(send);
    }

}
