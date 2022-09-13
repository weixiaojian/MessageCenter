package com.imwj.msg.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.google.common.base.Throwables;
import com.imwj.msg.common.constant.MessageCenterConstant;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.dto.model.SmsContentModel;
import com.imwj.msg.common.enums.ChannelType;
import com.imwj.msg.handler.domain.sms.MessageTypeSmsConfig;
import com.imwj.msg.handler.domain.sms.SmsParam;
import com.imwj.msg.handler.handler.BaseHandler;
import com.imwj.msg.handler.handler.Handler;
import com.imwj.msg.handler.script.SmsScriptHolder;
import com.imwj.msg.support.dao.SmsRecordDao;
import com.imwj.msg.support.domain.MessageTemplate;
import com.imwj.msg.support.domain.SmsRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * 短信处理Handler
 * @author langao_q
 * @since 2021-12-30 15:48
 */
@Slf4j
@Component
public class SmsHandler extends BaseHandler implements Handler {

    /**
     * 初始化渠道和handler关系
     */
    public SmsHandler() {
        channelCode = ChannelType.SMS.getCode();
    }

    @Resource
    private SmsRecordDao smsRecordDao;

    @Autowired
    private SmsScriptHolder smsScriptHolder;

    @ApolloConfig("message.center")
    private Config config;

    @Override
    public boolean handler(TaskInfo taskInfo) {
        //1.调用腾讯接口发送短信
        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(getSmsContent(taskInfo))
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .build();

        try {
            /**
             * 1.动态配置做负载均衡
             * 2.指定厂商发送短信
             */
            // 获取负载配置：这里会返回两个handler 优先用第一个处理，如果第一个成功return结束，第一个失败则尝试第二个
            MessageTypeSmsConfig[] messageTypeSmsConfigs = loadBalance(getMessageTypeSmsConfig(taskInfo.getMsgType()));
            for(MessageTypeSmsConfig messageTypeSmsConfig : messageTypeSmsConfigs){
                List<SmsRecord> recordList = smsScriptHolder.route(messageTypeSmsConfig.getScriptName()).send(smsParam);
                if(CollUtil.isNotEmpty(recordList)){
                    for(SmsRecord smsRecord : recordList){
                        smsRecordDao.insert(smsRecord);
                        return true;
                    }
                }
            }
        }catch (Exception e){
            log.error("SmsHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
        }
        return false;
    }



    /**
     * 短信内容参数拼接
     * @param taskInfo
     * @return
     */
    private String getSmsContent(TaskInfo taskInfo) {
        SmsContentModel smsContentModel = (SmsContentModel) taskInfo.getContentModel();
        if (StrUtil.isNotBlank(smsContentModel.getUrl())) {
            return smsContentModel.getContent() + " " + smsContentModel.getUrl();
        } else {
            return smsContentModel.getContent();
        }
    }

    /**
     * 根据消息类型来获取发送渠道账号配置
     * 示例：msg_type_sms_config [{"message_type_10":[{"weights":80,"scriptName":"TencentSmsScript"},{"weights":20,"scriptName":"YunPianSmsScript"}]}]
     * @param msgType
     * @return
     */
    private List<MessageTypeSmsConfig> getMessageTypeSmsConfig(Integer msgType) {
        String apolloKey = "msg_type_sms_config";
        String messagePrefix = "message_type_";

        String property = config.getProperty(apolloKey, MessageCenterConstant.APOLLO_DEFAULT_VALUE_JSON_ARRAY);
        JSONArray jsonArray = JSON.parseArray(property);
        for(int i=0; i<jsonArray.size(); i++){
            JSONArray array = jsonArray.getJSONObject(i).getJSONArray(messagePrefix + msgType);
            if(CollUtil.isNotEmpty(array)){
                List<MessageTypeSmsConfig> result = JSON.parseArray(JSON.toJSONString(array), MessageTypeSmsConfig.class);
                return result;
            }
        }
        return null;
    }

    /**
     * 根据随机数来实现流量负载
     * 根据配置的权重优先走某个账号，并取出一个备份的
     * @param messageTypeSmsConfig
     * @return
     */
    private static MessageTypeSmsConfig[] loadBalance(List<MessageTypeSmsConfig> messageTypeSmsConfig) {
        int total = 0;
        for(MessageTypeSmsConfig channelConfig : messageTypeSmsConfig){
            total += channelConfig.getWeights();
        }

        // 生成一个随机数【1，total】 看落到那个区间
        Random random = new Random();
        int index = random.nextInt(total) + 1;

        MessageTypeSmsConfig supplier = null;
        MessageTypeSmsConfig supplierBack = null;
        for(int i=0; i< messageTypeSmsConfig.size(); ++i){
            if(index <= messageTypeSmsConfig.get(i).getWeights()){
                supplier = messageTypeSmsConfig.get(i);

                // 取下一个handler
                int j = (i+1) % messageTypeSmsConfig.size();
                if(i == j){
                    return new MessageTypeSmsConfig[]{supplier};
                }
                supplierBack = messageTypeSmsConfig.get(j);
                return new MessageTypeSmsConfig[]{supplier, supplierBack};
            }
            index -= messageTypeSmsConfig.get(i).getWeights();
        }
        return null;
    }

    public static void main(String[] args) {
        List<MessageTypeSmsConfig> list = new ArrayList<>();
        list.add(MessageTypeSmsConfig.builder().weights(70).scriptName("test1").build());
        list.add(MessageTypeSmsConfig.builder().weights(20).scriptName("test2").build());
        list.add(MessageTypeSmsConfig.builder().weights(10).scriptName("test3").build());

        for(int i=0; i<10; i++){
            MessageTypeSmsConfig[] messageTypeSmsConfigs = loadBalance(list);

            System.out.println(JSONUtil.toJsonStr(messageTypeSmsConfigs));
        }
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {

    }
}
