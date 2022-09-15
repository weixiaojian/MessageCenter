package com.imwj.msg.cron.constant;

/**
 * 缓冲等待 常量
 * @author wj
 * @create 2022-07-04 16:00
 */
public class PendingConstant {

    /**
     * 阻塞队列大小
     */
    public static final Integer QUEUE_SIZE = 100;

    /**
     * 触发执行的数量阈值
     */
    public static final Integer NUM_THRESHOLD = 100;

    /**
     * batch 触发执行的时间阈值，单位毫秒【必填】
     */
    public static final Long TIME_THRESHOLD = 1000L;

}