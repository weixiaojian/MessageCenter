package com.imwj.msg.config;

import com.imwj.msg.action.AfterParamCheckAction;
import com.imwj.msg.action.AssembleAction;
import com.imwj.msg.action.PreParamCheckAction;
import com.imwj.msg.action.SendMqAction;
import com.imwj.msg.enums.BusinessCode;
import com.imwj.msg.pipeline.BusinessProcess;
import com.imwj.msg.pipeline.ProcessController;
import com.imwj.msg.pipeline.ProcessTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 链路组装配置类
 * @author langao_q
 * @since 2021-12-30 10:06
 */
@Configuration
public class PipelineConfig {

    /**
     * 普通发送执行程
     * 1. 前置参数校验
     * 2. 组装参数
     * 3. 后置参数校验
     * 4. 发送消息至MQ
     * @return
     */
    @Bean("commonSendTemplate")
    public ProcessTemplate commonSendTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        List<BusinessProcess> processList = new ArrayList<>();
        processList.add(preParamCheckAction());
        processList.add(assembleAction());
        processList.add(afterParamCheckAction());
        processList.add(sendMqAction());
        processTemplate.setProcessList(processList);
        return processTemplate;
    }
    /**
     * 撤回消息执行程
     * 2. 组装参数
     * 4. 发送消息至MQ
     * @return
     */
    @Bean("commonRecallTemplate")
    public ProcessTemplate commonRecallTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        List<BusinessProcess> processList = new ArrayList<>();
        processList.add(assembleAction());
        processList.add(sendMqAction());
        processTemplate.setProcessList(processList);
        return processTemplate;
    }

    /**
     * pipeline流程控制器
     * 目前暂定只有 普通发送的流程
     * 后续扩展则加BusinessCode和ProcessTemplate
     * @return
     */
    @Bean
    public ProcessController processController() {
        ProcessController processController = new ProcessController();
        HashMap<String, ProcessTemplate> templateConfig = new HashMap<>(4);
        //普通发送
        templateConfig.put(BusinessCode.COMMON_SEND.getCode(), commonSendTemplate());
        //消息撤回
        templateConfig.put(BusinessCode.RECALL.getCode(), commonRecallTemplate());
        processController.setTemplateConfig(templateConfig);
        return processController;
    }

    /**
     * 责任链-前置参数校验
     * @return
     */
    @Bean
    public PreParamCheckAction preParamCheckAction(){
        return new PreParamCheckAction();
    }

    /**
     * 责任链-参数拼接
     * @return
     */
    @Bean
    public AssembleAction assembleAction(){
        return new AssembleAction();
    }

    /**
     * 责任链-后置参数校验
     * @return
     */
    @Bean
    public AfterParamCheckAction afterParamCheckAction(){
        return new AfterParamCheckAction();
    }

    /**
     * 责任链-发送消息到MQ
     * @return
     */
    @Bean
    public SendMqAction sendMqAction(){
        return new SendMqAction();
    }
}
