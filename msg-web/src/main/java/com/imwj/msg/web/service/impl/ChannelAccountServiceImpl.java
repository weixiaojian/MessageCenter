package com.imwj.msg.web.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imwj.msg.common.constant.MessageCenterConstant;
import com.imwj.msg.support.dao.ChannelAccountDao;
import com.imwj.msg.support.domain.ChannelAccount;
import com.imwj.msg.web.service.ChannelAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wj
 * @create 2022-09-27 17:08
 */
@Service
public class ChannelAccountServiceImpl implements ChannelAccountService {

    @Autowired
    private ChannelAccountDao channelAccountDao;

    @Override
    public ChannelAccount save(ChannelAccount channelAccount) {
        if (channelAccount.getId() == null) {
            channelAccount.setCreated(Math.toIntExact(DateUtil.currentSeconds()));
            channelAccount.setIsDeleted(MessageCenterConstant.FALSE);
        }
        channelAccountDao.insert(channelAccount);
        return channelAccount;
    }

    @Override
    public ChannelAccount queryByChannelType(Integer channelType) {
        QueryWrapper<ChannelAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("channel_type", channelType);
        return channelAccountDao.selectOne(queryWrapper);
    }

    @Override
    public List<ChannelAccount> list() {
        return channelAccountDao.selectList(new QueryWrapper<>());
    }

    @Override
    public void deleteByIds(List<Long> idList) {
        channelAccountDao.deleteBatchIds(idList);
    }
}
