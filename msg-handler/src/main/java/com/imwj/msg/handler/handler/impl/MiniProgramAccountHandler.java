package com.imwj.msg.handler.handler.impl;

import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.dto.model.MiniProgramContentModel;
import com.imwj.msg.common.enums.ChannelType;
import com.imwj.msg.handler.handler.BaseHandler;
import com.imwj.msg.handler.handler.Handler;
import com.imwj.msg.handler.script.MiniProgramAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 小程序推送订阅消息
 * @author wj
 * @create 2022-08-26 11:41
 */
@Component
@Slf4j
public class MiniProgramAccountHandler extends BaseHandler implements Handler {

    @Autowired
    private MiniProgramAccountService miniProgramAccountService;

    /**
     * 初始化渠道和handler关系
     */
    public MiniProgramAccountHandler() {
        channelCode = ChannelType.MINI_PROGRAM.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        List<WxMaSubscribeMessage> wxMaSubscribeMessages = buildTemplateMsg(taskInfo);
        // 微信模板消息需要记录相应结果
        try {
            miniProgramAccountService.send(wxMaSubscribeMessages);
            log.info("MiniProgramAccountHandler#handler successfully");
            return true;
        }catch (Exception e){
            log.error("MiniProgramAccountHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }

    /**
     * 通过taskInfo构建小程序订阅消息
     * @param taskInfo
     * @return
     */
    private List<WxMaSubscribeMessage> buildTemplateMsg(TaskInfo taskInfo){
        // 需要是关注公众号用户的openId
        Set<String> receiver = taskInfo.getReceiver();
        Long messageTemplateId = taskInfo.getMessageTemplateId();
        // 微信模板消息可以关联到系统业务 通过接口查询
        String templateId = getRealWxMpTemplateId(messageTemplateId);
        ArrayList<WxMaSubscribeMessage> wxMaSubscribeMessages = new ArrayList<>(receiver.size());
        MiniProgramContentModel contentModel = (MiniProgramContentModel) taskInfo.getContentModel();
        String page = contentModel.getPage();
        // content转换成map格式
        Map<String, String> param = JSONUtil.toBean(contentModel.getContent(), Map.class);
        // 构建微信模板消息
        for(String openId : receiver){
            WxMaSubscribeMessage templateMessage = WxMaSubscribeMessage.builder()
                    .toUser(openId)
                    .templateId(templateId)
                    .page(page)
                    .build();
            // MsgData 对应模板消息 键 -- 值 -- color
            param.forEach((k, v) -> templateMessage.addData(new WxMaSubscribeMessage.MsgData(k, v)));
            wxMaSubscribeMessages.add(templateMessage);
        }
        return wxMaSubscribeMessages;
    }

    /**
     * 根据模板id获取真实的微信模板id
     * TODO 此处暂返回固定值：k5vj8jo4AJXGzlzycz_Z8_pHxqdpQGqo6oKB4B3nP2Y
     * @return
     */
    private String getRealWxMpTemplateId(Long messageTemplateId){
        return "k5vj8jo4AJXGzlzycz_Z8_pHxqdpQGqo6oKB4B3nP2Y";
    }
}
