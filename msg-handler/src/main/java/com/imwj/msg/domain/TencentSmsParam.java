package com.imwj.msg.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 腾讯短信参数
 *
 * 参数示例：
 * @author langao_q
 * @since 2021-12-30 16:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TencentSmsParam {

    /**
     * api相关
     */
    private String url;
    private String region ;

    /**
     * 账号相关
     */
    private String secretId;
    private String secretKey;
    private String smsSdkAppId;
    private String templateId;
    private String signName;

    /**
     * 标识渠道商Id
     */
    private Integer supplierId;

    /**
     * 标识渠道商名字
     */
    private String supplierName;

}
