package com.imwj.msg.cron.pending;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.imwj.msg.api.domain.BatchSendRequest;
import com.imwj.msg.api.domain.MessageParam;
import com.imwj.msg.api.enums.BusinessCode;
import com.imwj.msg.api.service.SendService;
import com.imwj.msg.cron.constant.PendingConstant;
import com.imwj.msg.cron.domain.CrowdInfoVo;
import com.imwj.msg.support.pending.AbstractLazyPending;
import com.imwj.msg.support.pending.PendingParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 批量处理任务信息
 * 调用 batch 发送接口 进行消息推送
 * @author wj
 * @create 2022-07-04 16:04
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CrowdBatchTaskPending extends AbstractLazyPending<CrowdInfoVo> {

    @Autowired
    private SendService sendService;

    /**
     * 初始化
     */
    public CrowdBatchTaskPending(){
        PendingParam<CrowdInfoVo> pendingParam = new PendingParam<>();
        pendingParam.setNumThreshold(PendingConstant.NUM_THRESHOLD)
                .setQueue(new LinkedBlockingQueue(PendingConstant.QUEUE_SIZE))
                .setTimeThreshold(PendingConstant.TIME_THRESHOLD)
                .setExecutorService(ExecutorBuilder.create()
                        .setCorePoolSize(PendingConstant.CORE_POOL_SIZE)
                        .setMaxPoolSize(PendingConstant.MAX_POOL_SIZE)
                        .setWorkQueue(PendingConstant.BLOCKING_QUEUE)
                        .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                        .setAllowCoreThreadTimeOut(true)
                        .setKeepAliveTime(PendingConstant.KEEP_LIVE_TIME, TimeUnit.SECONDS)
                .build());
        this.pendingParam = pendingParam;
    }


    @Override
    public void doHandle(List<CrowdInfoVo> list) {
        log.info("theadName:{},doHandle:{},CrowdInfoVo{}", Thread.currentThread().getName(), list.size(), JSONUtil.toJsonStr(list));
        //1.如果参数相同 组装成同一个MessageParam发送
        Map<Map<String, String>, String> paramMap = MapUtil.newHashMap();
        for(CrowdInfoVo crowdInfoVo : list){
            String receiver = crowdInfoVo.getReceiver();
            Map<String, String> vars = crowdInfoVo.getParams();
            if(paramMap.get(vars) == null){
                paramMap.put(vars, receiver);
            }else{
                String newReceiver = StringUtils.join(new String[]{paramMap.get(vars), receiver}, StrUtil.COMMA);
                paramMap.put(vars,newReceiver);
            }
        }
        //2.组装参数
        ArrayList<MessageParam> messageParamList = Lists.newArrayList();
        for(Map.Entry<Map<String, String>, String> entry : paramMap.entrySet()){
            MessageParam messageParam = MessageParam.builder().receiver(entry.getValue()).variables(entry.getKey()).build();
            messageParamList.add(messageParam);
        }
        //3.调用批量发送接口发送消息
        BatchSendRequest batchSendRequest = BatchSendRequest.builder().code(BusinessCode.COMMON_SEND.getCode())
                .messageParamList(messageParamList)
                .messageTemplateId(CollUtil.getFirst(list.iterator()).getMessageTemplateId())
                .build();
        sendService.batchSend(batchSendRequest);
    }
}
