package com.imwj.msg.common.domain;

import lombok.Data;

/**
 * 统一的 REST响应对象
 * @author langao_q
 * @since 2020-07-13 14:52
 */
@Data
public class RetResult {

    /**
     * 成功
     */
    public static int SUCCESS_CODE = 200;
    /**
     * 失败
     */
    public static int FAIL_CODE = 500;
    /**
     * 业务警告
     */
    public static int WARN_CODE = 400;

    int code;
    String message;
    Object data;

    private RetResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static RetResult success() {
        return new RetResult(SUCCESS_CODE,"操作成功！",true);
    }
    public static RetResult success(Object data) {
        return new RetResult(SUCCESS_CODE,"操作成功！",data);
    }
    public static RetResult fail(String message) {
        return new RetResult(FAIL_CODE,message,"");
    }
    public static RetResult warn(String message) {
        return new RetResult(WARN_CODE,message,"");
    }
}
