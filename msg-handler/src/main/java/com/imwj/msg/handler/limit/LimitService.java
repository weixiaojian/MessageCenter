package com.imwj.msg.handler.limit;

import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.handler.deduplication.DeduplicationParam;
import com.imwj.msg.handler.service.deduplication.service.AbstractDeduplicationService;

import java.util.Set;

/**
 * @author wj
 * @create 2022-08-22 11:05
 */
public interface LimitService {

    /**
     * @param service 去重器对象
     * @param taskInfo
     * @param param 去重参数
     * @return 返回不符合条件的手机号码
     */
    Set<String> limitFilter(AbstractDeduplicationService service, TaskInfo taskInfo, DeduplicationParam param);

}
