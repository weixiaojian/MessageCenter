package com.imwj.msg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author langao_q
 * @since 2021-12-28 10:48
 */
@SpringBootApplication
public class MsgApplication {


    public static void main(String[] args) {
        // apollo的地址
        System.setProperty("apollo.config-service", "http://127.0.0.1:8080");

        SpringApplication.run(MsgApplication.class, args);
    }
}
