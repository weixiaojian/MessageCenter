package com.imwj.msg.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 消息屏蔽枚举
 * @author wj
 * @create 2022-07-26 15:18
 */
@Getter
@ToString
@AllArgsConstructor
public enum  ShieldType {

    /**
     * 夜间不屏蔽
     */
    NIGHT_NO_SHIELD(10, "夜间不屏蔽"),
    /**
     * 夜间屏蔽
     */
    NIGHT_SHIELD(20, "夜间屏蔽"),
    /**
     * 夜间屏蔽(次日早上9点发送
     */
    NIGHT_SHIELD_BUT_NEXT_DAY_SEND(30, "夜间屏蔽(次日早上9点发送)");

    private Integer code;
    private String description;

}
