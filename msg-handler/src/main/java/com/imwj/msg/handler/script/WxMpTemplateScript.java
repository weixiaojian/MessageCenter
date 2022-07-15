package com.imwj.msg.handler.script;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信公众号发送模板消息
 * @author wj
 * @create 2022-07-15 11:13
 */
@Service
@Slf4j
public class WxMpTemplateScript implements OfficialAccountScript {

    @Value("${wx.mp.account.appid}")
    private String appId;
    @Value("${wx.mp.account.secret}")
    private String secret;
    @Value("${wx.mp.account.token}")
    private String token;
    @Value("${wx.mp.account.aesKey}")
    private String aesKey;

    @Override
    public List<String> send(List<WxMpTemplateMessage> messages) throws Exception {
        WxMpService wxMpService = initService();
        ArrayList<String> messageIds = new ArrayList<>(messages.size());
        for(WxMpTemplateMessage wxMpTemplateMessage : messages){
            String msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
            messageIds.add(msgId);
        }
        return messageIds;
    }

    /**
     * 初始化微信服务号
     */
    public WxMpService initService(){
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(appId);
        config.setSecret(secret);
        config.setToken(token);
        config.setAesKey(aesKey);
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }
}
