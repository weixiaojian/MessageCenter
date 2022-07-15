package com.imwj.msg.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 公众号内容model
 * @author langao_q
 * @since 2021-12-29 19:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfficialAccountsContentModel extends ContentModel {
    /**
     * 模板消息发送的数据
     */
    String content;

    /**
     * 模板消息跳转的url
     */
    String url;
}
