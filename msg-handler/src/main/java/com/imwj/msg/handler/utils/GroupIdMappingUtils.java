package com.imwj.msg.handler.utils;


import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.enums.ChannelType;
import com.imwj.msg.common.enums.MessageType;

import java.util.ArrayList;
import java.util.List;

/**
 * groupId 标识着每一个消费者组
 * 消费者组id = ChannelType.code_en + MessageType.code_en
 * @author langao_q
 * @since 2021-12-30 11:00
 */
public class GroupIdMappingUtils {
    /**
     * 获取所有的消费者组id
     * @return
     */
    public static List<String> getAllGroupIds() {
        List<String> groupIds = new ArrayList<>();
        for (ChannelType channelType : ChannelType.values()) {
            for (MessageType messageType : MessageType.values()) {
                groupIds.add(channelType.getCodeEn() + "." + messageType.getCodeEn());
            }
        }
        return groupIds;
    }

    /**
     * 根据TaskInfo获取消费者组id
     * @param taskInfo
     * @return
     */
    public static String getGroupIdByTaskInfo(TaskInfo taskInfo) {
        String channelCode = ChannelType.getEnumByCode(taskInfo.getSendChannel()).getCodeEn();
        String messageCode = MessageType.getEnumByCode(taskInfo.getMsgType()).getCodeEn();
        return channelCode + "." + messageCode;
    }
}
