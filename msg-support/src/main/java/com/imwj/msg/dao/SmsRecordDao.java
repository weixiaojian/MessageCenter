package com.imwj.msg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imwj.msg.domain.SmsRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 短信记录信息 Mapper 接口
 * </p>
 *
 * @author langao_q
 * @since 2021-12-30
 */
@Mapper
public interface SmsRecordDao extends BaseMapper<SmsRecord> {

}
