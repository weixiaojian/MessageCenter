package com.imwj.msg.service;

import com.imwj.msg.domain.MessageTemplate;
import com.imwj.common.page.PageVo;

import java.util.List;

/**
 * 消息模板管理 接口
 * @author wj
 * @create 2022-05-07 11:41
 */
public interface messageTemplateService {
    /**
     * 查询未删除的模板列表（分页)
     *
     * @param pageVo
     * @return
     */
    List<MessageTemplate> queryList(PageVo pageVo);

    /**
     * 统计未删除的条数
     *
     * @return
     */
    Long count();

    /**
     * 单个 保存或者更新
     * 存在ID 更新
     * 不存在ID保存
     *
     * @param messageTemplate
     * @return
     */
    MessageTemplate saveOrUpdate(MessageTemplate messageTemplate);


    /**
     * 软删除(deleted=1)
     *
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据ID查询模板信息
     *
     * @param id
     * @return
     */
    MessageTemplate queryById(Long id);

    /**
     * 复制配置
     *
     * @param id
     */
    void copy(Long id);
}
