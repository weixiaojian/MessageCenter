package com.imwj.msg.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.imwj.msg.common.constant.MessageCenterConstant;
import com.imwj.msg.common.constant.SendAccountConstant;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.dto.account.DingDingRobotAccount;
import com.imwj.msg.common.dto.model.DingDingRobotContentModel;
import com.imwj.msg.common.enums.ChannelType;
import com.imwj.msg.common.enums.SendMessageType;
import com.imwj.msg.handler.domain.dingding.DingDingRobotParam;
import com.imwj.msg.handler.domain.dingding.DingDingRobotResult;
import com.imwj.msg.handler.handler.BaseHandler;
import com.imwj.msg.handler.handler.Handler;
import com.imwj.msg.support.domain.MessageTemplate;
import com.imwj.msg.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 钉钉消息自定义机器人 消息处理器
 * @author wj
 * @create 2022-07-29 11:14
 */
@Slf4j
@Service
public class DingDingRobotHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    /**
     * 初始化渠道和handler关系
     */
    public DingDingRobotHandler() {
        channelCode = ChannelType.DING_DING_ROBOT.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            DingDingRobotAccount account = accountUtils.getAccount(taskInfo.getSendAccount(), SendAccountConstant.DING_DING_ROBOT_ACCOUNT_KEY, SendAccountConstant.DING_DING_ROBOT_PREFIX, DingDingRobotAccount.class);
            DingDingRobotParam dingRobotParam = assemBleParam(taskInfo);
            String httpResult = HttpUtil.post(assembleParamUrl(account), JSONUtil.toJsonStr(dingRobotParam));
            DingDingRobotResult dingRobotResult = JSONUtil.toBean(httpResult, DingDingRobotResult.class);
            if(dingRobotResult.getErrCode() == 0){
                return true;
            }
            log.error("DingDingHandler#handler fail!result:{},params:{}", JSON.toJSONString(dingRobotResult), JSON.toJSONString(taskInfo));
        }catch (Exception e){
            log.error("DingDingHandler#handler fail!e:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }

    /**
     * 参数拼接
     * @param taskInfo
     * @return
     */
    private DingDingRobotParam assemBleParam(TaskInfo taskInfo){
        // 接收者相关
        DingDingRobotParam.AtVO atVo = DingDingRobotParam.AtVO.builder().build();
        if(MessageCenterConstant.SEND_ALL.equals(CollUtil.getFirst(taskInfo.getReceiver()))){
            atVo.setIsAtAll(true);
        }else{
            atVo.setAtUserIds(new ArrayList<>(taskInfo.getReceiver()));
        }
        // 消息类型及内容相关
        DingDingRobotContentModel contentModel = (DingDingRobotContentModel) taskInfo.getContentModel();
        DingDingRobotParam param = DingDingRobotParam.builder().at(atVo)
                .msgtype(SendMessageType.getDingDingRobotTypeByCode(contentModel.getSendType()))
                .build();
        if (SendMessageType.TEXT.getCode().equals(contentModel.getSendType())) {
            param.setText(DingDingRobotParam.TextVO.builder().content(contentModel.getContent()).build());
        }
        if (SendMessageType.MARKDOWN.getCode().equals(contentModel.getSendType())) {
            param.setMarkdown(DingDingRobotParam.MarkdownVO.builder().title(contentModel.getTitle()).text(contentModel.getContent()).build());
        }
        if (SendMessageType.LINK.getCode().equals(contentModel.getSendType())) {
            param.setLink(DingDingRobotParam.LinkVO.builder().title(contentModel.getTitle()).text(contentModel.getContent()).messageUrl(contentModel.getUrl()).picUrl(contentModel.getPicUrl()).build());
        }
        if (SendMessageType.NEWS.getCode().equals(contentModel.getSendType())) {
            List<DingDingRobotParam.FeedCardVO.LinksVO> linksVOS = JSON.parseArray(contentModel.getFeedCards(), DingDingRobotParam.FeedCardVO.LinksVO.class);
            DingDingRobotParam.FeedCardVO feedCardVO = DingDingRobotParam.FeedCardVO.builder().links(linksVOS).build();
            param.setFeedCard(feedCardVO);
        }
        if (SendMessageType.ACTION_CARD.getCode().equals(contentModel.getSendType())) {
            List<DingDingRobotParam.ActionCardVO.BtnsVO> btnsVOS = JSON.parseArray(contentModel.getBtns(), DingDingRobotParam.ActionCardVO.BtnsVO.class);
            DingDingRobotParam.ActionCardVO actionCardVO = DingDingRobotParam.ActionCardVO.builder().title(contentModel.getTitle()).text(contentModel.getContent()).btnOrientation(contentModel.getBtnOrientation()).btns(btnsVOS).build();
            param.setActionCard(actionCardVO);
        }

        return param;
    }

    /**
     * url拼接
     * @param account
     * @return
     */
    private String assembleParamUrl(DingDingRobotAccount account){
        long currentTimeMillis = System.currentTimeMillis();
        String sign = assembleSign(currentTimeMillis, account.getSecret());
        return (account.getWebhook() + "&timestamp=" + currentTimeMillis + "&sign=" + sign);
    }

    /**
     * 使用HmacSHA256算法计算签名
     *
     * @param currentTimeMillis
     * @param secret
     * @return
     */
    private String assembleSign(long currentTimeMillis, String secret) {
        String sign = "";
        try {
            String stringToSign = currentTimeMillis + String.valueOf(StrUtil.C_LF) + secret;
            Mac mac = Mac.getInstance(MessageCenterConstant.HMAC_SHA256_ENCRYPTION_ALGO);
            mac.init(new SecretKeySpec(secret.getBytes(MessageCenterConstant.CHARSET_NAME), MessageCenterConstant.HMAC_SHA256_ENCRYPTION_ALGO));
            byte[] signData = mac.doFinal(stringToSign.getBytes(MessageCenterConstant.CHARSET_NAME));
            sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), MessageCenterConstant.CHARSET_NAME);
        } catch (Exception e) {
            log.error("DingDingHandler#assembleSign fail!:{}", Throwables.getStackTraceAsString(e));
        }
        return sign;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {

    }

}
