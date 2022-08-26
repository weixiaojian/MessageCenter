package com.imwj.msg.handler.script;

import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;

import java.util.List;

/**
 * 小程序消息service
 * @author wj
 * @create 2022-08-26 11:43
 */
public interface MiniProgramAccountService {

    /**
     * 发送订阅消息
     *
     * @param wxMaSubscribeMessages 模板消息列表
     * @return
     * @throws Exception
     */
    void send(List<WxMaSubscribeMessage> wxMaSubscribeMessages) throws Exception;

}
