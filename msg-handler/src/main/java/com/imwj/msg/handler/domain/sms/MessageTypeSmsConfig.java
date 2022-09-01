package com.imwj.msg.handler.domain.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对于每种消息类型的 短信配置
 * @author wj
 * @create 2022-08-31 16:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTypeSmsConfig {

    /**
     * 权重(决定着流量的占比)
     */
    private Integer weights;

    /**
     * script名称
     */
    private String scriptName;
}
