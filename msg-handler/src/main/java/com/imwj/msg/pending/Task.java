package com.imwj.msg.pending;

import cn.hutool.core.collection.CollUtil;
import com.imwj.msg.domain.TaskInfo;
import com.imwj.msg.handler.HandlerHolder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Task 执行器
 * 0.丢弃消息
 * 1.通用去重功能
 * 2.发送消息
 * @author langao_q
 * @since 2021-12-30 10:52
 */
@Data
@Slf4j
@Accessors(chain = true)
public class Task implements Runnable {

    @Autowired
    private HandlerHolder handlerHolder;

    /*@Autowired
    private DeduplicationRuleService deduplicationRuleService;
    @Autowired
    private DiscardMessageService discardMessageService;*/

    private TaskInfo taskInfo;

    @Override
    public void run() {

        // 2. 真正发送消息
        if (CollUtil.isNotEmpty(taskInfo.getReceiver())) {
            handlerHolder.route(taskInfo.getSendChannel())
                    .doHandler(taskInfo);
        }
    }
}
