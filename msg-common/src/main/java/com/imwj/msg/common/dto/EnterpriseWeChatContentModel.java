package com.imwj.msg.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 企业微信
 *  https://developer.work.weixin.qq.com/document/path/90372#%E6%96%87%E6%9C%AC%E6%B6%88%E6%81%AF
 * @author wj
 * @create 2022-07-25 10:35
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseWeChatContentModel extends ContentModel {

    /**
     * 下发企业微信消息的类型
     */
    private String messageType;

    /**
     * 文本消息 - 文案
     */
    private String content;

    /**
     * 图片媒体文件id
     */
    private String mediaId;

}
