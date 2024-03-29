package com.imwj.msg.handler.handler.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.imwj.msg.common.constant.SendAccountConstant;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.dto.account.WechatOfficialAccount;
import com.imwj.msg.common.dto.model.OfficialAccountsContentModel;
import com.imwj.msg.common.enums.ChannelType;
import com.imwj.msg.handler.handler.BaseHandler;
import com.imwj.msg.handler.handler.Handler;
import com.imwj.msg.support.domain.MessageTemplate;
import com.imwj.msg.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 公众号模板消息处理Handler
 * @author wj
 * @create 2022-07-15 10:58
 */
@Component
@Slf4j
public class OfficialAccountHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    /**
     * 初始化渠道和handler关系
     */
    public OfficialAccountHandler() {
        channelCode = ChannelType.OFFICIAL_ACCOUNT.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        List<WxMpTemplateMessage> mpTemplateMessages = buildTemplateMsg(taskInfo);
        // 微信模板消息需要记录相应结果
        try {
            WechatOfficialAccount wechatOfficialAccount = accountUtils.getAccount(taskInfo.getSendAccount(),
                    SendAccountConstant.WECHAT_OFFICIAL_ACCOUNT_KEY,
                    SendAccountConstant.WECHAT_OFFICIAL_PREFIX,
                    WechatOfficialAccount.class);
            WxMpService wxMpService = initService(wechatOfficialAccount);
            ArrayList<String> messageIds = new ArrayList<>(mpTemplateMessages.size());
            for(WxMpTemplateMessage wxMpTemplateMessage : mpTemplateMessages){
                String msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
                messageIds.add(msgId);
            }
            log.info("OfficialAccountHandler#handler successfully messageIds:{}", messageIds);
            return true;
        }catch (Exception e){
            log.error("OfficialAccountHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }

    /**
     * 通过taskInfo构建微信模板消息
     * @param taskInfo
     * @return
     */
    private List<WxMpTemplateMessage> buildTemplateMsg(TaskInfo taskInfo){
        // 需要是关注公众号用户的openId
        Set<String> receiver = taskInfo.getReceiver();
        Long messageTemplateId = taskInfo.getMessageTemplateId();
        // 微信模板消息可以关联到系统业务 通过接口查询
        String templateId = getRealWxMpTemplateId(messageTemplateId);
        ArrayList<WxMpTemplateMessage> wxMpTemplateMessages = new ArrayList<>(receiver.size());
        OfficialAccountsContentModel contentModel = (OfficialAccountsContentModel) taskInfo.getContentModel();
        String url = contentModel.getUrl();
        // content转换成map格式
        Map<String, String> param = JSONUtil.toBean(contentModel.getContent(), Map.class);
        // 构建微信模板消息
        for(String openId : receiver){
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(openId)
                    .templateId(templateId)
                    .url(url)
                    .build();
            // WxMpTemplateData 对应模板消息 键 -- 值 -- color
            param.forEach((k, v) -> templateMessage.addData(new WxMpTemplateData(k, v)));
            wxMpTemplateMessages.add(templateMessage);
        }
        return wxMpTemplateMessages;
    }

    /**
     * 根据模板id获取真实的微信模板id
     * TODO 此处暂返回固定值：peaVpld-GClemLN97pSGf6uhIoa3In2MHcTMhYiZs9c
     * @return
     */
    private String getRealWxMpTemplateId(Long messageTemplateId){
        return "peaVpld-GClemLN97pSGf6uhIoa3In2MHcTMhYiZs9c";
    }

    /**
     * 初始化微信服务号
     */
    public WxMpService initService(WechatOfficialAccount wechatOfficialAccount){
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(wechatOfficialAccount.getAppId());
        config.setSecret(wechatOfficialAccount.getSecret());
        config.setToken(wechatOfficialAccount.getToken());
        config.setAesKey(wechatOfficialAccount.getAesKey());
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {

    }
}
