package com.imwj.msg.stream.sink;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.imwj.msg.common.domain.AnchorInfo;
import com.imwj.msg.common.domain.SimpleAnchorInfo;
import com.imwj.msg.stream.utils.LettuceRedisUtils;
import io.lettuce.core.RedisFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.ArrayList;
import java.util.Date;

/**
 * 消息进 redis/hive
 * @author wj
 * @create 2022-07-13 10:49
 */
@Slf4j
public class MsgSink  implements SinkFunction<AnchorInfo> {

    @Override
    public void invoke(AnchorInfo anchorInfo, Context context) throws Exception {
        realTimeData(anchorInfo);
        offlineDate(anchorInfo);
    }

    /**
     * 实时数据存入Redis
     * 1.用户维度(查看用户当天收到消息的链路详情)，数量级大，只保留当天
     * 2.消息模板维度(查看消息模板整体下发情况)，数量级小，保留30天
     *
     * @param info
     */
    private void realTimeData(AnchorInfo info) {
        try {
            LettuceRedisUtils.pipeline(redisAsyncCommands -> {
                ArrayList<RedisFuture<?>> redisFutures = new ArrayList<>();
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
                redisFutures.add(redisAsyncCommands.expire(String.valueOf(info.getBusinessId()).getBytes(), (DateUtil.offsetDay(new Date(), 30).getTime())/ 1000));
                return redisFutures;
            });
        }catch (Exception e){
            log.error("MsgSink#invoke error: {}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 离线数据存入hive
     * @param anchorInfo
     */
    private void offlineDate(AnchorInfo anchorInfo) {
    }
}