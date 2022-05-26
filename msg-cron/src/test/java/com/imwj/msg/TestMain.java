package com.imwj.msg;

import cn.hutool.http.HttpUtil;

/**
 * @author wj
 * @create 2022-05-26 18:12
 */
public class TestMain {

    public static void main(String[] args) {
        String param = "{" +
                "    \"username\":\"admin\"," +
                "    \"password\":\"123456\"," +
                "    \"randomCode\":\"123123\"" +
                "}";
        String result = HttpUtil.post("http://127.0.0.1:8899/xxl-job-admin/login", param);
        System.out.println(result);
    }

}
