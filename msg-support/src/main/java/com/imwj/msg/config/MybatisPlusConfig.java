package com.imwj.msg.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * mybatis-plus扫描Mapper.java
 * @author langao_q
 * @since 2020-07-20 10:40
 */
@Configuration
@MapperScan("com.imwj.dao")
@EnableTransactionManagement
public class MybatisPlusConfig {

    /**
     * 分页插件，自动识别数据库类型
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
