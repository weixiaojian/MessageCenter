package com.imwj.msg.config;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * 操作日志记录处理
 * @author langao_q
 * @since 2021-12-29 16:41
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private HttpServletRequest request;

    /**
     * 配置切入点表达式
     */
    @Pointcut("execution(* com.imwj.msg.controller..*(..))")
    public void webData() {
    }

    @Around(value = "webData()")
    public Object aroundWebData(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long millis = System.currentTimeMillis();
        Object result = null;
        Exception ex = null;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Exception e) {
            ex = e;
            throw e;
        } finally {
            handleLog(proceedingJoinPoint, ex, result, millis);
        }
        return result;
    }

    /**
     * 处理日志
     */
    protected void handleLog(final JoinPoint joinPoint, final Exception e, final Object resultData, long millis) {
        try {
            //请求参数
            String params = "";
            if("GET".equals(request.getMethod())){
                params = JSONUtil.toJsonStr(request.getParameterMap());
            }else{
                params = getRequestParams(joinPoint, request.getMethod());
            }
            // 返回参数
            String resStr = JSONUtil.toJsonStr(resultData);
            //请求地址
            String url = request.getRequestURI();
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            // 设置请求方式
            String method = request.getMethod();
            long ms = System.currentTimeMillis() - millis;
            String info = "【" + method + "】,耗时:[" + ms + "],url:【" + url + "】," +
                    "method:[" + className + "." + methodName + "()],param:" + params + ",";
            //返回结果
            if (e != null) {
                info += "Exception:[" + e.getMessage() + "]";
                log.info(info);
            }else if(resStr != null){
                info += "resultData:" + (resStr.length() > 600 ? "数据太长不打印" : resStr);
                log.info(info);
            } else {
                info += "resultData:{}";
                log.info(info);
            }
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取请求的参数
     *
     * @throws Exception 异常
     */
    private String getRequestParams(JoinPoint joinPoint, String method) throws Exception {
        String params = "";
        if (HttpMethod.PUT.name().equals(method) || HttpMethod.POST.name().equals(method)) {
            params = argsArrayToString(joinPoint.getArgs());
        } else {
            String decode = "";
            if (request.getQueryString() != null) {
                decode = URLDecoder.decode(request.getQueryString(), "utf-8");
            }
            params = "{" + decode + "}";
        }
        return params;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (!isFilterObject(paramsArray[i])) {
                    Object jsonObj = JSON.toJSON(paramsArray[i]);
                    params += (jsonObj != null ? jsonObj.toString() : "") + " ";
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }
}
