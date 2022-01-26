package com.imwj.msg.service.deduplication.service;

import com.imwj.msg.domain.DeduplicationParam;

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
