package com.imwj.msg.handler.service.discard;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.imwj.msg.common.constant.MessageCenterConstant;
import com.imwj.msg.common.domain.AnchorInfo;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.enums.AnchorState;
import com.imwj.msg.support.service.ConfigService;
import com.imwj.msg.support.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 丢弃消息（模板id配置在）
 * @author langao_q
 * @since 2022-01-07 15:16
 */
@Service
public class DiscardMessageService {

    @Autowired
    private LogUtils logUtils;

    /**
     * 配置样例：key=DiscardMessageService   value=[1,2,3]
     */
    private static final String DISCARD_MESSAGE_KEY = "discardMsgIds";

    @Autowired
    private ConfigService config;

    /**
     * 丢弃消息，配置在apollo
     * @param taskInfo
     * @return
     */
    public boolean isDiscard(TaskInfo taskInfo) {
        JSONArray array = JSON.parseArray(config.getProperty(DISCARD_MESSAGE_KEY,
                MessageCenterConstant.APOLLO_DEFAULT_VALUE_JSON_ARRAY));
        //如果模板id在apollo的配置中配置了要丢弃  则丢弃该消息
        if (array.contains(String.valueOf(taskInfo.getMessageTemplateId()))) {
            logUtils.print(AnchorInfo.builder().businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).state(AnchorState.DISCARD.getCode()).build());
            return true;
        }
        return false;
    }
}