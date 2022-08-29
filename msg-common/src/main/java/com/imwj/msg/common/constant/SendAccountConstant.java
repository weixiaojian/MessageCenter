package com.imwj.msg.common.constant;

/**
 * 发送账号的常量信息汇总
 * 约定：所有的账号都从66开始，步长为10
 * @author wj
 * @create 2022-08-17 11:44
 */
public class SendAccountConstant {

    /**
     * 钉钉 工作应用消息 账号
     */
    public static final String DING_DING_WORK_NOTICE_ACCOUNT_KEY = "dingDingWorkNoticeAccount";
    public static final String DING_DING_WORK_NOTICE_PREFIX = "ding_ding_work_notice_";
    public static final String DING_DING_ACCESS_TOKEN_PREFIX = "ding_ding_access_token_";


    /**
     * 邮件 账号
     */
    public static final String EMAIL_ACCOUNT_KEY = "emailAccount";
    public static final String EMAIL_ACCOUNT_PREFIX = "email_";


    /**
     * 钉钉群自定义机器人 账号
     */
    public static final String DING_DING_ROBOT_ACCOUNT_KEY = "dingDingRobotAccount";
    public static final String DING_DING_ROBOT_PREFIX = "ding_ding_robot_";

    /**
     * 企业微信 应用消息 账号
     */
    public static final String ENTERPRISE_WECHAT_ACCOUNT_KEY = "enterpriseWechatAccount";
    public static final String ENTERPRISE_WECHAT_PREFIX = "enterprise_wechat_";


    /**
     * 短信 账号
     */
    public static final String SMS_ACCOUNT_KEY = "smsAccount";
    public static final String SMS_PREFIX = "sms_";

    /**
     * 微信服务号 应用消息 账号
     */
    public static final String WECHAT_OFFICIAL_ACCOUNT_KEY = "officialAccount";
    public static final String WECHAT_OFFICIAL_PREFIX = "official_";

    /**
     * 微信小程序 应用消息 账号
     */
    public static final String WECHAT_MINI_PROGRAM_ACCOUNT_KEY = "miniProgramAccount";
    public static final String WECHAT_MINI_PROGRAM_PREFIX = "mini_program_";

    /**
     * 账号约定：所有的账号都从10开始，步长为10
     */
    public static final Integer START = 66;
    public static final Integer STEP = 10;
}
