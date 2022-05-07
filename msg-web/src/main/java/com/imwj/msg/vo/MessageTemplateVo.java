package com.imwj.msg.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 消息模板的Vo
 * @author wj
 * @create 2022-05-07 14:24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplateVo {
    /**
     * 返回List列表
     */
    private List<Map<String,Object>> rows;

    /**
     * 总条数
     */
    private Long count;
}
