package com.imwj.msg.handler.script;

import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

import java.util.List;

/**
 * @author wj
 * @create 2022-07-15 11:12
 */
public interface OfficialAccountScript {

    /**
     * 发送模板消息
     *
     * @param wxMpTemplateMessages 模板消息列表
     * @return
     * @throws Exception
     */
    List<String> send(List<WxMpTemplateMessage> wxMpTemplateMessages) throws Exception;
}
