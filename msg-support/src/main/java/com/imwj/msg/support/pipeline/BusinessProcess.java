package com.imwj.msg.support.pipeline;

/**
 * 业务执行器
 * @author langao_q
 * @since 2021-12-29 17:31
 */
public interface BusinessProcess<T extends ProcessModel>  {

    /**
     * 真正处理逻辑
     * @param context
     */
    void process(ProcessContext<T> context);
}
