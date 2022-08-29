package com.imwj.msg.cron.dto.getui;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 个推token响应参数
 * @author wj
 * @create 2022-08-29 17:08
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class GeTuiTokenResultDTO {

    @JSONField(name = "msg")
    private String msg;
    @JSONField(name = "code")
    private Integer code;
    @JSONField(name = "data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JSONField(name = "expire_time")
        private String expireTime;
        @JSONField(name = "token")
        private String token;
    }

}
