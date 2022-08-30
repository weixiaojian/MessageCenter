package com.imwj.msg.web.controller;

import com.imwj.msg.common.domain.RetResult;
import com.imwj.msg.common.enums.ChannelType;
import com.imwj.msg.cron.handler.RefreshDingDingAccessTokenHandler;
import com.imwj.msg.cron.handler.RefreshGeTuiAccessTokenHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 手动刷新token
 * @author wj
 * @create 2022-08-30 16:28
 */
@Api(tags = {"手动刷新token的接口"})
@RestController
public class RefreshTokenController {

    @Autowired
    private RefreshDingDingAccessTokenHandler refreshDingDingAccessTokenHandler;
    @Autowired
    private RefreshGeTuiAccessTokenHandler refreshGeTuiAccessTokenHandler;

    /**
     * 手动刷新对应发送渠道的token
     * channelType取值来源com.imwj.msg.common.enums.ChannelType
     * @return
     */
    @ApiOperation(value = "手动刷新token", notes = "钉钉/个推 token刷新")
    @RequestMapping("/refresh/{channelTypeCode}")
    public RetResult refresh(@PathVariable("channelTypeCode") String channelTypeCode){
        if(ChannelType.DING_DING_WORK_NOTICE.getCode().equals(channelTypeCode)){
            refreshDingDingAccessTokenHandler.execute();
        }
        if(ChannelType.PUSH.getCode().equals(channelTypeCode)){
            refreshGeTuiAccessTokenHandler.execute();
        }
        return RetResult.success("刷新成功！");
    }

}
