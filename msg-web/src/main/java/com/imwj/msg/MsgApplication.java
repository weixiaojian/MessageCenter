package com.imwj.msg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author langao_q
 * @since 2021-12-28 10:48
 */
@SpringBootApplication
@MapperScan("com.imwj.msg.support.dao")
public class MsgApplication {


    public static void main(String[] args) {
        // apollo的地址
        System.setProperty("apollo.config-service", "http://192.168.156.128:8080");

        SpringApplication.run(MsgApplication.class, args);
    }
}
