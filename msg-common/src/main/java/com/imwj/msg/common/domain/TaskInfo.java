package com.imwj.msg.common.domain;

import com.imwj.msg.common.dto.model.ContentModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 发送任务信息
 * @author langao_q
 * @since 2021-12-28 11:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskInfo {

    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 业务Id(数据追踪使用)
     * 生成逻辑参考
     */
    private Long businessId;

    /**
     * 接收者
     */
    private Set<String> receiver;

    /**
     * 发送的Id类型
     */
    private Integer idType;

    /**
     * 发送渠道
     */
    private Integer sendChannel;

    /**
     * 模板类型
     */
    private Integer templateType;

    /**
     * 消息类型
     */
    private Integer msgType;
    /**
     * 屏蔽类型 10.夜间不屏蔽 20.夜间屏蔽 30.夜间屏蔽(次日早上9点发送)
     */
    private Integer shieldType;
    /**
     * 发送文案模型
     * message_template表存储的content是JSON(所有内容都会塞进去)
     * 不同的渠道要发送的内容不一样(比如发push会有img，而短信没有)
     * 所以会有ContentModel
     */
    private ContentModel contentModel;

    /**
     * 发送账号（邮件下可有多个发送账号、短信可有多个发送账号..）
     */
    private Integer sendAccount;

}
