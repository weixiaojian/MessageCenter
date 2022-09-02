package com.imwj.msg.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.google.common.base.Throwables;
import com.imwj.msg.common.constant.MessageCenterConstant;
import com.imwj.msg.common.constant.SendAccountConstant;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.dto.account.DingDingWorkNoticeAccount;
import com.imwj.msg.common.dto.model.DingDingWorkContentModel;
import com.imwj.msg.common.enums.ChannelType;
import com.imwj.msg.common.enums.SendMessageType;
import com.imwj.msg.handler.handler.BaseHandler;
import com.imwj.msg.handler.handler.Handler;
import com.imwj.msg.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 钉钉消息自定义机器人 消息处理器
 * https://open.dingtalk.com/document/group/custom-robot-access
 * @author wj
 * @create 2022-08-17 11:41
 */
@Slf4j
@Service
public class DingDingWorkNoticeHandler extends BaseHandler implements Handler {


    @Autowired
    private AccountUtils accountUtils;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";

    public DingDingWorkNoticeHandler() {
        channelCode = ChannelType.DING_DING_WORK_NOTICE.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            DingDingWorkNoticeAccount account = accountUtils.getAccount(taskInfo.getSendAccount(), SendAccountConstant.DING_DING_WORK_NOTICE_ACCOUNT_KEY, SendAccountConstant.DING_DING_WORK_NOTICE_PREFIX, DingDingWorkNoticeAccount.class);
            OapiMessageCorpconversationAsyncsendV2Request request = assembleParam(account, taskInfo);
            String accessToken = redisTemplate.opsForValue().get(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + taskInfo.getSendAccount());
            OapiMessageCorpconversationAsyncsendV2Response response = new DefaultDingTalkClient(URL).execute(request, accessToken);
            if (response.getErrcode() == 0) {
                return true;
            }
            // 常见的错误 应当 关联至 AnchorState,由后台统一透出失败原因
            log.error("DingDingWorkNoticeHandler#handler fail!result:{},params:{}", JSON.toJSONString(response), JSON.toJSONString(taskInfo));
        } catch (Exception e) {
            log.error("DingDingWorkNoticeHandler#handler fail!{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
        }
        return false;
    }

    /**
     * 拼装参数
     *
     * @param account
     * @param taskInfo
     */
    private OapiMessageCorpconversationAsyncsendV2Request assembleParam(DingDingWorkNoticeAccount account, TaskInfo taskInfo) {
        OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
        DingDingWorkContentModel contentModel = (DingDingWorkContentModel) taskInfo.getContentModel();
        try {
            // 接收者相关
            if (MessageCenterConstant.SEND_ALL.equals(CollUtil.getFirst(taskInfo.getReceiver()))) {
                req.setToAllUser(true);
            } else {
                req.setUseridList(StringUtils.join(taskInfo.getReceiver(), StrUtil.C_COMMA));
            }
            req.setAgentId(Long.parseLong(account.getAgentId()));


            OapiMessageCorpconversationAsyncsendV2Request.Msg message = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            message.setMsgtype(SendMessageType.getDingDingWorkTypeByCode(contentModel.getSendType()));

            // 根据类型设置入参
            if (SendMessageType.TEXT.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.Text textObj = new OapiMessageCorpconversationAsyncsendV2Request.Text();
                textObj.setContent(contentModel.getContent());
                message.setText(textObj);
            }
            if (SendMessageType.IMAGE.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.Image image = new OapiMessageCorpconversationAsyncsendV2Request.Image();
                image.setMediaId(contentModel.getMediaId());
                message.setImage(image);
            }
            if (SendMessageType.VOICE.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.Voice voice = new OapiMessageCorpconversationAsyncsendV2Request.Voice();
                voice.setMediaId(contentModel.getMediaId());
                voice.setDuration(contentModel.getDuration());
                message.setVoice(voice);
            }
            if (SendMessageType.FILE.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.File file = new OapiMessageCorpconversationAsyncsendV2Request.File();
                file.setMediaId(contentModel.getMediaId());
                message.setFile(file);
            }
            if (SendMessageType.LINK.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.Link link = new OapiMessageCorpconversationAsyncsendV2Request.Link();
                link.setText(contentModel.getContent());
                link.setTitle(contentModel.getTitle());
                link.setPicUrl(contentModel.getPicUrl());
                link.setMessageUrl(contentModel.getUrl());
                message.setLink(link);
            }

            if (SendMessageType.MARKDOWN.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.Markdown markdown = new OapiMessageCorpconversationAsyncsendV2Request.Markdown();
                markdown.setText(contentModel.getContent());
                markdown.setTitle(contentModel.getTitle());
                message.setMarkdown(markdown);

            }
            if (SendMessageType.ACTION_CARD.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.ActionCard actionCard = new OapiMessageCorpconversationAsyncsendV2Request.ActionCard();
                actionCard.setTitle(contentModel.getTitle());
                actionCard.setMarkdown(contentModel.getContent());
                actionCard.setBtnOrientation(contentModel.getBtnOrientation());
                actionCard.setBtnJsonList(JSON.parseArray(contentModel.getBtns(), OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList.class));
                message.setActionCard(actionCard);

            }
            if (SendMessageType.ACTION_CARD.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.OA oa = new OapiMessageCorpconversationAsyncsendV2Request.OA();
                oa.setMessageUrl(contentModel.getUrl());
                oa.setHead(JSON.parseObject(contentModel.getHead(), OapiMessageCorpconversationAsyncsendV2Request.Head.class));
                oa.setBody(JSON.parseObject(contentModel.getBody(), OapiMessageCorpconversationAsyncsendV2Request.Body.class));
                message.setOa(oa);
            }
            req.setMsg(message);
        } catch (Exception e) {
            log.error("assembleParam fail:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return req;
    }

}
