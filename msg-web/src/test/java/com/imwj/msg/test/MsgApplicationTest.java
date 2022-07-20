package com.imwj.msg.test;

import com.imwj.msg.handler.handler.HandlerHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author langao_q
 * @since 2021-12-28 10:48
 */
@SpringBootTest
class MsgApplicationTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void test(){
        redisTemplate.opsForValue().set("test1.a","1");
        redisTemplate.opsForValue().set("test1.b","1");

        System.out.println(redisTemplate.opsForValue().get("test:*"));
        System.out.println(redisTemplate.keys("test:*"));

    }

}
