package com.imwj.msg.handler.service.deduplication.build;


import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.enums.AnchorState;
import com.imwj.msg.common.enums.DeduplicationType;
import com.imwj.msg.handler.deduplication.DeduplicationParam;
import org.springframework.stereotype.Service;

/**
 * 内容去重
 * @author langao_q
 * @since 2022-01-26 16:40
 */
@Service
public class ContentDeduplicationBuilder  extends AbstractDeduplicationBuilder implements Builder{

    public ContentDeduplicationBuilder() {
        deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        if (deduplicationParam == null) {
            return null;
        }
        deduplicationParam.setAnchorState(AnchorState.CONTENT_DEDUPLICATION);
        return deduplicationParam;

    }
}
