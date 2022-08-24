package com.imwj.msg.handler.limit;

import cn.hutool.core.collection.CollUtil;
import com.imwj.msg.common.constant.MessageCenterConstant;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.handler.deduplication.DeduplicationParam;
import com.imwj.msg.handler.service.deduplication.service.AbstractDeduplicationService;
import com.imwj.msg.support.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 简单去重器（目前承载着 N分钟相同内容去重）
 * @author wj
 * @create 2022-08-22 11:07
 */
@Service(value = "SimpleLimitService")
public class SimpleLimitService extends AbstractLimitService {

    private static final String LIMIT_TAG = "SP_";

    @Autowired
    private RedisUtils redisUtils;


    public Set<String> limitFilter(AbstractDeduplicationService service, TaskInfo taskInfo, DeduplicationParam param) {
        Set<String> filterReceiver = new HashSet<>(taskInfo.getReceiver().size());
        // 获取redis记录
        Map<String, String> readyPutRedisReceiver = new HashMap(taskInfo.getReceiver().size());
        //redis数据隔离
        List<String> keys = deduplicationAllKey(service, taskInfo).stream().map(key -> LIMIT_TAG + key).collect(Collectors.toList());
        Map<String, String> inRedisValue = redisUtils.mGet(keys);

        for (String receiver : taskInfo.getReceiver()) {
            String key = LIMIT_TAG + deduplicationSingleKey(service, taskInfo, receiver);
            String value = inRedisValue.get(key);

            // 符合条件的用户
            if (value != null && Integer.parseInt(value) >= param.getCountNum()) {
                filterReceiver.add(receiver);
            } else {
                readyPutRedisReceiver.put(receiver, key);
            }
        }

        // 不符合条件的用户：需要更新Redis(无记录添加，有记录则累加次数)
        putInRedis(readyPutRedisReceiver, inRedisValue, param.getDeduplicationTime());

        return filterReceiver;
    }


    /**
     * 存入redis 实现去重
     *
     * @param readyPutRedisReceiver
     */
    private void putInRedis(Map<String, String> readyPutRedisReceiver,
                            Map<String, String> inRedisValue, Long deduplicationTime) {
        Map<String, String> keyValues = new HashMap<>(readyPutRedisReceiver.size());
        for (Map.Entry<String, String> entry : readyPutRedisReceiver.entrySet()) {
            String key = entry.getValue();
            if (inRedisValue.get(key) != null) {
                keyValues.put(key, String.valueOf(Integer.valueOf(inRedisValue.get(key)) + 1));
            } else {
                keyValues.put(key, String.valueOf(MessageCenterConstant.TRUE));
            }
        }
        if (CollUtil.isNotEmpty(keyValues)) {
            redisUtils.pipelineSetEx(keyValues, deduplicationTime);
        }
    }

}