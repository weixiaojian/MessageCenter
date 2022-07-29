package com.imwj.msg.handler.domain.dingding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 钉钉自定义机器人 账号信息
 * @author wj
 * @create 2022-07-29 11:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DingDingRobotAccount {

    /**
     * 密钥
     */
    private String secret;

    /**
     * 自定义群机器人中的 webhook
     */
    private String webhook;


}
