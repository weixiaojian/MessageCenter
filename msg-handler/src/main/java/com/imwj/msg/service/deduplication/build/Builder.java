package com.imwj.msg.service.deduplication.build;

import com.imwj.msg.domain.DeduplicationParam;
import com.imwj.msg.domain.TaskInfo;

/**
 * @author langao_q
 * @since 2022-01-26 16:39
 */
public interface Builder {

    String DEDUPLICATION_CONFIG_PRE = "deduplication_";

    /**
     * 根据配置构建去重参数
     *
     * @param deduplication
     * @param taskInfo
     * @return
     */
    DeduplicationParam build(String deduplication, TaskInfo taskInfo);

}
