package com.imwj.msg.common.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 钉钉工作消息 账号信息
 * AppKey和AppSecret以及agentId都可在钉钉开发者后台的应用详情页面获取。
 * https://open-dev.dingtalk.com/?spm=ding_open_doc.document.0.0.13b6722fd9ojfy
 * @author wj
 * @create 2022-08-17 11:40
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DingDingWorkNoticeAccount {

    /**
     * 应用的唯一标识key。
     */
    private String appKey;

    /**
     * 应用的密钥
     */
    private String appSecret;

    /**
     * 发送消息时使用的微应用的AgentID
     */
    private String agentId;

}