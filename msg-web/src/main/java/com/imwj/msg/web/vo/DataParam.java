package com.imwj.msg.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全链路 请求参数
 * @author wj
 * @create 2022-07-13 18:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataParam {
    /**
     * 传入userId查看用户的链路信息
     */
    private String userId;


    /**
     * 业务Id(数据追踪使用)
     * 生成逻辑参考 TaskInfoUtils
     */
    private Long businessId;
}