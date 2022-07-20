package com.imwj.msg.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.imwj.msg.common.constant.MessageCenterConstant;
import com.imwj.msg.common.domain.SimpleAnchorInfo;
import com.imwj.msg.common.enums.AnchorState;
import com.imwj.msg.common.enums.ChannelType;
import com.imwj.msg.support.dao.MessageTemplateDao;
import com.imwj.msg.support.domain.MessageTemplate;
import com.imwj.msg.support.utils.RedisUtils;
import com.imwj.msg.support.utils.TaskInfoUtils;
import com.imwj.msg.web.constants.AmisVoConstant;
import com.imwj.msg.web.service.DataService;
import com.imwj.msg.web.vo.EchartsVo;
import com.imwj.msg.web.vo.UserTimeLineVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wj
 * @create 2022-07-19 10:55
 */
@Service
public class DataServiceImpl implements DataService {
    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private MessageTemplateDao messageTemplateDao;

    @Override
    public UserTimeLineVo getTraceUserInfo(String receiver) {
        List<String> userInfoList = redisUtils.lRange(receiver, 0, -1);
        if (CollUtil.isEmpty(userInfoList)) {
            return UserTimeLineVo.builder().items(new ArrayList<>()).build();
        }
        // 0. 按时间排序
        List<SimpleAnchorInfo> sortAnchorList = userInfoList.stream().map(s -> JSON.parseObject(s, SimpleAnchorInfo.class)).sorted((o1, o2) -> Math.toIntExact(o1.getTimestamp() - o2.getTimestamp())).collect(Collectors.toList());

        // 1. 对相同的businessId进行分类  {"businessId":[{businessId,state,timeStamp},{businessId,state,timeStamp}]}
        Map<String, List<SimpleAnchorInfo>> map = MapUtil.newHashMap();
        for (SimpleAnchorInfo simpleAnchorInfo : sortAnchorList) {
            List<SimpleAnchorInfo> simpleAnchorInfos = map.get(String.valueOf(simpleAnchorInfo.getBusinessId()));
            if (CollUtil.isEmpty(simpleAnchorInfos)) {
                simpleAnchorInfos = new ArrayList<>();
            }
            simpleAnchorInfos.add(simpleAnchorInfo);
            map.put(String.valueOf(simpleAnchorInfo.getBusinessId()), simpleAnchorInfos);
        }

        // 2. 封装vo 给到前端渲染展示
        List<UserTimeLineVo.ItemsVO> items = new ArrayList<>();
        for (Map.Entry<String, List<SimpleAnchorInfo>> entry : map.entrySet()) {
            Long messageTemplateId = TaskInfoUtils.getMessageTemplateIdFromBusinessId(Long.valueOf(entry.getKey()));
            MessageTemplate messageTemplate = messageTemplateDao.selectById(messageTemplateId);

            StringBuilder sb = new StringBuilder();
            for (SimpleAnchorInfo simpleAnchorInfo : entry.getValue()) {
                if (AnchorState.RECEIVE.getCode().equals(simpleAnchorInfo.getState())) {
                    sb.append(StrPool.CRLF);
                }
                String startTime = DateUtil.format(new Date(simpleAnchorInfo.getTimestamp()), DatePattern.NORM_DATETIME_PATTERN);
                String stateDescription = AnchorState.getDescriptionByCode(simpleAnchorInfo.getState());
                sb.append(startTime).append(StrPool.C_COLON).append(stateDescription).append("==>");
            }

            for (String detail : sb.toString().split(StrPool.CRLF)) {
                if (StrUtil.isNotBlank(detail)) {
                    UserTimeLineVo.ItemsVO itemsVO = UserTimeLineVo.ItemsVO.builder()
                            .businessId(entry.getKey())
                            .sendType(ChannelType.getEnumByCode(messageTemplate.getSendChannel()).getDescription())
                            .creator(messageTemplate.getCreator())
                            .title(messageTemplate.getName())
                            .detail(detail)
                            .build();
                    items.add(itemsVO);
                }
            }
        }
        return UserTimeLineVo.builder().items(items).build();
    }

    @Override
    public EchartsVo getTraceMessageTemplateInfo(String businessId) {
        // 1. 获取businessId并获取模板信息
        businessId = getRealBusinessId(businessId);
        MessageTemplate messageTemplate  = messageTemplateDao.selectById(TaskInfoUtils.getMessageTemplateIdFromBusinessId(Long.valueOf(businessId)));

        List<String> xAxisList = new ArrayList<>();
        List<Integer> actualData = new ArrayList<>();

        /**
         * key：state
         * value:stateCount
         */
        Map<Object, Object> anchorResult = redisUtils.hGetAll(getRealBusinessId(businessId));
        if (CollUtil.isNotEmpty(anchorResult)) {
            anchorResult = MapUtil.sort(anchorResult);
            for (Map.Entry<Object, Object> entry : anchorResult.entrySet()) {
                String description = AnchorState.getDescriptionByCode(Integer.valueOf(String.valueOf(entry.getKey())));
                xAxisList.add(description);
                actualData.add(Integer.valueOf(String.valueOf(entry.getValue())));
            }
        }


        String title = "【" + messageTemplate.getName() + "】在" + TaskInfoUtils.getDateFromBusinessId(Long.valueOf(businessId)) + "的下发情况：";

        return EchartsVo.builder()
                .title(EchartsVo.TitleVO.builder().text(title).build())
                .legend(EchartsVo.LegendVO.builder().data(Arrays.asList(AmisVoConstant.LEGEND_TITLE)).build())
                .xAxis(EchartsVo.XAxisVO.builder().data(xAxisList).build())
                .series(Arrays.asList(EchartsVo.SeriesVO.builder().name(AmisVoConstant.LEGEND_TITLE).type(AmisVoConstant.TYPE).data(actualData).build()))
                .yAxis(EchartsVo.YAxisVO.builder().build())
                .tooltip(EchartsVo.TooltipVO.builder().build())
                .build();
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
