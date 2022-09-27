package com.imwj.msg.test;

import com.imwj.msg.support.utils.NacosUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author langao_q
 * @since 2021-12-28 10:48
 */
@SpringBootTest
class MsgApplicationTest {

    @Autowired
    private NacosUtils nacosUtils;

    @Test
    public void test(){
        System.out.println(nacosUtils.getProperty("smsAccount", ""));;
    }

}
