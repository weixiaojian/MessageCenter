package com.imwj.msg.web.service;

import com.imwj.msg.web.vo.EchartsVo;
import com.imwj.msg.web.vo.UserTimeLineVo;

/**
 * Flink数据接口
 * @author wj
 * @create 2022-07-19 10:21
 */
public interface DataService {

    /**
     * 获取全链路追踪 用户维度信息
     * @param receiver 接收者
     */
    UserTimeLineVo getTraceUserInfo(String receiver);


    /**
     * 获取全链路追踪 消息模板维度信息
     * @param businessId 业务ID（如果传入消息模板ID，则生成当天的业务ID）
     */
    EchartsVo getTraceMessageTemplateInfo(String businessId);

}
