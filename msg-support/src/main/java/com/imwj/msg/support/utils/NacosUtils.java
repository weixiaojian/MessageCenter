package com.imwj.msg.support.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.StringReader;
import java.util.Properties;

/**
 * nacos获取配置工具类
 * @author wj
 * @create 2022-09-23 16:17
 */
@Slf4j
@Component
@ConfigurationProperties()
public class NacosUtils {

    @Value("${msg.nacos.server}")
    private String nacosServer;
    @Value("${msg.nacos.group}")
    private String nacosGroup;
    @Value("${msg.nacos.dataId}")
    private String nacosDataId;
    @Value("${msg.nacos.namespace}")
    private String nacosNamespace;
    private final Properties request = new Properties();
    private final Properties properties = new Properties();

    /**
     * 根据key获取nacos中的配置值
     * @param key
     * @param defaultValue
     * @return
     */
    public String getProperty(String key, String defaultValue) {
        try {
            String property = this.getContext();
            if (StringUtils.hasText(property)) {
                properties.load(new StringReader(property));
            }
        } catch (Exception e) {
            log.error("Nacos error:{}", ExceptionUtils.getStackTrace(e));
        }
        String property = properties.getProperty(key, defaultValue);
        return StrUtil.isBlank(property) ? defaultValue : property;
    }

    /**
     * 初始化nacos连接对象
     * @return
     */
    private String getContext() {
        String context = null;
        try {
            request.put(PropertyKeyConst.SERVER_ADDR, nacosServer);
            request.put(PropertyKeyConst.NAMESPACE, nacosNamespace);
            context = NacosFactory.createConfigService(request)
                    .getConfig(nacosDataId, nacosGroup, 5000);
        } catch (NacosException e) {
            log.error("Nacos error:{}", ExceptionUtils.getStackTrace(e));
        }
        return context;
    }
}
