package com.imwj.msg.stream.sink;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.imwj.msg.common.domain.AnchorInfo;
import com.imwj.msg.stream.domain.SimpleAnchorInfo;
import com.imwj.msg.stream.utils.LettuceRedisUtils;
import io.lettuce.core.RedisFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消息进 redis/hive
 * @author wj
 * @create 2022-07-13 10:49
 */
@Slf4j
public class MsgSink  implements SinkFunction<AnchorInfo> {

    /**
     * batch的超时时间和大小
     */
    private static final Integer BATCH_SIZE = 10;
    private static final Long TIME_OUT = 2000L;

    /**
     * 用ThreadLocal来暂存数据做批量处理
     */
    private static ThreadLocal<List<AnchorInfo>> baseAnchors = ThreadLocal.withInitial(() -> new ArrayList<>(BATCH_SIZE));
    private static ThreadLocal<Long> lastClearTime = ThreadLocal.withInitial(() -> new Long(System.currentTimeMillis()));


    @Override
    public void invoke(AnchorInfo anchorInfo) throws Exception {
        realTimeData(anchorInfo);
        offlineDate(anchorInfo);
    }

    /**
     * 实时数据存入Redis
     * 1.用户维度(查看用户当天收到消息的链路详情)，数量级大，只保留当天
     * 2.消息模板维度(查看消息模板整体下发情况)，数量级小，保留30天
     *
     * @param anchorInfo
     */
    private void realTimeData(AnchorInfo anchorInfo) {
        baseAnchors.get().add(anchorInfo);
        try {
            LettuceRedisUtils.pipeline(redisAsyncCommands -> {
                ArrayList<RedisFuture<?>> redisFutures = new ArrayList<>();
                for(AnchorInfo info : baseAnchors.get()){
                    /**
                     * 1.构建userId维度的链路消息  数据结构list:{key,list}
                     * key:userId,listValue:[{timestamp,state,businessId},{timestamp,state,businessId}]
                     */
                    SimpleAnchorInfo simpleAnchorInfo = SimpleAnchorInfo.builder().businessId(info.getBusinessId()).state(info.getState()).timestamp(info.getTimestamp()).build();
                    for(String id : info.getIds()){
                        redisFutures.add(redisAsyncCommands.lpush(id.getBytes(), JSON.toJSONString(simpleAnchorInfo).getBytes()));
                        redisFutures.add(redisAsyncCommands.expire(id.getBytes(), (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000));
                    }

                    /**
                     * 2.构建消息模板维度的链路消息，数据接口hash:{key,hash}
                     * key:businessId,hashValue:{state,stateCount}
                     */
                    redisFutures.add(redisAsyncCommands.hincrby(String.valueOf(info.getBusinessId()).getBytes(),
                            String.valueOf(info.getState()).getBytes(), info.getIds().size()));
                    redisFutures.add(redisAsyncCommands.expire(String.valueOf(info.getBusinessId()).getBytes(), DateUtil.offsetDay(new Date(), 30).getTime()));
                }
                return redisFutures;
            });
        }catch (Exception e){
            log.error("MsgSink#invoke error: {}", Throwables.getStackTraceAsString(e));
        } finally {
            lastClearTime.set(System.currentTimeMillis());
            baseAnchors.get().clear();
        }
    }

    /**
     * 离线数据存入hive
     * @param anchorInfo
     */
    private void offlineDate(AnchorInfo anchorInfo) {
    }
}