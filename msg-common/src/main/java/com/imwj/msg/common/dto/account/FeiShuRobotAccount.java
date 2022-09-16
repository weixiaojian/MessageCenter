package com.imwj.msg.common.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 飞书 机器人 账号信息
 * @author wj
 * @create 2022-09-16 16:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeiShuRobotAccount {
    /**
     * 自定义群机器人中的 webhook
     */
    private String webhook;

}

