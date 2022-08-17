package com.imwj.msg.handler.service.deduplication.service;


import com.imwj.msg.handler.deduplication.DeduplicationParam;

/**
 * @author langao_q
 * @since 2022-01-26 16:43
 */
public interface DeduplicationService {

    /**
     * 去重
     * @param param
     */
    void deduplication(DeduplicationParam param);
}
