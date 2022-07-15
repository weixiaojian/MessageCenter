package com.imwj.msg.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author langao_q
 * @since 2022-01-11 15:08
 */
public class Main {

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        map.put("orderNumber","100001");
        map.put("status","已签收");
        System.out.println(JSONUtil.toJsonStr(map));

        Map map1 = JSONUtil.toBean(JSONUtil.toJsonStr(map), Map.class);
        System.out.println(map1);
    }

}
