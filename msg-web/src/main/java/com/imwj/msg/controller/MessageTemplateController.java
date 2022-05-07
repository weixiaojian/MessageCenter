package com.imwj.msg.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imwj.msg.dao.MessageTemplateDao;
import com.imwj.msg.domain.*;
import com.imwj.msg.enums.BusinessCode;
import com.imwj.msg.enums.RespStatusEnum;
import com.imwj.msg.page.PageVo;
import com.imwj.msg.page.RetPage;
import com.imwj.msg.service.SendService;
import com.imwj.msg.service.impl.MessageTemplateServiceImpl;
import com.imwj.msg.service.messageTemplateService;
import com.imwj.msg.utils.ConvertMap;
import com.imwj.msg.vo.BasicResultVO;
import com.imwj.msg.vo.MessageTemplateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 模板添加controller
 * @author langao_q
 * @since 2021-12-29 16:13
 */
@Api("模板管理")
@RestController
@RequestMapping("/messageTemplate")
public class MessageTemplateController {

    private static final List<String> flatFieldName = Arrays.asList("msgContent");

    @Resource
    private MessageTemplateDao messageTemplateDao;
    @Resource
    private MessageTemplateServiceImpl messageTemplateService;

    @Autowired
    private SendService sendService;


    /**
     * 插入一条短信模板数据
     * @return
     */
    @ApiOperation("/插入一条短信模板数据")
    @GetMapping("/insertSms")
    public RetResult insertSms() {
        MessageTemplate messageTemplate = MessageTemplate.builder()
                .name("短信模板")
                .auditStatus(10)
                .flowId("test")
                .msgStatus(10)
                .idType(30)
                .sendChannel(30)
                .templateType(10)
                .msgType(10)
                .expectPushTime("0")
                .msgContent("{\"content\":\"{$content}\"}")
                .sendAccount(66)
                .creator("yyyyc")
                .updator("yyyyu")
                .team("yyyt")
                .proposer("yyyy22")
                .auditor("yyyyyyz")
                .isDeleted(0)
                .created(Math.toIntExact(DateUtil.currentSeconds()))
                .updated(Math.toIntExact(DateUtil.currentSeconds()))
                .deduplicationTime(1)
                .isNightShield(0)
                .build();
        messageTemplateService.saveOrUpdate(messageTemplate);

        return RetResult.success(messageTemplate);
    }

    /**
     * 插入一条邮件模板数据
     * @return
     */
    @ApiOperation("/插入一条邮件模板数据")
    @GetMapping("/insertEmail")
    public RetResult insertEmail() {
        MessageTemplate messageTemplate = MessageTemplate.builder()
                .name("邮件模板")
                .auditStatus(10)
                .flowId("test")
                .msgStatus(10)
                .idType(40)
                .sendChannel(40)
                .templateType(20)
                .msgType(10)
                .expectPushTime("0")
                .msgContent("{\"content\":\"{$content}\",\"title\":\"{$title}\"}")
                .sendAccount(66)
                .creator("yyyyc")
                .updator("yyyyu")
                .team("yyyt")
                .proposer("yyyy22")
                .auditor("yyyyyyz")
                .isDeleted(0)
                .created(Math.toIntExact(DateUtil.currentSeconds()))
                .updated(Math.toIntExact(DateUtil.currentSeconds()))
                .deduplicationTime(1)
                .isNightShield(0)
                .build();
        messageTemplateService.saveOrUpdate(messageTemplate);
        return RetResult.success(messageTemplate);
    }


    /**
     * 查询所有的模板数据
     */
    @ApiOperation("/查询所有的模板数据")
    @GetMapping("/list")
    public BasicResultVO query(PageVo pageVo) {
        List<Map<String, Object>> result = ConvertMap.flatList(messageTemplateService.queryList(pageVo), flatFieldName);
        long count = messageTemplateService.count();
        MessageTemplateVo messageTemplateVo = MessageTemplateVo.builder().count(count).rows(result).build();
        return BasicResultVO.success(messageTemplateVo);
    }

    /**
     * 如果Id存在，则修改
     * 如果Id不存在，则保存
     */
    @PostMapping("/save")
    @ApiOperation("/插入数据")
    public BasicResultVO saveOrUpdate(@RequestBody MessageTemplate messageTemplate) {
        MessageTemplate info = messageTemplateService.saveOrUpdate(messageTemplate);
        return BasicResultVO.success(info);
    }

    /**
     * 根据Id查找
     */
    @GetMapping("query/{id}")
    @ApiOperation("/根据Id查找")
    public BasicResultVO queryById(@PathVariable("id") Long id) {
        Map<String, Object> result = ConvertMap.flatSingle(messageTemplateService.queryById(id), flatFieldName);
        return BasicResultVO.success(result);
    }

    /**
     * 根据Id复制
     */
    @PostMapping("copy/{id}")
    @ApiOperation("/根据Id复制")
    public BasicResultVO copyById(@PathVariable("id") Long id) {
        messageTemplateService.copy(id);
        return BasicResultVO.success();
    }

    /**
     * 根据Id删除
     * id多个用逗号分隔开
     */
    @DeleteMapping("delete/{id}")
    @ApiOperation("/根据Ids删除")
    public BasicResultVO deleteByIds(@PathVariable("id") String id) {

        if (StrUtil.isNotBlank(id)) {
            List<Long> idList = Arrays.stream(id.split(StrUtil.COMMA)).map(s -> Long.valueOf(s)).collect(Collectors.toList());
            messageTemplateService.deleteByIds(idList);
            return BasicResultVO.success();
        }
        return BasicResultVO.fail();
    }

    /**
     * 测试发送接口
     */
    @PostMapping("test")
    @ApiOperation("/测试发送接口")
    public BasicResultVO test(@RequestBody PageVo pageVo) {

        Map<String, String> variables = JSON.parseObject(pageVo.getMsgContent(), Map.class);
        MessageParam messageParam = MessageParam.builder().receiver(pageVo.getReceiver()).variables(variables).build();
        SendRequest sendRequest = SendRequest.builder().code(BusinessCode.COMMON_SEND.getCode()).messageTemplateId(pageVo.getId()).messageParam(messageParam).build();
        SendResponse response = sendService.send(sendRequest);
        if (response.getCode() != RespStatusEnum.SUCCESS.getCode()) {
            return BasicResultVO.fail(response.getMsg());
        }
        return BasicResultVO.success(response);
    }
}
