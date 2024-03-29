package com.imwj.msg.handler.deduplication;

import com.alibaba.fastjson.annotation.JSONField;
import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.common.enums.AnchorState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 去重服务所需要的参数
 * @author langao_q
 * @since 2021-12-31 11:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeduplicationParam {


    /**
     * TaskIno信息
     */
    private TaskInfo taskInfo;

    /**
     * 去重时间
     * 单位：秒
     */
    @JSONField(name = "time")
    private Long deduplicationTime;

    /**
     * 需达到的次数去重
     */
    @JSONField(name = "num")
    private Integer countNum;

    /**
     * 标识属于哪种去重
     */
    private AnchorState anchorState;


}
