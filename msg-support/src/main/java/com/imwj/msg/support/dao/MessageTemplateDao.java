package com.imwj.msg.support.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.imwj.msg.common.page.PageVo;
import com.imwj.msg.support.domain.MessageTemplate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息模板处理dao
 * @author langao_q
 * @since 2021-12-29 16:16
 */
@Mapper
public interface MessageTemplateDao extends BaseMapper<MessageTemplate> {

    /**
     * 分页查询模板数据
     * @param page
     * @param pageVo
     * @return
     */
    IPage<MessageTemplate> findMessageTemplatePage(IPage<MessageTemplate> page, PageVo pageVo);

}
