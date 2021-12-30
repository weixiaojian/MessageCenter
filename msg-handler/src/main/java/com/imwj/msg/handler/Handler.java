package com.imwj.msg.handler;

import com.imwj.msg.domain.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 发送各个渠道的handler
 * @author langao_q
 * @since 2021-12-30 15:50
 */
public abstract class Handler{

    /**
     * 标识渠道的Code
     * 子类初始化的时候指定
     */
    protected Integer channelCode;


    @Autowired
    private HandlerHolder handlerHolder;

    /**
     * 初始化渠道和handler的映射关系
     */
    @PostConstruct
    public void init(){
        handlerHolder.putHandler(channelCode,this);
    }

    public void doHandler(TaskInfo taskInfo) {
        handler(taskInfo);
    }

    /**
     * 统一处理的handler接口
     *
     * @param taskInfo
     * @return
     */
    public abstract void handler(TaskInfo taskInfo);

}
