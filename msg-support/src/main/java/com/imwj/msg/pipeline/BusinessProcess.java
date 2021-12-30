package com.imwj.msg.pipeline;

/**
 * @author langao_q
 * @since 2021-12-29 17:31
 */
public interface BusinessProcess {

    /**
     * 真正处理逻辑
     * @param context
     */
    void process(ProcessContext context);
}
