package com.imwj.msg.service.deduplication;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.domain.TaskInfo;
import org.springframework.stereotype.Service;

/**
 * 内容去重（默认5分钟相同的文案发给相同的用户去重）
 * @author langao_q
 * @since 2021-12-31 15:17
 */
@Service
public class ContentDeduplicationService extends AbstractDeduplicationService{

    /**
     * 内容去重构建key：md5Hex（模板id + 手机号 + 内容）
     * @param taskInfo
     * @param receiver
     * @return
     */
    @Override
    protected String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return DigestUtil.md5Hex(taskInfo.getMessageTemplateId() + receiver
                + JSON.toJSONString(taskInfo.getContentModel()));
    }
}
