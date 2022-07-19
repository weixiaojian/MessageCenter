package com.imwj.msg.web.service;

import com.imwj.msg.web.vo.EchartsVo;
import com.imwj.msg.web.vo.TimeLineItemVo;

/**
 * Flink数据接口
 * @author wj
 * @create 2022-07-19 10:21
 */
public interface DataService {

    /**
     * 获取全链路追踪 用户维度信息
     */
    TimeLineItemVo getTraceUserInfo(String receiver);


    /**
     * 获取全链路追踪 消息模板维度信息
     */
    EchartsVo getTraceMessageTemplateInfo(String businessId);

}
