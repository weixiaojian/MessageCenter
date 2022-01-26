package com.imwj.msg.service.deduplication.build;

import com.imwj.msg.domain.DeduplicationParam;
import com.imwj.msg.domain.TaskInfo;
import com.imwj.msg.enums.AnchorState;
import com.imwj.msg.enums.DeduplicationType;
import org.springframework.stereotype.Service;

/**
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
        if (deduplication == null) {
            return null;
        }
        deduplicationParam.setAnchorState(AnchorState.CONTENT_DEDUPLICATION);
        return deduplicationParam;

    }
}
