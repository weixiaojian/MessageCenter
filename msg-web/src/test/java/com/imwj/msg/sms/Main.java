package com.imwj.msg.sms;

import com.alibaba.fastjson.JSONArray;

/**
 * @author langao_q
 * @since 2022-01-07 15:23
 */
public class Main {

    public static void main(String[] args) {
        Long id = 1L;

        JSONArray array = new JSONArray();
        array.add("1");
        array.add(2);
        array.add(3);

        if (array.contains(String.valueOf(id))) {
            System.out.println("包含");
        }else {
            System.out.println("不包含");
        }
    }

}
