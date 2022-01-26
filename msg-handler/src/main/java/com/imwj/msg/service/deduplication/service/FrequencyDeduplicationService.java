package com.imwj.msg.service.deduplication.service;

import cn.hutool.core.util.StrUtil;
import com.imwj.msg.domain.TaskInfo;
import org.springframework.stereotype.Service;

/**
 * 频次去重
 * @author langao_q
 * @since 2021-12-31 15:18
 */
@Service
public class FrequencyDeduplicationService extends AbstractDeduplicationService{

    private static final String PREFIX = "FRE";

    /**
     * 频次去重构建key：FRE + _ + 手机号 + _ + 模板id + _ + 发送渠道
     * @param taskInfo
     * @param receiver
     * @return
     */
    @Override
    protected String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return PREFIX + StrUtil.C_UNDERLINE
                + receiver  + StrUtil.C_UNDERLINE
                + taskInfo.getMessageTemplateId() + StrUtil.C_UNDERLINE
                + taskInfo.getSendChannel();
    }
}
