package com.imwj.msg.handler.handler.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.enums.ChannelType;
import com.imwj.msg.handler.handler.BaseHandler;
import com.imwj.msg.handler.handler.Handler;
import com.imwj.msg.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 企业微信发送消息handler
 * @author wj
 * @create 2022-07-25 10:38
 */
@Slf4j
@Component
public class EnterpriseWeChatHandler extends BaseHandler implements Handler {

    /**
     * 构建WxCpMessage时需要用的常量
     */
    private static final String ALL = "@all";
    private static final String DELIMITER = "|";

    /**
     * 账号信息
     */
    private static final String ENTERPRISE_WECHAT_ACCOUNT_KEY = "enterpriseWechatAccount";
    private static final String PREFIX = "enterprise_wechat_";

    @Autowired
    private AccountUtils accountUtils;

    public EnterpriseWeChatHandler() {
        channelCode = ChannelType.ENTERPRISE_WE_CHAT.getCode();
    }

    /**
     * 发送企业微信消息 TODO 待实现
     * @param taskInfo
     * @return
     */
    @Override
    public boolean handler(TaskInfo taskInfo) {
        // 1.获取企业微信账号信息
        accountUtils.getAccount(taskInfo.getSendAccount(), ENTERPRISE_WECHAT_ACCOUNT_KEY, PREFIX, new WxCpDefaultConfigImpl());

        WxCpMessage wxCpMessage = new WxCpMessage();
        try {


            return true;
        } catch (Exception e) {
            log.error("EnterpriseWeChatHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }
}
