package com.imwj.msg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imwj.msg.domain.MessageTemplate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息模板处理dao
 * @author langao_q
 * @since 2021-12-29 16:16
 */
@Mapper
public interface MessageTemplateDao extends BaseMapper<MessageTemplate> {

}
