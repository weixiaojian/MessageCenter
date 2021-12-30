package com.imwj.msg.pending;

import com.imwj.msg.config.ThreadPoolConfig;
import com.imwj.msg.utils.GroupIdMappingUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 存储 每种消息类型 与 TaskPending 的关系
 * @author langao_q
 * @since 2021-12-30 10:54
 */
@Component
public class TaskPendingHolder {
    /**
     * 线程池的参数
     */
    private Integer coreSize = 3;
    private Integer maxSize = 10;
    private Integer queueSize = 100;
    private Map<String, ExecutorService> taskPendingHolder = new HashMap<>(32);

    /**
     * 获取得到所有的groupId
     */
    private static List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();

    /**
     * 给每个渠道，每种消息类型初始化一个线程池
     * @PostConstruct：启动时就会初始化好所有的线程池
     */
    @PostConstruct
    public void init() {
        for (String groupId : groupIds) {
            taskPendingHolder.put(groupId, ThreadPoolConfig.getThreadPool(coreSize, maxSize, queueSize));
        }
    }

    /**
     * 根据groupId获取指定的线程池
     * @param groupId
     * @return
     */
    public ExecutorService route(String groupId){
        return taskPendingHolder.get(groupId);
    }

}
