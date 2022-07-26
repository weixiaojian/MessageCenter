package com.imwj.msg.handler.shield.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.enums.ShieldType;
import com.imwj.msg.handler.shield.ShieldService;
import com.imwj.msg.support.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;

/**
 * 屏蔽服务
 * @author wj
 * @create 2022-07-26 16:54
 */
@Slf4j
@Service
public class ShieldServiceImpl implements ShieldService {

    private static final String NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY = "night_shield_send";

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void shield(TaskInfo taskInfo) {
        /**
         * 当消息满足以下两点条件，就不进行消息发送 而是第二天早上进行发送
         * 1.当前时间小于8点
         * 2.模板消息设置了消息夜间屏蔽
         */
        if(isNight() && isNightShieldType(taskInfo.getShieldType())){
            if (ShieldType.NIGHT_SHIELD_BUT_NEXT_DAY_SEND.getCode().equals(taskInfo.getShieldType())) {
                // 将数据存入到redis中 过期时间设置为一天后
                redisUtils.lPush(NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY, JSON.toJSONString(taskInfo), (DateUtil.offsetDay(new Date(), 1).getTime()) / 1000);
            }
            // 收件人设置为空将消息过滤调
            taskInfo.setReceiver(new HashSet<>());
        }
    }

    /**
     * 根据code判断是否为夜间屏蔽类型
     */
    private boolean isNightShieldType(Integer code) {
        if (ShieldType.NIGHT_SHIELD.getCode().equals(code)
                || ShieldType.NIGHT_SHIELD_BUT_NEXT_DAY_SEND.getCode().equals(code)) {
            return true;
        }
        return false;
    }

    /**
     * 当前小时 < 8 默认就认为是凌晨(夜晚)
     * @return
     */
    private boolean isNight() {
        return Integer.valueOf(DateFormatUtils.format(new Date(), "HH")) < 8;
    }


}
