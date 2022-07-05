package com.imwj.msg.cron.enums;

/**
 * 调度类型
 * @author wj
 * @create 2022-05-07 17:41
 */
public enum ScheduleTypeEnum {

    /**
     * 任务类型
     */
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
