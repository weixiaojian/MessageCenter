package com.imwj.msg.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 打点信息枚举
 * @author langao_q
 * @since 2021-12-31 11:39
 */
@Getter
@ToString
@AllArgsConstructor
public enum AnchorState {

    //成功消费Kafka
    RECEIVE(10, "成功消费Kafka"),
    //消费被丢弃
    DISCARD(20, "消费被丢弃"),
    //消息被内容去重
    CONTENT_DEDUPLICATION(30, "消息被内容去重"),
    //消息被频次去重
    RULE_DEDUPLICATION(40, "消息被频次去重"),
    //白名单过滤
    WHITE_LIST(50, "白名单过滤"),
    //消息下发成功
    SEND_SUCCESS(60, "消息下发成功"),
    //消息下发失败
    SEND_FAIL(70, "消息下发失败"),
    //消息被点击
    CLICK(0100, "消息被点击"),
    ;

    private Integer code;
    private String description;

    /**
     * 通过code获取描述
     *
     * @param code
     * @return
     */
    public static String getDescriptionByCode(Integer code) {
        for (AnchorState anchorState : AnchorState.values()) {
            if (anchorState.getCode().equals(code)) {
                return anchorState.getDescription();
            }
        }
        return "未知点位";
    }

}
