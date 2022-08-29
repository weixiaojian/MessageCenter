package com.imwj.msg.common.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公众号模板消息参数
 * https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Template_Message_Interface.html
 * @author wj
 * @create 2022-07-15 10:57
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WechatOfficialAccount {

    /**
     * appId
     */
    private String appId;
    /**
     * secret
     */
    private String secret;
    /**
     * token
     */
    private String token;
    /**
     * aesKey
     */
    private String aesKey;
}
