package com.imwj.msg.test;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @author langao_q
 * @since 2022-01-11 15:08
 */
public class Main {

    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(2);
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                fun1(rateLimiter);
            }, String.valueOf(i)).start();
        }
    }


    public static void fun1(RateLimiter rateLimiter){
        System.out.println("线程名：" + Thread.currentThread().getName());
        Double costTime = rateLimiter.acquire(2);
        if(costTime > 0){
            System.out.println(Thread.currentThread().getName() + "--限流中...");
        }
    }
}
