package com.imwj.msg.pipeline;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.imwj.common.enums.RespStatusEnum;
import com.imwj.common.vo.BasicResultVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 流程控制器
 * @author langao_q
 * @since 2021-12-29 17:31
 */
@Data
@Slf4j
public class ProcessController {
    /**
     * 责任链list：PreParamCheckAction、AfterParamCheckAction、AssembleAction、SendMqAction
     */
    private Map<String, ProcessTemplate> templateConfig = null;


    /**
     * 执行责任链
     * @param context
     * @return
     */
    public ProcessContext process(ProcessContext context) {
        /**
         * 前置检查
         */
        if (!preCheck(context)) {
            return context;
        }
        List<BusinessProcess> processList = templateConfig.get(context.getCode()).getProcessList();
        for (BusinessProcess businessProcess : processList){
            businessProcess.process(context);
            if(context.getNeedBreak()){
                break;
            }
        }
        return context;
    }

    /**
     * 前置检查方法
     * @param context
     * @return
     */
    private Boolean preCheck(ProcessContext context) {
        //校验上下文不为空
        if(context == null){
            context = new ProcessContext();
            context.setResponse(BasicResultVO.fail(RespStatusEnum.CONTEXT_IS_NULL));
            return false;
        }
        //校验业务数据编码是否为空
        String businessCode = context.getCode();
        if (StrUtil.isBlank(businessCode)) {
            context.setResponse(BasicResultVO.fail(RespStatusEnum.BUSINESS_CODE_IS_NULL));
            return false;
        }
        //校验执行模板是否为空
        ProcessTemplate processTemplate = templateConfig.get(businessCode);
        if(processTemplate == null){
            context.setResponse(BasicResultVO.fail(RespStatusEnum.PROCESS_TEMPLATE_IS_NULL));
            return false;
        }
        //校验执行模板列表是否为空
        List<BusinessProcess> processList = processTemplate.getProcessList();
        if(CollectionUtil.isEmpty(processList)){
            context.setResponse(BasicResultVO.fail(RespStatusEnum.PROCESS_LIST_IS_NULL));
            return false;
        }
        return true;
    }
}
