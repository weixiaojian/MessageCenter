package com.imwj.msg.handler.pending;

import com.imwj.msg.handler.config.ThreadPoolConfig;
import com.imwj.msg.handler.utils.GroupIdMappingUtils;
import com.imwj.msg.support.config.ThreadPoolExecutorShutdownDefinition;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ThreadPoolExecutorShutdownDefinition threadPoolExecutorShutdownDefinition;

    /**
     * 线程池的参数
     */
    private Integer coreSize = 3;
    private Integer maxSize = 5;
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
            // 得到一个线程池
            ExecutorService threadPool = ThreadPoolConfig.getThreadPool(coreSize, maxSize, queueSize);
            // 注册线程池（方便优雅的关闭线程池）
            threadPoolExecutorShutdownDefinition.registryExecutor(threadPool);
            // 将线程池存储
            taskPendingHolder.put(groupId, threadPool);
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
