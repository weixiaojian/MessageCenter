package com.imwj.msg.handler.domain.feishu;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 飞书机器人响应参数
 * @author wj
 * @create 2022-09-16 16:59
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class FeiShuRobotResult {

    /**
     * extra
     */
    @JSONField(name = "Extra")
    private Object extra;
    /**
     * statusCode
     */
    @JSONField(name = "StatusCode")
    private Integer statusCode;
    /**
     * statusMessage
     */
    @JSONField(name = "StatusMessage")
    private String statusMessage;
    /**
     * code
     */
    @JSONField(name = "code")
    private Integer code;
    /**
     * msg
     */
    @JSONField(name = "msg")
    private String msg;
    /**
     * data
     */
    @JSONField(name = "data")
    private DataDTO data;

    /**
     * DataDTO
     */
    @NoArgsConstructor
    @Data
    public static class DataDTO {
    }
}
