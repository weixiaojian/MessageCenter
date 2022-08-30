package com.imwj.msg.handler.domain.push;

import com.imwj.msg.common.domain.TaskInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wj
 * @create 2022-08-30 11:01
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PushParam {

    /**
     * 调用 接口时需要的token
     */
    private String token;

    /**
     * 调用接口时需要的appId
     */
    private String appId;

    /**
     * 消息模板的信息
     */
    private TaskInfo taskInfo;

}
