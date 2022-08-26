package com.imwj.msg.web.controller;


import com.imwj.msg.api.domain.SendRequest;
import com.imwj.msg.api.domain.SendResponse;
import com.imwj.msg.api.service.SendService;
import com.imwj.msg.common.domain.RetResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 短信发送controller
 * @author langao_q
 * @since 2021-12-29 10:26
 */
@Api("发送消息")
@Slf4j
@RestController
@RequestMapping("/sms")
public class SendController {

    @Resource
    private SendService sendService;

    /**
     * 发送短信 {"code":"send","messageParam":{"receiver":"15200985202","variables":{"content":"6666"}},"messageTemplateId":1}
     *
     * @param sendRequest
     * @return
     */
    @ApiOperation("/发送短信")
    @RequestMapping("/sendSmsTest")
    public RetResult sendSmsTest(@RequestBody SendRequest sendRequest) {
        SendResponse sendResponse = sendService.send(sendRequest);
        return RetResult.success(sendResponse);
    }

    /**
     * 发送邮件 {"code":"send","messageParam":{"receiver":"2916863213@qq.com","variables":{"title":"EmailTest","content":"6666"}},"messageTemplateId":2}
     *
     * @param sendRequest
     * @return
     */
    @ApiOperation("/发送邮件")
    @RequestMapping("/sendEmailTest")
    public RetResult sendEmailTest(@RequestBody SendRequest sendRequest) {
        SendResponse sendResponse = sendService.send(sendRequest);
        return RetResult.success(sendResponse);
    }

    /**
     * 发送公众号模板消息：{"code":"send","messageParam":{"receiver":"o5HmTt3gF7lFFAStoLT5Ij4HE1ow","variables":{"content":"{orderNumber:'1', status:'2'}","url":"http://blog.imwj.club/"}},"messageTemplateId":6}
     * 发送企业微信消息：{"code":"send","messageParam":{"receiver":"15200985202","variables":{"content":"测试"}},"messageTemplateId":7}
     * 发送小程序订阅消息：{"code":"send","messageParam":{"receiver":"o5HmTt3gF7lFFAStoLT5Ij4HE1ow","variables":{"content":"{thing1:'1', thing3:'2'}","page":"http://blog.imwj.club/"}},"messageTemplateId":8}
     * @param sendRequest
     * @return
     */
    @ApiOperation("/发送公众号模板消息")
    @RequestMapping("/sendWechatTest")
    public RetResult sendWechatTest(@RequestBody SendRequest sendRequest) {
        SendResponse sendResponse = sendService.send(sendRequest);
        return RetResult.success(sendResponse);
    }

}
