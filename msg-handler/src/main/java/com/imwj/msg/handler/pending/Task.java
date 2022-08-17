package com.imwj.msg.handler.pending;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.handler.handler.HandlerHolder;
import com.imwj.msg.handler.service.deduplication.DeduplicationRuleService;
import com.imwj.msg.handler.service.discard.DiscardMessageService;
import com.imwj.msg.handler.shield.ShieldService;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Task 执行器
 * 0.丢弃消息
 * 1.1消息夜间屏蔽
 * 1.2通用去重功能
 * 2.发送消息
 * @author langao_q
 * @since 2021-12-30 10:52
 */
@Data
@Slf4j
@Accessors(chain = true)
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Task implements Runnable {

    @Autowired
    private HandlerHolder handlerHolder;

    @Autowired
    private DeduplicationRuleService deduplicationRuleService;
    @Autowired
    private DiscardMessageService discardMessageService;
    @Autowired
    private ShieldService shieldService;

    private TaskInfo taskInfo;

    @Override
    public void run() {
        // 0.丢弃消息
        if (discardMessageService.isDiscard(taskInfo)) {
            log.info("消息丢弃 {}", JSON.toJSONString(taskInfo));
            return;
        }
        // 1.1消息夜间屏蔽
        shieldService.shield(taskInfo);

        // 1.2平台通用去重
        if(CollUtil.isNotEmpty(taskInfo.getReceiver())){
            deduplicationRuleService.duplication(taskInfo);
        }

        // 2. 真正发送消息
        if (CollUtil.isNotEmpty(taskInfo.getReceiver())) {
            handlerHolder.route(taskInfo.getSendChannel())
                    .doHandler(taskInfo);
        }
    }
}
