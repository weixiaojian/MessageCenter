package com.imwj.msg.handler.script;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送存储器（服务名 -> 对应处理器）
 * @author wj
 * @create 2022-08-31 16:54
 */
@Component
public class SmsScriptHolder {

    private Map<String, SmsScript> handlers = new HashMap<>(8);

    /**
     * 注册handler
     * @param scriptName
     * @param handler
     */
    public void putHandler(String scriptName, SmsScript handler) {
        handlers.put(scriptName, handler);
    }

    /**
     * 根据服务名获取指定handler
     * @param scriptName
     * @return
     */
    public SmsScript route(String scriptName) {
        return handlers.get(scriptName);
    }
}
