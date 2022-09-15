package com.imwj.msg.handler.handler.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.imwj.msg.common.constant.SendAccountConstant;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.dto.account.EnterpriseWeChatRobotAccount;
import com.imwj.msg.common.dto.model.EnterpriseWeChatRobotContentModel;
import com.imwj.msg.common.enums.ChannelType;
import com.imwj.msg.common.enums.SendMessageType;
import com.imwj.msg.handler.domain.wechat.root.EnterpriseWeChatRobotParam;
import com.imwj.msg.handler.handler.BaseHandler;
import com.imwj.msg.handler.handler.Handler;
import com.imwj.msg.support.domain.MessageTemplate;
import com.imwj.msg.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业微信群机器人 消息处理器
 * @author wj
 * @create 2022-09-15 17:22
 */
@Slf4j
@Service
public class EnterpriseWeChatRobotHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    public EnterpriseWeChatRobotHandler() {
        channelCode = ChannelType.ENTERPRISE_WE_CHAT_ROBOT.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            EnterpriseWeChatRobotAccount account = accountUtils.getAccount(taskInfo.getSendAccount(), SendAccountConstant.ENTERPRISE_WECHAT_ROBOT_ACCOUNT_KEY, SendAccountConstant.ENTERPRISE_WECHAT_ROBOT_PREFIX, EnterpriseWeChatRobotAccount.class);
            EnterpriseWeChatRobotParam enterpriseWeChatRobotParam = assembleParam(taskInfo);
            String result = HttpRequest.post(account.getWebhook()).header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                    .body(JSON.toJSONString(enterpriseWeChatRobotParam))
                    .timeout(2000)
                    .execute().body();
            JSONObject jsonObject = JSON.parseObject(result);
            if (jsonObject.getInteger("errcode") != 0) {
                return true;
            }
            log.error("EnterpriseWeChatRobotHandler#handler fail! result:{},params:{}", JSON.toJSONString(jsonObject), JSON.toJSONString(taskInfo));
        } catch (Exception e) {
            log.error("EnterpriseWeChatRobotHandler#handler fail!e:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }

    private EnterpriseWeChatRobotParam assembleParam(TaskInfo taskInfo) {
        EnterpriseWeChatRobotContentModel contentModel = (EnterpriseWeChatRobotContentModel) taskInfo.getContentModel();
        EnterpriseWeChatRobotParam param = EnterpriseWeChatRobotParam.builder()
                .msgType(SendMessageType.getEnterpriseWeChatRobotTypeByCode(contentModel.getSendType())).build();

        if (SendMessageType.TEXT.getCode().equals(contentModel.getSendType())) {
            param.setText(EnterpriseWeChatRobotParam.TextDTO.builder().content(contentModel.getContent()).build());
        }
        if (SendMessageType.MARKDOWN.getCode().equals(contentModel.getSendType())) {
            param.setMarkdown(EnterpriseWeChatRobotParam.MarkdownDTO.builder().content(contentModel.getContent()).build());
        }
        if (SendMessageType.IMAGE.getCode().equals(contentModel.getSendType())) {
            FileReader fileReader = new FileReader(contentModel.getImagePath());
            byte[] bytes = fileReader.readBytes();
            param.setImage(EnterpriseWeChatRobotParam.ImageDTO.builder().base64(Base64.encode(bytes))
                    .md5(DigestUtil.md5Hex(bytes)).build());
        }
        if (SendMessageType.FILE.getCode().equals(contentModel.getSendType())) {
            param.setFile(EnterpriseWeChatRobotParam.FileDTO.builder().mediaId(contentModel.getMediaId()).build());
        }
        if (SendMessageType.NEWS.getCode().equals(contentModel.getSendType())) {
            List<EnterpriseWeChatRobotParam.NewsDTO.ArticlesDTO> articlesDTOS = JSON.parseArray(contentModel.getArticles(), EnterpriseWeChatRobotParam.NewsDTO.ArticlesDTO.class);
            param.setNews(EnterpriseWeChatRobotParam.NewsDTO.builder().articles(articlesDTOS).build());
        }
        if (SendMessageType.TEMPLATE_CARD.getCode().equals(contentModel.getSendType())) {
            //
        }
        return param;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {

    }
}
