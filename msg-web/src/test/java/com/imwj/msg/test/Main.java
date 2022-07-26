package com.imwj.msg.test;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @author langao_q
 * @since 2022-01-11 15:08
 */
public class Main {

    public static void main(String[] args) {
        System.out.println((DateUtil.offsetDay(new Date(), 1).getTime()) / 1000);
    }

}
