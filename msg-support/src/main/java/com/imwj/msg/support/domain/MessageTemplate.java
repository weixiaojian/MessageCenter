package com.imwj.msg.support.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 消息模板实体类
 * @author langao_q
 * @since 2021-12-29 16:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("message_template")
@Accessors(chain = true)
public class MessageTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String name;

    /**
     * 当前消息审核状态： 10.待审核 20.审核成功 30.被拒绝
     */
    private Integer auditStatus;
    /**
     * 定时任务Id(由xxl-job返回)
     */
    private Integer cronTaskId;
    /**
     * 定时发送的人群ID
     * 1. 目前阶段直接填入Id测试
     * 2. 正常是需要通过ID获取文件遍历每个Id
     */
    private String cronCrowdPath;
    /**
     * 工单ID
     */
    private String flowId;

    /**
     * 当前消息状态：10.新建 20.停用 30.启用 40.等待发送 50.发送中 60.发送成功 70.发送失败
     */
    private Integer msgStatus;

    /**
     * 消息的发送ID类型：10. userId 20.did 30.手机号 40.openId 50.email
     */
    private Integer idType;

    /**
     * 消息发送渠道：10.IM 20.Push 30.短信 40.Email 50.公众号 60.小程序
     */
    private Integer sendChannel;

    /**
     * 10.运营类 20.技术类接口调用
     */
    private Integer templateType;

    /**
     * 10.通知类消息 20.营销类消息 30.验证码类消息
     */
    private Integer msgType;
    /**
     * 屏蔽类型 10.夜间不屏蔽 20.夜间屏蔽 30.夜间屏蔽(次日早上9点发送)
     */
    private Integer shieldType;
    /**
     * 期望发送时间：立即发送.10 定时任务以及周期任务.cron表达式
     */
    private String expectPushTime;

    /**
     * 消息内容 占位符用{$var}表示
     */
    private String msgContent;

    /**
     * 发送账号 一个渠道下可存在多个账号
     */
    private Integer sendAccount;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 更新者
     */
    private String updator;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 业务方团队
     */
    private String team;

    /**
     * 业务方
     */
    private String proposer;

    /**
     * 是否删除:
     * 0：未删除
     * 1：已删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Integer created;

    /**
     * 更新时间
     */
    private Integer updated;

}
