package com.imwj.msg.handler.pending;

import com.dtp.core.thread.DtpExecutor;
import com.imwj.msg.handler.config.HandlerThreadPoolConfig;
import com.imwj.msg.handler.utils.GroupIdMappingUtils;
import com.imwj.msg.support.utils.ThreadPoolUtils;
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
    private ThreadPoolUtils threadPoolUtils;

    /**
     * 任务线程池管理器（存储消费kafka的所有线程池）
     */
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
            //获取线程池
            DtpExecutor executor = HandlerThreadPoolConfig.getExecutor(groupId);
            //注册线程池（注册为动态线程池 + 优雅关闭）
            threadPoolUtils.register(executor);
            //添加到任务管理器
            taskPendingHolder.put(groupId, executor);
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
