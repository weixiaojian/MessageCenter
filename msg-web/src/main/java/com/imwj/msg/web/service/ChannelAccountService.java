package com.imwj.msg.web.service;

import com.imwj.msg.support.domain.ChannelAccount;

/**
 * 渠道账号service
 * @author wj
 * @create 2022-09-27 17:08
 */
public interface ChannelAccountService {
    /**
     * 保存渠道账号数据
     * @param channelAccount
     * @return
     */
    ChannelAccount save(ChannelAccount channelAccount);

    /**
     * 根据类型查询渠道账号数据
     * @param channelType
     * @return
     */
    ChannelAccount queryByChannelType(Integer channelType);
}
