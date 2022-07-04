package com.imwj.msg.constant;

/**
 * xxl-job常量信息
 * @author wj
 * @create 2022-05-07 17:43
 */
public class XxlJobConstant {
    /**
     * 接口路径
     */
    public static final String LOGIN_URL = "/login";
    public static final String INSERT_URL = "/jobinfo/add";
    public static final String UPDATE_URL = "/jobinfo/update";
    public static final String DELETE_URL = "/jobinfo/remove";
    public static final String RUN_URL = "/jobinfo/start";
    public static final String STOP_URL = "/jobinfo/stop";

    /**
     * 执行器组接口路径
     */
    public static final String JOB_GROUP_PAGE_LIST = "/jobgroup/pageList";
    public static final String JOB_GROUP_INSERT_URL = "/jobgroup/save";

    /**
     * 执行器名称
     */
    public static final String JOB_HANDLER_NAME = "msgJobHandler";

    /**
     * 超时时间
     */
    public static final Integer TIME_OUT = 120;

    /**
     * 失败重试次数
     */
    public static final Integer RETRY_COUNT = 2;

    /**
     * 立即执行的任务 延迟时间(秒数)
     */
    public static final Integer DELAY_TIME = 5;
}
