package com.imwj.msg.cron.enums;

/**
 * GlueTyp 类型（默认BEAN)
 * @author wj
 * @create 2022-05-07 17:41
 */
public enum GlueTypeEnum {

    /**
     * 类型
     */
    BEAN,
    GLUE_GROOVY,
    GLUE_SHELL,
    GLUE_PYTHON,
    GLUE_PHP,
    GLUE_NODEJS,
    GLUE_POWERSHELL;

    GlueTypeEnum() {
    }
}
