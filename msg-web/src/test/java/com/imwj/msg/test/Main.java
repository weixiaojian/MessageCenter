package com.imwj.msg.test;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.dto.EnterpriseWeChatContentModel;

/**
 * @author langao_q
 * @since 2022-01-11 15:08
 */
public class Main {

    public static void main(String[] args) {
        EnterpriseWeChatContentModel text = EnterpriseWeChatContentModel.builder().messageType("10").content("text").build();
        TaskInfo taskInfo = TaskInfo.builder().contentModel(text).build();
        String s1 = JSONUtil.toJsonStr(taskInfo);
        System.out.println(s1);

        TaskInfo s2 = JSONUtil.toBean(s1, TaskInfo.class);
        System.out.println(s2);

        TaskInfo s3 = JSON.parseObject(s1, TaskInfo.class);
        System.out.println(s3);

    }

}
