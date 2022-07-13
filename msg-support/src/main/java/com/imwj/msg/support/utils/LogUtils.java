package com.imwj.msg.support.utils;

import cn.monitor4all.logRecord.bean.LogDTO;
import cn.monitor4all.logRecord.service.CustomLogListener;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.imwj.msg.common.domain.AnchorInfo;
import com.imwj.msg.common.domain.LogParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 日志埋点工具类
 * @author langao_q
 * @since 2022-01-11 10:18
 */
@Slf4j
@Component
public class LogUtils extends CustomLogListener {

    @Autowired
    private KafkaUtils kafkaUtils;

    @Value("${msg.business.log.topic.name}")
    private String topicName;

    /**
     * 方法切面的日志 @OperationLog 所产生
     */
    @Override
    public void createLog(LogDTO logDTO) throws Exception {
        log.info(JSON.toJSONString(logDTO));
    }

    /**
     * 记录当前对象信息
     */
    public void print(LogParam logParam) {
        logParam.setTimestamp(System.currentTimeMillis());
        log.info(JSON.toJSONString(logParam));
    }

    /**
     * 记录打点信息(将日志发送到kafka)
     */
    public void print(AnchorInfo anchorInfo) {
        anchorInfo.setTimestamp(System.currentTimeMillis());
        String message = JSON.toJSONString(anchorInfo);
        log.info(JSON.toJSONString(anchorInfo));
        try {
            kafkaUtils.send(topicName, message);
        } catch (Exception e) {
            log.error("LogUtils#print kafka fail! e:{},params:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(anchorInfo));
        }

    }

    /**
     * 记录当前对象信息和打点信息
     */
    public void print(LogParam logParam,AnchorInfo anchorInfo) {
        print(anchorInfo);
        print(logParam);
    }

}