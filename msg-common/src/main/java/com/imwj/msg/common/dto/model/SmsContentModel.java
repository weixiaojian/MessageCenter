package com.imwj.msg.common.dto.model;

import lombok.*;


/**
 * @author langao_q
 * @since 2021-12-29 19:05
 * <p>
 * 短信内容模型
 *
 * 在前端填写的时候分开，但最后处理的时候会将url拼接在content上
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SmsContentModel extends ContentModel {

    /**
     * 短信发送内容
     */
    private String content;

    /**
     * 短信发送链接
     */
    private String url;

}
