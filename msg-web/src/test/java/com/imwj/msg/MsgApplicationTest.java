package com.imwj.msg;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;

/**
 * @author langao_q
 * @since 2021-12-28 10:48
 */
@SpringBootTest
class MsgApplicationTest {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Test
    public void test(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", "test");
        System.err.println("发送用户日志数据:"+JSON.toJSONString(map));
        kafkaTemplate.send("MESSAGE_CENTER", JSON.toJSONString(map));
    }

}
