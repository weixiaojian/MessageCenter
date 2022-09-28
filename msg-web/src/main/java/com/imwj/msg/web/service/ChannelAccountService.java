package com.imwj.msg.web.service;

import com.imwj.msg.support.domain.ChannelAccount;

import java.util.List;

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

    /**
     * 所有的渠道账号信息
     * @return
     */
    List<ChannelAccount> list();

    /**
     * 根据Ids删除
     * @param idList
     */
    void deleteByIds(List<Long> idList);
}
