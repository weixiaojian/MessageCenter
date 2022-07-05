package com.imwj.msg.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 模板枚举信息
 * @author wj
 * @create 2022-05-07 11:24
 */
@Getter
@ToString
@AllArgsConstructor
public enum TemplateType {

    /**
     * 模板类型
     */
    CLOCKING(10, "定时类的模板(后台定时调用)"),
    REALTIME(20, "实时类的模板(接口实时调用)"),
    ;

    private Integer code;
    private String description;

}
