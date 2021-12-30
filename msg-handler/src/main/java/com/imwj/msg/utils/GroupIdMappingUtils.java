package com.imwj.msg.utils;

import com.imwj.msg.domain.TaskInfo;
import com.imwj.msg.enums.ChannelType;
import com.imwj.msg.enums.MessageType;

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
                groupIds.add(channelType.getCode_en() + "." + messageType.getCode_en());
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
        String channelCode = ChannelType.getEnumByCode(taskInfo.getSendChannel()).getCode_en();
        String messageCode = MessageType.getEnumByCode(taskInfo.getMsgType()).getCode_en();
        return channelCode + "." + messageCode;
    }
}
