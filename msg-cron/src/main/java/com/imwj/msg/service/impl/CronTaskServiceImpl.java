package com.imwj.msg.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.constant.XxlJobConstant;
import com.imwj.msg.entity.XxlJobGroup;
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
        //1.参数转换
        Map<String, Object> params = JSON.parseObject(JSON.toJSONString(xxlJobInfo), Map.class);
        //2.请求地址拼接
        String path = xxlJobInfo.getId() == null ? xxlAddresses + XxlJobConstant.INSERT_URL
                : xxlAddresses + XxlJobConstant.UPDATE_URL;
        HttpResponse response = null;
        try {
            //3.发起请求
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            // 插入时需要返回Id，而更新时不需要
            if (path.contains(XxlJobConstant.INSERT_URL) && response.isOk()) {
                Integer taskId = Integer.parseInt(String.valueOf(JSON.parseObject(response.body()).get("content")));
                return BasicResultVO.success(taskId);
            } else if (response.isOk()) {
                return BasicResultVO.success();
            }
        } catch (Exception e) {
            log.error("CronTaskService#saveTask fail:{}", JSON.toJSONString(response.body()));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }
        return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
    }

    @Override
    public BasicResultVO deleteCronTask(Integer taskId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", taskId);
        String path = xxlAddresses + XxlJobConstant.DELETE_URL;
        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
        if (!response.isOk()) {
            log.error("CronTaskService#deleteCronTask fail:{}", JSON.toJSONString(response.body()));
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
            log.error("CronTaskService#stopCronTask fail:{}", JSON.toJSONString(response.body()));
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
        hashMap.put("randomCode", IdUtil.fastSimpleUUID());
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

    @Override
    public BasicResultVO getGroupId(String appName, String title) {
        String path = xxlAddresses + XxlJobConstant.JOB_GROUP_PAGE_LIST;

        HashMap<String, Object> params = new HashMap<>();
        params.put("appname", appName);
        params.put("title", title);

        HttpResponse response = null;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            Integer id = JSON.parseObject(response.body()).getJSONArray("data").getJSONObject(0).getInteger("id");
            if (response.isOk() && id != null) {
                return BasicResultVO.success(id);
            }
        } catch (Exception e) {
            log.error("CronTaskService#getGroupId fail:{}", JSON.toJSONString(response.body()));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }
        return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
    }

    @Override
    public BasicResultVO createGroup(XxlJobGroup xxlJobGroup) {
        Map<String, Object> params = JSON.parseObject(JSON.toJSONString(xxlJobGroup), Map.class);
        String path = xxlAddresses + XxlJobConstant.JOB_GROUP_INSERT_URL;
        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
        if (!response.isOk()) {
            log.error("CronTaskService#createGroup fail:{}", JSON.toJSONString(response.body()));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }
        return BasicResultVO.success();
    }
}
