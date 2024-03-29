package com.imwj.msg.handler.service.deduplication.build;

import com.alibaba.fastjson.JSONObject;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.handler.deduplication.DeduplicationParam;
import com.imwj.msg.handler.deduplication.DeduplicationHolder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 构建去重抽象类
 * @author langao_q
 * @since 2022-01-26 16:42
 */
public  abstract class AbstractDeduplicationBuilder implements Builder {

    protected Integer deduplicationType;

    @Autowired
    private DeduplicationHolder deduplicationHolder;

    @PostConstruct
    public void init() {
        deduplicationHolder.putBuilder(deduplicationType, this);
    }

    public DeduplicationParam getParamsFromConfig(Integer key, String duplicationConfig, TaskInfo taskInfo) {
        JSONObject object = JSONObject.parseObject(duplicationConfig);
        if (object == null) {
            return null;
        }
        DeduplicationParam deduplicationParam = JSONObject.parseObject(object.getString(DEDUPLICATION_CONFIG_PRE + key), DeduplicationParam.class);
        if (deduplicationParam == null) {
            return null;
        }
        deduplicationParam.setTaskInfo(taskInfo);
        return deduplicationParam;
    }
}
