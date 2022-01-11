package com.imwj.msg.util;

import cn.hutool.core.date.DateUtil;
import com.imwj.msg.constant.AustinConstant;

import java.util.Date;

/**
 * 模板消息数据工具类
 * @author langao_q
 * @since 2021-12-29 19:11
 */
public class TaskInfoUtils {
    private static int TYPE_FLAG = 1000000;

    /**
     * 生成BusinessId
     * 模板类型+模板ID+当天日期
     * (固定16位)
     */
    public static Long generateBusinessId(Long templateId, Integer templateType) {
        Integer today = Integer.valueOf(DateUtil.format(new Date(), AustinConstant.YYYYMMDD));
        return Long.valueOf(String.format("%d%s", templateType * TYPE_FLAG + templateId, today));
    }

    /**
     * 对url添加平台参数（用于追踪数据)
     */
    public static String generateUrl(String url, Long templateId, Integer templateType) {
        url = url.trim();
        Long businessId = generateBusinessId(templateId, templateType);
        if (url.indexOf('?') == -1) {
            return url + "?track_code_bid=" + businessId;
        } else {
            return url + "&track_code_bid=" + businessId;
        }
    }
}
