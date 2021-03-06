package com.imwj.msg.handler.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * channel->Handler的映射关系
 * @author langao_q
 * @since 2021-12-30 15:48
 */
@Component
public class HandlerHolder {

    public Map<Integer, Handler> handlers = new HashMap<Integer, Handler>(32);

    public void putHandler(Integer channelCode, Handler handler) {
        handlers.put(channelCode, handler);
    }

    public Handler route(Integer channelCode) {
        return handlers.get(channelCode);
    }
}
