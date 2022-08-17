package com.imwj.msg.common.constant;

/**
 * 系统常量
 * @author langao_q
 * @since 2021-12-29 19:04
 */
public class MessageCenterConstant {
    /**
     * boolean转换
     */
    public final static Integer TRUE = 1;
    public final static Integer FALSE = 0;


    /**
     * cron时间格式
     */
    public final static String CRON_FORMAT = "ss mm HH dd MM ? yyyy-yyyy";

    /**
     * apollo默认的值
     */
    public final static String APOLLO_DEFAULT_VALUE_JSON_OBJECT = "{}";
    public final static String APOLLO_DEFAULT_VALUE_JSON_ARRAY = "[]";

    /**
     * businessId默认的长度
     * 生成的逻辑：TaskInfoUtils#generateBusinessId(java.lang.Long, java.lang.Integer)
     */
    public final static Integer BUSINESS_ID_LENGTH = 16;

    /**
     * 消息发送给全部人的标识
     * (企业微信 应用消息)
     * (钉钉自定义机器人)
     *
     */
    public static final String SEND_ALL = "@all";


    /**
     * 加密算法
     */
    public static final String HMAC_SHA256_ENCRYPTION_ALGO = "HmacSHA256";

    /**
     * 编码格式
     */
    public static final String CHARSET_NAME = "UTF-8";

    /**
     * HTTP 请求方法
     */
    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";
}
