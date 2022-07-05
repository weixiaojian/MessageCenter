package com.imwj.msg.handler.service.deduplication.build;

import cn.hutool.core.date.DateUtil;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.enums.AnchorState;
import com.imwj.msg.common.enums.DeduplicationType;
import com.imwj.msg.handler.domain.DeduplicationParam;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author langao_q
 * @since 2022-01-26 16:40
 */
@Service
public class FrequencyDeduplicationBuilder  extends AbstractDeduplicationBuilder implements Builder{

    public FrequencyDeduplicationBuilder() {
        deduplicationType = DeduplicationType.FREQUENCY.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        if (deduplicationParam == null) {
            return null;
        }
        deduplicationParam.setDeduplicationTime((DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000);
        deduplicationParam.setAnchorState(AnchorState.RULE_DEDUPLICATION);
        return deduplicationParam;
    }
}
