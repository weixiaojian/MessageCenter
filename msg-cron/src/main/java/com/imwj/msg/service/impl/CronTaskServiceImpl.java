package com.imwj.msg.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.constant.XxlJobConstant;
import com.imwj.msg.entity.XxlJobInfo;
import com.imwj.msg.enums.RespStatusEnum;
import com.imwj.msg.service.CronTaskService;
import com.imwj.msg.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @create 2022-05-07 17:48
 */
@Slf4j
@Service
public class CronTaskServiceImpl implements CronTaskService {

    @Value("${xxl.job.admin.username}")
    private String xxlUserName;

    @Value("${xxl.job.admin.password}")
    private String xxlPassword;

    @Value("${xxl.job.admin.addresses}")
    private String xxlAddresses;

    @Override
    public BasicResultVO saveCronTask(XxlJobInfo xxlJobInfo) {
        Map<String, Object> params = JSON.parseObject(JSON.toJSONString(xxlJobInfo), Map.class);

        String path;
        if (xxlJobInfo.getId() != null) {
            path = xxlAddresses + XxlJobConstant.INSERT_URL;
        } else {
            path = xxlAddresses + XxlJobConstant.UPDATE_URL;
        }

        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
        if (!response.isOk()) {
            log.error("TaskService#saveTask fail:{}", JSON.toJSONString(response.body()));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }
        return BasicResultVO.success(JSON.toJSONString(response.body()));
    }

    @Override
    public BasicResultVO deleteCronTask(Integer taskId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", taskId);
        String path = xxlAddresses + XxlJobConstant.DELETE_URL;
        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
        if (!response.isOk()) {
            log.error("TaskService#deleteCronTask fail:{}", JSON.toJSONString(response.body()));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }
        return BasicResultVO.success();
    }

    @Override
    public BasicResultVO startCronTask(Integer taskId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", taskId);
        String path = xxlAddresses + XxlJobConstant.RUN_URL;
        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
        if (!response.isOk()) {
            log.error("TaskService#startCronTask fail:{}", JSON.toJSONString(response.body()));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }
        return BasicResultVO.success();
    }

    @Override
    public BasicResultVO stopCronTask(Integer taskId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", taskId);

        String path = xxlAddresses + XxlJobConstant.STOP_URL;
        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
        if (!response.isOk()) {
            log.error("TaskService#stopCronTask fail:{}", JSON.parseObject(response.body()));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }
        return BasicResultVO.success();
    }

    /**
     * 获取xxl cookie
     *
     * @return String
     */
    private String getCookie() {
        String path = xxlAddresses + XxlJobConstant.LOGIN_URL;
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("userName", xxlUserName);
        hashMap.put("password", xxlPassword);
        log.info("TaskService#getCookie params：{}", hashMap);

        HttpResponse response = HttpRequest.post(path).form(hashMap).execute();
        if (response.isOk()) {
            List<HttpCookie> cookies = response.getCookies();
            StringBuilder sb = new StringBuilder();
            for (HttpCookie cookie : cookies) {
                sb.append(cookie.toString());
            }
            return sb.toString();
        }
        log.error("TaskService#getCookie fail:{}", JSON.parseObject(response.body()));
        return null;
    }
}
