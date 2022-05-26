package com.imwj.msg.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * 每一行的csv记录
 * @author wj
 * @create 2022-05-26 16:50
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrowdInfoVo implements Serializable {

    /**
     * 接收者id
     */
    private String id;

    /**
     * 参数信息
     */
    private Map<String, String> params;
}
