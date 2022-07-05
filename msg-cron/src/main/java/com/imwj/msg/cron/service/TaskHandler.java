package com.imwj.msg.cron.service;

/**
 * 具体处理定时任务逻辑的Handler
 * @author wj
 * @create 2022-05-26 16:52
 */
public interface TaskHandler {

    /**
     * 处理具体的逻辑
     *
     * @param messageTemplateId
     */
    void handle(Long messageTemplateId);

}
