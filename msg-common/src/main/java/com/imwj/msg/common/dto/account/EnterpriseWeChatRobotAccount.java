package com.imwj.msg.common.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 企业微信机器人账号信息
 * @author wj
 * @create 2022-09-15 17:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseWeChatRobotAccount {

    /**
     * 自定义群机器人中的 webhook
     */
    private String webhook;
}
