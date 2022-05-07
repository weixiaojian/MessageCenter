package com.imwj.msg.dto;

import lombok.*;

/**
 * @author langao_q
 * @since 2021-12-29 19:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
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
