package com.imwj.msg.web.service.impl;

import com.imwj.msg.common.constant.MessageCenterConstant;
import com.imwj.msg.common.enums.AnchorState;
import com.imwj.msg.support.dao.MessageTemplateDao;
import com.imwj.msg.support.domain.MessageTemplate;
import com.imwj.msg.support.utils.RedisUtils;
import com.imwj.msg.support.utils.TaskInfoUtils;
import com.imwj.msg.web.service.DataService;
import com.imwj.msg.web.vo.EchartsVo;
import com.imwj.msg.web.vo.TimeLineItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wj
 * @create 2022-07-19 10:55
 */
@Service
public class DataServiceImpl implements DataService {
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private MessageTemplateDao messageTemplateDao;

    @Override
    public TimeLineItemVo getTraceUserInfo(String receiver) {
        return null;
    }

    @Override
    public EchartsVo getTraceMessageTemplateInfo(String businessId) {
        /**
         * key：state
         * value:stateCount
         */
        Map<Object, Object> anchorResult = redisUtils.hGetAll(getRealBusinessId(businessId));
        List<Integer> stateList = anchorResult.entrySet().stream().map(objectObjectEntry -> Integer.valueOf(String.valueOf(objectObjectEntry.getKey()))).collect(Collectors.toList());
        for (AnchorState value : AnchorState.values()) {

        }

        return null;
    }

    /**
     * 如果传入的是模板ID，则生成【当天】的businessId进行查询
     * 如果传入的是businessId，则按默认的businessId进行查询
     * 判断是否为businessId则判断长度是否为16位（businessId长度固定16)
     */
    private String getRealBusinessId(String businessId) {
        if (MessageCenterConstant.BUSINESS_ID_LENGTH == businessId.length()) {
            return businessId;
        }
        MessageTemplate messageTemplate = messageTemplateDao.selectById(Long.valueOf(businessId));
        if (messageTemplate != null) {
            return String.valueOf(TaskInfoUtils.generateBusinessId(messageTemplate.getId(), messageTemplate.getTemplateType()));
        }
        return businessId;
    }

}
