package com.imwj.msg.handler.receiver;

import com.imwj.msg.support.config.SupportThreadPoolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 拉取短信回执信息（腾讯和云片）
 * @author wj
 * @create 2022-08-31 15:22
 */
@Component
@Slf4j
public class SmsReceipt {

    @Autowired
    private TencentSmsReceipt tencentSmsReceipt;

    @Autowired
    private YunPianSmsReceipt yunPianSmsReceipt;

    @PostConstruct
    private void init() {
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            while (true) {
                // TODO回执拉取暂时关闭 需要时打开即可
                // tencentSmsReceipt.pull();
                // yunPianSmsReceipt.pull();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
            }
        });
    }
}
