package com.imwj.msg.handler.service.deduplication.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.enums.DeduplicationType;
import com.imwj.msg.handler.limit.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 内容去重（默认5分钟相同的文案发给相同的用户去重）
 * @author langao_q
 * @since 2021-12-31 15:17
 */
@Service
public class ContentDeduplicationService extends AbstractDeduplicationService{

    @Autowired
    public ContentDeduplicationService(@Qualifier("SlideWindowLimitService") LimitService limitService) {
        this.limitService = limitService;
        deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    /**
     * 内容去重构建key：md5Hex（模板id + 手机号 + 内容）
     * @param taskInfo
     * @param receiver
     * @return
     */
    @Override
    public String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return DigestUtil.md5Hex(taskInfo.getMessageTemplateId() + receiver
                + JSON.toJSONString(taskInfo.getContentModel()));
    }
}
