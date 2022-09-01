package com.imwj.msg.handler.script;

import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;

/**
 * sms发送抽象类
 * @author wj
 * @create 2022-08-31 16:46
 */
@Slf4j
public abstract class BaseSmsScript implements SmsScript{

    @Autowired
    private SmsScriptHolder smsScriptHolder;

    @PostConstruct
    public void registerProcessScript(){
        if(ArrayUtil.isEmpty(this.getClass().getAnnotations())){
            log.error("BaseSmsScript can not find annotation!");
            return;
        }
        Annotation handlerAnnotation = null;
        // 得到当前类上的所有注解
        for(Annotation annotation : this.getClass().getAnnotations()){
            // 如果包含指定@SmsScriptHandler注解
            if(annotation instanceof  SmsScriptHandler){
                handlerAnnotation = annotation;
                break;
            }
        }
        if(handlerAnnotation == null){
            log.error("handler annotations not declared");
            return;
        }
        // 注册handler(将当前handler注册到SmsScriptHolder，名称即为@SmsScriptHandler的value)
        smsScriptHolder.putHandler(((SmsScriptHandler)handlerAnnotation).value(), this);
    }
}
