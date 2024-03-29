package com.imwj.msg.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.imwj.msg.common.constant.MessageCenterConstant;
import com.imwj.msg.common.constant.SendAccountConstant;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.dto.model.EnterpriseWeChatContentModel;
import com.imwj.msg.common.enums.ChannelType;
import com.imwj.msg.common.enums.SendMessageType;
import com.imwj.msg.handler.handler.BaseHandler;
import com.imwj.msg.handler.handler.Handler;
import com.imwj.msg.support.domain.MessageTemplate;
import com.imwj.msg.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpMessageServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.article.MpnewsArticle;
import me.chanjar.weixin.cp.bean.article.NewArticle;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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
    private static final String DELIMITER = "|";


    @Autowired
    private AccountUtils accountUtils;

    /**
     * 初始化渠道和handler关系
     */
    public EnterpriseWeChatHandler() {
        channelCode = ChannelType.ENTERPRISE_WE_CHAT.getCode();
    }

    /**
     * 发送企业微信消息
     * @param taskInfo
     * @return
     */
    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            // 1.获取企业微信账号信息
            WxCpDefaultConfigImpl accountConfig = accountUtils.getAccount(taskInfo.getSendAccount(), SendAccountConstant.ENTERPRISE_WECHAT_ACCOUNT_KEY, SendAccountConstant.ENTERPRISE_WECHAT_PREFIX, WxCpDefaultConfigImpl.class);
            // 2.构建WxCpServiceImpl 服务接口
            WxCpMessageServiceImpl messageService = new WxCpMessageServiceImpl(initService(accountConfig));
            // 3.发送消息
            WxCpMessageSendResult result = messageService.send(buildWxCpMessage(taskInfo, accountConfig.getAgentId()));
            // 3.1数据埋点
            buildAnchorState(result);
            return true;
        } catch (Exception e) {
            log.error("EnterpriseWeChatHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }

    /**
     * 打点相关的信息记录
     *
     * @param result
     */
    private void buildAnchorState(WxCpMessageSendResult result) {

    }

    /**
     * 初始化 WxCpServiceImpl 服务接口
     * @param config
     * @return
     */
    private WxCpService initService(WxCpDefaultConfigImpl config){
        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);
        return wxCpService;
    }

    /**
     * 构建企业微信下发消息的对象
     * @param taskInfo
     * @param agentId
     * @return
     */
    private WxCpMessage buildWxCpMessage(TaskInfo taskInfo, Integer agentId){
        WxCpMessage message = null;
        // 判断是否是发送所有成员
        String userId;
        if(MessageCenterConstant.SEND_ALL.equals(CollUtil.getFirst(taskInfo.getReceiver()))){
            userId = CollUtil.getFirst(taskInfo.getReceiver());
        }else{
            userId = StringUtils.join(taskInfo.getReceiver(), DELIMITER);
        }
        // 根据消息model来组转消息发送数据实体
        EnterpriseWeChatContentModel model = (EnterpriseWeChatContentModel) taskInfo.getContentModel();
        // 不同类型组装不同实体,
        if(SendMessageType.TEXT.getCode().equals(model.getSendType())){
            message = WxCpMessage.TEXT().content(model.getContent()).build();
        }else if (SendMessageType.IMAGE.getCode().equals(model.getSendType())) {
            message = WxCpMessage.IMAGE().mediaId(model.getMediaId()).build();
        } else if (SendMessageType.VOICE.getCode().equals(model.getSendType())) {
            message = WxCpMessage.VOICE().mediaId(model.getMediaId()).build();
        } else if (SendMessageType.VIDEO.getCode().equals(model.getSendType())) {
            message = WxCpMessage.VIDEO().mediaId(model.getMediaId()).description(model.getDescription()).title(model.getTitle()).build();
        } else if (SendMessageType.FILE.getCode().equals(model.getSendType())) {
            message = WxCpMessage.FILE().mediaId(model.getMediaId()).build();
        } else if (SendMessageType.TEXT_CARD.getCode().equals(model.getSendType())) {
            message = WxCpMessage.TEXTCARD().url(model.getUrl()).title(model.getTitle()).description(model.getDescription()).btnTxt(model.getBtnTxt()).build();
        } else if (SendMessageType.NEWS.getCode().equals(model.getSendType())) {
            List<NewArticle> newArticles = JSON.parseArray(model.getArticles(), NewArticle.class);
            message = WxCpMessage.NEWS().articles(newArticles).build();
        } else if (SendMessageType.MP_NEWS.getCode().equals(model.getSendType())) {
            List<MpnewsArticle> mpNewsArticles = JSON.parseArray(model.getMpNewsArticle(), MpnewsArticle.class);
            message = WxCpMessage.MPNEWS().articles(mpNewsArticles).build();
        } else if (SendMessageType.MARKDOWN.getCode().equals(model.getSendType())) {
            message = WxCpMessage.MARKDOWN().content(model.getContent()).build();
        } else if (SendMessageType.MINI_PROGRAM_NOTICE.getCode().equals(model.getSendType())) {
            Map contentItems = JSON.parseObject(model.getContentItems(), Map.class);
            message = WxCpMessage.newMiniProgramNoticeBuilder().appId(model.getAppId()).page(model.getPage()).emphasisFirstItem(model.getEmphasisFirstItem()).contentItems(contentItems).title(model.getTitle()).description(model.getDescription()).build();
        }
        message.setAgentId(agentId);
        message.setToUser(userId);
        return message;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {

    }
}
