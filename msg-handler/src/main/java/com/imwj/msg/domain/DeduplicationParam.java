package com.imwj.msg.domain;

import com.imwj.msg.enums.AnchorState;
import lombok.Builder;
import lombok.Data;

/**
 * 去重服务所需要的参数
 * @author langao_q
 * @since 2021-12-31 11:39
 */
@Data
@Builder
public class DeduplicationParam {


    /**
     * TaskIno信息
     */
    private TaskInfo taskInfo;

    /**
     * 去重时间
     * 单位：秒
     */
    private Long deduplicationTime;

    /**
     * 需达到的次数去重
     */
    private Integer countNum;

    /**
     * 标识属于哪种去重
     */
    private AnchorState anchorState;


}
