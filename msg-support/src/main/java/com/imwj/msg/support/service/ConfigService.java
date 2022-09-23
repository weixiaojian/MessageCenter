package com.imwj.msg.support.service;

/**
 * 读取配置服务
 * @author wj
 * @create 2022-09-14 17:25
 */
public interface ConfigService {

    /**
     * 读取配置
     * 1、当启动使用了apollo、nacos，优先读取apollo
     * 2、当没有启动apollo，读取本地 local.properties 配置文件的内容
     * @param key
     * @param defaultValue
     * @return
     */
    String getProperty(String key, String defaultValue);
}
