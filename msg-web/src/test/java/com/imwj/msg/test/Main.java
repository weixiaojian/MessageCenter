package com.imwj.msg.test;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * @author langao_q
 * @since 2022-01-11 15:08
 */
public class Main {

    public static void main(String[] args) {
        String json = "{}";
        JSONObject obj = JSONUtil.parseObj(json);
        Object test = obj.get("test");
        System.out.println(test instanceof JSONArray);
    }

}
