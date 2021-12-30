package com.imwj.msg.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imwj.msg.dao.MessageTemplateDao;
import com.imwj.msg.domain.MessageTemplate;
import com.imwj.msg.domain.RetResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 模板添加controller
 * @author langao_q
 * @since 2021-12-29 16:13
 */
@RestController
@RequestMapping("/messageTemplate")
public class MessageTemplateController {

    @Resource
    private MessageTemplateDao messageTemplateDao;

    /**
     * 插入一条模板数据
     * @return
     */
    @GetMapping("/insert")
    public RetResult insert() {
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
                .msgContent("{\"content\":\"{$contentValue}\"}")
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
        messageTemplateDao.insert(messageTemplate);
        return RetResult.success(messageTemplate);
    }

    /**
     * 查询所有的模板数据
     */
    @GetMapping("/query")
    public RetResult query() {
        QueryWrapper<MessageTemplate> queryWrapper = new QueryWrapper<>();
        List<MessageTemplate> list = messageTemplateDao.selectList(queryWrapper);
        return RetResult.success(list);
    }
}
