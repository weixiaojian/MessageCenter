package com.imwj.msg.handler.receipt;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationGetsendresultRequest;
import com.dingtalk.api.response.OapiMessageCorpconversationGetsendresultResponse;
import com.imwj.msg.common.constant.SendAccountConstant;
import com.imwj.msg.common.dto.account.DingDingWorkNoticeAccount;
import com.imwj.msg.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 拉取钉钉工作消息回执信息
 * @author wj
 * @create 2022-09-06 16:38
 */
@Component
@Slf4j
public class DingDingWorkReceipt {

    private static final String URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult";

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AccountUtils accountUtils;

    public void pull() {
        try {
            // 拉取各个账号下的回执信息
            for (int index = SendAccountConstant.START; true; index = index + SendAccountConstant.STEP) {
                // 获取账号信息
                DingDingWorkNoticeAccount account = accountUtils.getAccount(index, SendAccountConstant.DING_DING_WORK_NOTICE_ACCOUNT_KEY, SendAccountConstant.DING_DING_WORK_NOTICE_PREFIX, DingDingWorkNoticeAccount.class);
                if (account == null) {
                    break;
                }
                // 获取token
                String accessToken = redisTemplate.opsForValue().get(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + index);
                DingTalkClient client = new DefaultDingTalkClient(URL);
                // 调用回执相关接口
                OapiMessageCorpconversationGetsendresultRequest req = new OapiMessageCorpconversationGetsendresultRequest();
                req.setAgentId(Long.valueOf(account.getAgentId()));
                req.setTaskId(456L);
                OapiMessageCorpconversationGetsendresultResponse rsp = client.execute(req, accessToken);
                log.info(rsp.getBody());
            }
        } catch (Exception e) {
            log.error("DingDingWorkReceipt#pull");
        }
    }

}
