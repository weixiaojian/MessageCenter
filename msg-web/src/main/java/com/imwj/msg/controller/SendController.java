package com.imwj.msg.controller;

import com.imwj.msg.domain.RetResult;
import com.imwj.msg.domain.SendRequest;
import com.imwj.msg.domain.SendResponse;
import com.imwj.msg.service.SendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 短信发送controller
 * @author langao_q
 * @since 2021-12-29 10:26
 */
@Slf4j
@RestController
@RequestMapping("/sms")
public class SendController {

    @Resource
    private SendService sendService;

    /**
     * 发送短信 {"code":"send","messageParam":{"receiver":"15200985202","variables":{"contentValue":"6666"}},"messageTemplateId":1}
     * @param sendRequest
     * @return
     */
    @GetMapping("/sendSmsTest")
    public RetResult sendSmsTest(@RequestBody SendRequest sendRequest) {
        SendResponse sendResponse = sendService.send(sendRequest);
        return RetResult.success(sendResponse);
    }

    /**
     * 发送邮件 {"code":"send","messageParam":{"receiver":"2916863213@qq.com","variables":{"title":"EmailTest","contentValue":"6666"}},"messageTemplateId":2}
     * @param sendRequest
     * @return
     */
    @GetMapping("/sendEmailTest")
    public RetResult sendEmailTest(@RequestBody SendRequest sendRequest) {
        SendResponse sendResponse = sendService.send(sendRequest);
        return RetResult.success(sendResponse);
    }

}
