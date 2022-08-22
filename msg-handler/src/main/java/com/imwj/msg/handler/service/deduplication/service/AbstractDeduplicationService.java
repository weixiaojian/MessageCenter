package com.imwj.msg.handler.service.deduplication.service;

import cn.hutool.core.collection.CollUtil;
import com.imwj.msg.common.domain.AnchorInfo;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.handler.deduplication.DeduplicationHolder;
import com.imwj.msg.handler.deduplication.DeduplicationParam;
import com.imwj.msg.handler.limit.LimitService;
import com.imwj.msg.support.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * 去重服务
 * @author langao_q
 * @since 2021-12-31 11:35
 */
@Slf4j
public abstract class AbstractDeduplicationService implements DeduplicationService {
    protected Integer deduplicationType;
    protected LimitService limitService;
    @Autowired
    private DeduplicationHolder deduplicationHolder;
    @Autowired
    private LogUtils logUtils;

    @PostConstruct
    private void init() {
        deduplicationHolder.putService(deduplicationType, this);
    }
    /**
     * redis去重
     * @param param
     */
    @Override
    public void deduplication(DeduplicationParam param){
        TaskInfo taskInfo = param.getTaskInfo();
        Set<String> filterReceiver = limitService.limitFilter(this, taskInfo, param);
        // 剔除符合去重条件的用户
        if (CollUtil.isNotEmpty(filterReceiver)) {
            taskInfo.getReceiver().removeAll(filterReceiver);
            logUtils.print(AnchorInfo.builder().businessId(taskInfo.getBusinessId()).ids(filterReceiver).state(param.getAnchorState().getCode()).build());
        }
    }

    /**
     * 构建去重的Key
     *
     * @param taskInfo
     * @param receiver
     * @return
     */
    public abstract String deduplicationSingleKey(TaskInfo taskInfo, String receiver);
}
