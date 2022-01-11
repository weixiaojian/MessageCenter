package com.imwj.msg.handler;

import com.imwj.msg.domain.AnchorInfo;
import com.imwj.msg.domain.TaskInfo;
import com.imwj.msg.enums.AnchorState;
import com.imwj.msg.util.LogUtils;
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
        //消息处理失败日志
        if(!handler(taskInfo)){
            LogUtils.print(AnchorInfo.builder().state(AnchorState.SEND_FAIL.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
        }
        //消息处理成功日志
        LogUtils.print(AnchorInfo.builder().state(AnchorState.SEND_SUCCESS.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
    }

    /**
     * 统一处理的handler接口
     *
     * @param taskInfo
     * @return
     */
    public abstract boolean handler(TaskInfo taskInfo);

}
