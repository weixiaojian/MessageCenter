package com.imwj.msg.handler.service.deduplication.service;


import cn.hutool.core.util.StrUtil;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.enums.DeduplicationType;
import com.imwj.msg.handler.limit.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 频次去重
 * @author langao_q
 * @since 2021-12-31 15:18
 */
@Service
public class FrequencyDeduplicationService extends AbstractDeduplicationService{

    @Autowired
    public FrequencyDeduplicationService(@Qualifier("SimpleLimitService") LimitService limitService) {
        this.limitService = limitService;
        deduplicationType = DeduplicationType.FREQUENCY.getCode();
    }

    private static final String PREFIX = "FRE";

    /**
     * 频次去重构建key：FRE + _ + 手机号 + _ + 模板id + _ + 发送渠道
     * @param taskInfo
     * @param receiver
     * @return
     */
    @Override
    public String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return PREFIX + StrUtil.C_UNDERLINE
                + receiver  + StrUtil.C_UNDERLINE
                + taskInfo.getMessageTemplateId() + StrUtil.C_UNDERLINE
                + taskInfo.getSendChannel();
    }
}
