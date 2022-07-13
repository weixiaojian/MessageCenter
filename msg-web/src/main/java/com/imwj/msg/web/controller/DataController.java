package com.imwj.msg.web.controller;

import com.imwj.msg.common.vo.BasicResultVO;
import com.imwj.msg.support.domain.MessageTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 获取数据接口（全链路追踪)
 * @author wj
 * @create 2022-07-13 18:01
 */
@Slf4j
@RestController
@RequestMapping("/messageTemplate")
@Api("获取数据接口（全链路追踪)")
public class DataController {

    /**
     * 如果Id存在，则修改
     * 如果Id不存在，则保存
     */
    @PostMapping("/data")
    @ApiOperation("/获取数据")
    public BasicResultVO getData(@RequestBody MessageTemplate messageTemplate) {
        return BasicResultVO.success();
    }

}
