package com.imwj.msg.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.constant.MessageCenterConstant;
import com.imwj.msg.constant.XxlJobConstant;
import com.imwj.msg.domain.MessageTemplate;
import com.imwj.msg.entity.XxlJobInfo;
import com.imwj.msg.enums.*;
import com.imwj.msg.service.CronTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * xxlJob工具类
 *
 * @author 3y
 */
@Component
public class XxlJobUtils {

    @Autowired
    private CronTaskService cronTaskService;

    /**
     * 构建xxlJobInfo信息
     *
     * @param messageTemplate
     * @return
     */
    public static XxlJobInfo buildXxlJobInfo(MessageTemplate messageTemplate) {

        // 判断是否为cron表达式
        String scheduleConf = StrUtil.EMPTY;
        String scheduleType = ScheduleTypeEnum.NONE.name();
        if (!messageTemplate.getExpectPushTime().equals(String.valueOf(MessageCenterConstant.FALSE))) {
            scheduleType = ScheduleTypeEnum.CRON.name();
            scheduleConf = messageTemplate.getExpectPushTime();
        }

        XxlJobInfo xxlJobInfo = XxlJobInfo.builder().jobGroup(1).jobDesc(messageTemplate.getName())
                .author(messageTemplate.getCreator())
                .scheduleConf(scheduleConf)
                .scheduleType(scheduleType)
                .misfireStrategy(MisfireStrategyEnum.DO_NOTHING.name())
                .executorBlockStrategy(ExecutorRouteStrategyEnum.CONSISTENT_HASH.name())
                .executorHandler(XxlJobConstant.HANDLER_NAME)
                .executorParam(JSON.toJSONString(messageTemplate))
                .executorBlockStrategy(ExecutorBlockStrategyEnum.SERIAL_EXECUTION.name())
                .executorTimeout(XxlJobConstant.TIME_OUT)
                .executorFailRetryCount(XxlJobConstant.RETRY_COUNT)
                .glueType(GlueTypeEnum.BEAN.name())
                .triggerStatus(MessageCenterConstant.FALSE)
                .glueRemark(StrUtil.EMPTY)
                .glueSource(StrUtil.EMPTY)
                .alarmEmail(StrUtil.EMPTY)
                .childJobId(StrUtil.EMPTY).build();

        if (messageTemplate.getCronTaskId() != null) {
            xxlJobInfo.setId(messageTemplate.getCronTaskId());
        }
        return xxlJobInfo;
    }

}
