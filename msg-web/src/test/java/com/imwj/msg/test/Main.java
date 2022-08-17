package com.imwj.msg.test;

import com.imwj.msg.common.enums.SendMessageType;

/**
 * @author langao_q
 * @since 2022-01-11 15:08
 */
public class Main {

    public static void main(String[] args) {
        Integer code = SendMessageType.TEXT.getCode();
        System.out.println(SendMessageType.TEXT.getCode().toString().equals("10"));
    }

}
