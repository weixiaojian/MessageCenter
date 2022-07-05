package com.imwj.msg.support.pipeline;

import lombok.Data;

import java.util.List;

/**
 * 业务执行模板（把责任链的逻辑串起来）
 * @author langao_q
 * @since 2021-12-29 11:39
 */
@Data
public class ProcessTemplate {

    private List<BusinessProcess> processList;
}