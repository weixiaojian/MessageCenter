package com.imwj.msg.controller;

import com.imwj.msg.domain.MessageParam;
import com.imwj.msg.domain.RetResult;
import com.imwj.msg.domain.SendRequest;
import com.imwj.msg.domain.SendResponse;
import com.imwj.msg.enums.BusinessCode;
import com.imwj.msg.service.SendService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送controller
 * @author langao_q
 * @since 2021-12-29 10:26
 */
@RestController
@RequestMapping("/sms")
public class SendController {

    @Resource
    private SendService sendService;

    @GetMapping("/sendSmsTest")
    public RetResult sendSmsTest(String phone, Long templateId) {
        //短信内容
        Map<String, String> variables = new HashMap<>(8);
        variables.put("contentValue", "6666");
        MessageParam messageParam = new MessageParam().setReceiver(phone).setVariables(variables);

        SendRequest sendRequest = new SendRequest().setCode(BusinessCode.COMMON_SEND.getCode())
                .setMessageTemplateId(templateId)
                .setMessageParam(messageParam);
        SendResponse sendResponse = sendService.send(sendRequest);
        return RetResult.success(sendResponse);
    }

}
