package com.imwj.msg.cron.enums;

/**
 * 调度过期策略
 * @author wj
 * @create 2022-05-07 17:41
 */
public enum MisfireStrategyEnum {

    /**
     * do nothing
     */
    DO_NOTHING,

    /**
     * fire once now
     */
    FIRE_ONCE_NOW;

    MisfireStrategyEnum() {
    }
}
