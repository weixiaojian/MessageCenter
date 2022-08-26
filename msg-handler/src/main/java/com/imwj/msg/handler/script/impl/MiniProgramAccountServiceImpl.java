package com.imwj.msg.handler.script.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.api.impl.WxMaSubscribeServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.imwj.msg.handler.script.MiniProgramAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wj
 * @create 2022-08-26 11:43
 */
@Service
@Slf4j
public class MiniProgramAccountServiceImpl implements MiniProgramAccountService {

    @Value("${wx.ma.account.appid}")
    private String appId;
    @Value("${wx.ma.account.secret}")
    private String secret;

    @Override
    public void send(List<WxMaSubscribeMessage> messages) throws Exception {
        WxMaSubscribeService wxMaSubscribeService = initService();
        ArrayList<String> messageIds = new ArrayList<>(messages.size());
        for(WxMaSubscribeMessage wxMaSubscribeMessage : messages){
            wxMaSubscribeService.sendSubscribeMsg(wxMaSubscribeMessage);
        }
    }

    /**
     * 初始化小程序账号信息
     */
    public WxMaSubscribeService initService(){
        WxMaService wxMaService = new WxMaServiceImpl();
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(appId);
        config.setSecret(secret);
        wxMaService.setWxMaConfig(config);
        return new WxMaSubscribeServiceImpl(wxMaService);
    }
}
