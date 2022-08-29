package com.imwj.msg.common.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小程序订阅消息账号参数
 * @author wj
 * @create 2022-08-29 14:29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeChatMiniProgramAccount {

    /**
     * appId
     */
    private String appId;
    /**
     * appSecret
     */
    private String appSecret;
}
