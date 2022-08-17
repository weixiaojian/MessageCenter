package com.imwj.msg.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 钉钉 自定义机器人
 * https://open.dingtalk.com/document/group/custom-robot-access
 * @author wj
 * @create 2022-07-29 11:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DingDingContentModel extends ContentModel {

    /**
     * 下发钉钉消息的类型 参考：SendMessageType.java
     */
    private String sendType;
    /**
     * 文本消息 - 文案
     */
    private String content;
    /**
     * 图片、文件、语音消息 需要发送使用的素材ID字段
     */
    private String mediaId;

}
