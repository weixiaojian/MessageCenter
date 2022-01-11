package com.imwj.msg;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.imwj.msg.domain.LogParam;

/**
 * @author langao_q
 * @since 2022-01-11 15:08
 */
public class Main {

    public static void main(String[] args) {
        try {
            LogParam logParam = new LogParam();
            String s = SecureUtil.md5(logParam.getBizType());
            System.out.println(s);

            String str = null;
            str.equals("");
        }catch (Exception e){
            System.out.println(e.getMessage());
            String sub = StrUtil.sub(e.getMessage(), 0, 20);
            System.out.println(sub);
        }
    }

}
