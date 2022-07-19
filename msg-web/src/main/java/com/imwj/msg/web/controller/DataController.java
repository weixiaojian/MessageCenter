package com.imwj.msg.web.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.common.enums.RespStatusEnum;
import com.imwj.msg.common.vo.BasicResultVO;
import com.imwj.msg.support.domain.MessageTemplate;
import com.imwj.msg.support.utils.RedisUtils;
import com.imwj.msg.web.service.DataService;
import com.imwj.msg.web.vo.DataParam;
import com.imwj.msg.web.vo.EchartsVo;
import com.imwj.msg.web.vo.UserTimeLineVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 获取数据接口（全链路追踪)
 * @author wj
 * @create 2022-07-13 18:01
 */
@Slf4j
@RestController
@RequestMapping("/trace")
@Api("获取数据接口（全链路追踪)")
public class DataController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private DataService dataService;

    /**
     * 获取数据Flink存到redis中的清洗数据
     */
    @PostMapping("/user")
    @ApiOperation("/获取【当天】用户接收消息的全链路数据")
    public BasicResultVO getData(@RequestBody DataParam dataParam) {
        UserTimeLineVo traceUserInfo = dataService.getTraceUserInfo(dataParam.getReceiver());
        return BasicResultVO.success(traceUserInfo);
    }

    @PostMapping("/messageTemplate")
    @ApiOperation("/获取消息模板全链路数据")
    public BasicResultVO getMessageTemplateData(@RequestBody DataParam dataParam) {
        EchartsVo echartsVo = EchartsVo.builder().build();
        if (StrUtil.isNotBlank(dataParam.getBusinessId())) {
            echartsVo = dataService.getTraceMessageTemplateInfo(dataParam.getBusinessId());
        }
        return new BasicResultVO<>(RespStatusEnum.SUCCESS, echartsVo);
    }

}
