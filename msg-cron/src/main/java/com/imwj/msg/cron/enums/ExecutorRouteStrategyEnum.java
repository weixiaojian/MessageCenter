package com.imwj.msg.cron.enums;


/**
 * xxl-job路由策略
 * @author wj
 * @create 2022-05-07 17:41
 */
public enum ExecutorRouteStrategyEnum {

    /**
     * 类型
     */
    FIRST,
    LAST,
    ROUND,
    RANDOM,
    CONSISTENT_HASH,
    LEAST_FREQUENTLY_USED,
    LEAST_RECENTLY_USED,
    FAILOVER,
    BUSYOVER,
    SHARDING_BROADCAST;

    ExecutorRouteStrategyEnum() {
    }
}
