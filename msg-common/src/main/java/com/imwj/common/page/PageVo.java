package com.imwj.common.page;

import lombok.Data;

/**
 * 分页查询实体
 * @author wj
 * @create 2022-05-07 11:33
 */
@Data
public class PageVo {

    /**
     * 起始页
     */
    protected Integer pageNum = 1;

    /**
     * 每页显示条数
     */
    protected Integer pageSize = 10;

    /**
     * 排序列
     */
    protected String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    protected String isAsc = "asc";

    /**
     * 模板ID
     */
    private Long id;

    /**
     * 消息接收者(测试发送时使用)
     */
    private String receiver;

    /**
     * 下发参数信息
     */
    private String msgContent;
}
