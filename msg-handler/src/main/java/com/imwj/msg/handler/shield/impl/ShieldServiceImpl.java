package com.imwj.msg.handler.shield.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.imwj.msg.common.domain.AnchorInfo;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.enums.AnchorState;
import com.imwj.msg.common.enums.ShieldType;
import com.imwj.msg.handler.shield.ShieldService;
import com.imwj.msg.support.utils.LogUtils;
import com.imwj.msg.support.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Autowired
    private LogUtils logUtils;

    @Override
    public void shield(TaskInfo taskInfo) {

        if (ShieldType.NIGHT_NO_SHIELD.getCode().equals(taskInfo.getShieldType())) {
            return;
        }

        /**
         * 当消息满足以下两点条件，就不进行消息发送 而是第二天早上进行发送
         * 1.当前时间小于8点
         * 2.模板消息设置了消息夜间屏蔽
         */
        if(isNight()){
            // 夜间屏蔽埋点
            if (ShieldType.NIGHT_SHIELD.getCode().equals(taskInfo.getShieldType())) {
                logUtils.print(AnchorInfo.builder().state(AnchorState.NIGHT_SHIELD.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
            }
            // 夜间屏蔽(次日发送)：数据存到redis + 埋点
            if (ShieldType.NIGHT_SHIELD_BUT_NEXT_DAY_SEND.getCode().equals(taskInfo.getShieldType())) {
                redisUtils.lPush(NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY, JSON.toJSONString(taskInfo, new SerializerFeature[]{SerializerFeature.WriteClassName}),
                        (DateUtil.offsetDay(new Date(), 1).getTime() / 1000) - DateUtil.currentSeconds());
                logUtils.print(AnchorInfo.builder().state(AnchorState.NIGHT_SHIELD_NEXT_SEND.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
            }
            taskInfo.setReceiver(new HashSet<>());
        }
    }

    /**
     * 当前小时 < 8 默认就认为是凌晨(夜晚)
     * @return
     */
    private boolean isNight() {
        return LocalDateTime.now().getHour() < 8;
    }


}
