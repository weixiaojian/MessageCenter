package com.imwj.msg.enums;

/**
 * 调度类型
 * @author wj
 * @create 2022-05-07 17:41
 */
public enum ScheduleTypeEnum {

    NONE,
    /**
     * schedule by cron
     */
    CRON,

    /**
     * schedule by fixed rate (in seconds)
     */
    FIX_RATE;

    ScheduleTypeEnum() {
    }

}
