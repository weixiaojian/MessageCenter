package com.imwj.msg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author langao_q
 * @since 2021-12-29 19:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailContentModel extends ContentModel {
    /**
     * 标题
     */
    private String title;

    /**
     * 内容(可写入HTML)
     */
    private String content;

}
