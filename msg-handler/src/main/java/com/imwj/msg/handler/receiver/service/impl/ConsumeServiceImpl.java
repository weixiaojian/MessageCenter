package com.imwj.msg.handler.receiver.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.imwj.msg.common.domain.AnchorInfo;
import com.imwj.msg.common.domain.LogParam;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.enums.AnchorState;
import com.imwj.msg.handler.handler.HandlerHolder;
import com.imwj.msg.handler.pending.Task;
import com.imwj.msg.handler.pending.TaskPendingHolder;
import com.imwj.msg.handler.receiver.service.ConsumeService;
import com.imwj.msg.handler.utils.GroupIdMappingUtils;
import com.imwj.msg.support.domain.MessageTemplate;
import com.imwj.msg.support.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息服务实现类
 * @author wj
 * @create 2022-09-20 11:03
 */
@Service
public class ConsumeServiceImpl implements ConsumeService {

    private static final String LOG_BIZ_TYPE = "Receiver#consumer";
    private static final String LOG_BIZ_RECALL_TYPE = "Receiver#recall";

    @Autowired
    private ApplicationContext context;

    @Autowired
    private TaskPendingHolder taskPendingHolder;

    @Autowired
    private LogUtils logUtils;

    @Autowired
    private HandlerHolder handlerHolder;


    @Override
    public void consume2Send(List<TaskInfo> taskInfoLists) {
        // 1.得到topicGroupId
        String topicGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfoLists));
        for(TaskInfo taskInfo : taskInfoLists){
            // 2.输出日志
            logUtils.print(LogParam.builder().bizType(LOG_BIZ_TYPE).object(taskInfo).build(), AnchorInfo.builder().ids(taskInfo.getReceiver()).businessId(taskInfo.getBusinessId()).state(AnchorState.RECEIVE.getCode()).build());
            // 3.得到Task数据对象，并通过taskPendingHolder得到线程池对象并执行task
            Task task = context.getBean(Task.class).setTaskInfo(taskInfo);
            taskPendingHolder.route(topicGroupId).execute(task);
        }
    }

    @Override
    public void consume2recall(MessageTemplate messageTemplate) {
        // 1.输出日志
        logUtils.print(LogParam.builder().bizType(LOG_BIZ_RECALL_TYPE).object(messageTemplate).build());
        // 2.通过渠道 + handlerHolder得到消息对应的handler 执行其中的消息撤回逻辑recall
        handlerHolder.route(messageTemplate.getSendChannel()).recall(messageTemplate);
    }
}
