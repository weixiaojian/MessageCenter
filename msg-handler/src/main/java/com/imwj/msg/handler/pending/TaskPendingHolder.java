package com.imwj.msg.handler.pending;

import com.dtp.common.em.QueueTypeEnum;
import com.dtp.common.em.RejectedTypeEnum;
import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import com.dtp.core.thread.ThreadPoolBuilder;
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
     * 给每个渠道，每种消息类型初始化一个线程池(初始化线程池为3-5)
     * 可以通过apollo配置：dynamic-tp-apollo-dtp.yml  动态修改线程池的信息
     * 注意的是：这里for循环创建了18个线程池，那么dynamic-tp-apollo-dtp.yml中需要修改那个线程池的配置 就要指线程池名称，例子中指定的是imwj.email.notice
     * @PostConstruct：启动时就会初始化好所有的线程池
     */
    @PostConstruct
    public void init() {
        for (String groupId : groupIds) {
            DtpExecutor dtpExecutor = ThreadPoolBuilder.newBuilder()
                    .threadPoolName("imwj." + groupId)
                    .corePoolSize(coreSize)
                    .maximumPoolSize(maxSize)
                    .workQueue(QueueTypeEnum.LINKED_BLOCKING_QUEUE.getName(), queueSize, false)
                    .rejectedExecutionHandler(RejectedTypeEnum.CALLER_RUNS_POLICY.getName())
                    .buildDynamic();

            DtpRegistry.register(dtpExecutor, "beanPostProcessor");
            threadPoolExecutorShutdownDefinition.registryExecutor(dtpExecutor);
            taskPendingHolder.put(groupId, dtpExecutor);
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
