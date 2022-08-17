package com.imwj.msg.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 微信下发消息类型枚举
 * 微信应用消息/钉钉/服务号均有多种的消息类型下发
 * @author wj
 * @create 2022-07-25 15:41
 */
@Getter
@ToString
@AllArgsConstructor
public enum SendMessageType {

    /**
     * 企业微信消息类型
     */
    TEXT(10, "文本"),
    VOICE(20, "语音"),
    VIDEO(30, "视频"),
    NEWS(40, "图文"),
    TEXT_CARD(50, "文本卡片"),
    FILE(60, "文件"),
    MINI_PROGRAM_NOTICE(70, "小程序通知"),
    MARKDOWN(80, "markdown"),
    TEMPLATE_CARD(90, "模板卡片"),
    IMAGE(100, "图片"),
    ;

    private Integer code;
    private String description;

}

