package com.imwj.msg.common.dto.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 个推推送消息model
 * @author langao_q
 * @since 2021-12-29 19:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PushContentModel extends ContentModel {

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 跳转url
     */
    private String url;

}
