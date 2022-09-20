package com.imwj.msg.support.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.ctrip.framework.apollo.Config;
import com.imwj.msg.support.service.ConfigService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * 读取配置实现类
 * @author wj
 * @create 2022-09-14 17:25
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    /**
     * 本地配置
     */
    private static final String PROPERTIES_PATH = "local.properties";
    private Props props = new Props(PROPERTIES_PATH, StandardCharsets.UTF_8);

    /**
     * apollo配置
     */
    @Value("${apollo.bootstrap.enabled}")
    private Boolean enableApollo;
    @Value("${apollo.bootstrap.namespaces}")
    private String namespaces;


    @Override
    public String getProperty(String key, String defaultValue) {
        if (enableApollo) {
            // apollo启用时读取服务器上的配置
            Config config = com.ctrip.framework.apollo.ConfigService.getConfig(namespaces.split(StrUtil.COMMA)[0]);
            return config.getProperty(key, defaultValue);
        } else {
            // apollo未启用读取`local.properties`文件配置
            return props.getProperty(key, defaultValue);
        }
    }
}
