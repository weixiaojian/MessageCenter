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
public interface Handler{

    /**
     * 处理器
     * @param taskInfo
     */
    void doHandler(TaskInfo taskInfo);

}
