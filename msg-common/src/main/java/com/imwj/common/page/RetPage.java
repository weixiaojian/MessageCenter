package com.imwj.common.page;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回实体
 * @author langao_q
 * @since 2021-02-04 16:13
 */
@Data
public class RetPage<T> implements Serializable {

    /**
     * 起始页
     */
    private Long pageNum;
    /**
     * 每页显示条数
     */
    private Long pageSize;
    /**
     * 总条数
     */
    private Long total;
    /**
     * 总页数
     */
    private Long pages;
    /**
     * 分页数据
     */
    private List<T> data;

    /**
     * 返回分页
     * @param pageNum 起始页
     * @param pageSize 每页显示条数
     * @param total 总条数
     * @param pages 总页数
     * @param data 分页数据
     */
    public RetPage(long pageNum, long pageSize, long total, long pages, List data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.pages = pages;
        this.data = data;
    }
}
