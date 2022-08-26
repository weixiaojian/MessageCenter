package com.imwj.msg.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小程序消息model
 * @author langao_q
 * @since 2021-12-29 19:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MiniProgramContentModel extends ContentModel {

    /**
     * 点击模板卡片后的跳转页面
     */
    private String page;
    /**
     * 内容
     */
    private String content;

}
