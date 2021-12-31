package com.imwj.msg.service.deduplication;

import cn.hutool.core.collection.CollUtil;
import com.imwj.msg.constant.AustinConstant;
import com.imwj.msg.domain.DeduplicationParam;
import com.imwj.msg.domain.TaskInfo;
import com.imwj.msg.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 去重服务
 * @author langao_q
 * @since 2021-12-31 11:35
 */
@Slf4j
public abstract class AbstractDeduplicationService {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * redis去重
     * @param param
     */
    public void deduplication(DeduplicationParam param){
        TaskInfo taskInfo = param.getTaskInfo();
        HashSet<String> filterSet = new HashSet<>(taskInfo.getReceiver().size());

        //获取redis中的记录
        HashSet<String> redisSet = new HashSet<>(taskInfo.getReceiver().size());
        List<String> keys = deduplicationAllKey(taskInfo);
        Map<String, String> redisValue = redisUtils.mGet(keys);
        for(String receiver : taskInfo.getReceiver()){
            String key = deduplicationSingleKey(taskInfo, receiver);
            String value = redisValue.get(key);
            //判断是否符合条件
            if(value != null && Integer.valueOf(value) > param.getCountNum()){
                filterSet.add(receiver);
            }else{
                Queue<String> readyPutRedisReceiver;
                redisSet.add(receiver);
            }
        }
        // 不符合条件的用户：需要更新Redis(无记录添加，有记录则累加次数)
        putInRedis(redisSet, redisValue, param);
        // 剔除符合去重条件的用户
        taskInfo.getReceiver().removeAll(filterSet);
    }

    /**
     * 存入redis 实现去重(无记录添加，有记录则累加次数)
     * @param redisSet
     * @param redisValue
     * @param param
     */
    private void putInRedis(Set<String> redisSet, Map<String, String> redisValue, DeduplicationParam param){
        Map<String, String> keyValues = new HashMap<>(redisSet.size());
        for(String receiver : redisSet){
            String key = deduplicationSingleKey(param.getTaskInfo(), receiver);
            if(redisValue.get(key) != null){
                keyValues.put(key, String.valueOf(Integer.valueOf(redisValue.get(key)) + 1));
            }else{
                keyValues.put(key, String.valueOf(AustinConstant.TRUE));
            }
        }
        if (CollUtil.isNotEmpty(keyValues)) {
            redisUtils.pipelineSetEX(keyValues, param.getDeduplicationTime());
        }
    }

    /**
     * 获取得到当前消息模板所有的去重Key
     *
     * @param taskInfo
     * @return
     */
    private List<String> deduplicationAllKey(TaskInfo taskInfo) {
        List<String> result = new ArrayList<>(taskInfo.getReceiver().size());
        for (String receiver : taskInfo.getReceiver()) {
            String key = deduplicationSingleKey(taskInfo, receiver);
            result.add(key);
        }
        return result;
    }

    /**
     * 构建去重的Key
     * @param taskInfo
     * @param receiver
     * @return
     */
    protected abstract String deduplicationSingleKey(TaskInfo taskInfo, String receiver);
}
