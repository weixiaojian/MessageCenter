package com.imwj.msg.api.impl.config;

import com.imwj.msg.api.enums.BusinessCode;
import com.imwj.msg.api.impl.action.AfterParamCheckAction;
import com.imwj.msg.api.impl.action.AssembleAction;
import com.imwj.msg.api.impl.action.PreParamCheckAction;
import com.imwj.msg.api.impl.action.SendMqAction;
import com.imwj.msg.support.pipeline.BusinessProcess;
import com.imwj.msg.support.pipeline.ProcessController;
import com.imwj.msg.support.pipeline.ProcessTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 链路组装配置类
 * @author langao_q
 * @since 2021-12-30 10:06
 */
@Configuration
public class PipelineConfig {

    @Autowired
    private PreParamCheckAction preParamCheckAction;
    @Autowired
    private AssembleAction assembleAction;
    @Autowired
    private AfterParamCheckAction afterParamCheckAction;
    @Autowired
    private SendMqAction sendMqAction;

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
        processTemplate.setProcessList(Arrays.asList(preParamCheckAction, assembleAction,
                afterParamCheckAction, sendMqAction));
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
        processTemplate.setProcessList(Arrays.asList(assembleAction, sendMqAction));
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

}
