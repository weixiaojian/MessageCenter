package com.imwj.msg.stream.sink;

import com.alibaba.fastjson.JSON;
import com.imwj.msg.common.domain.AnchorInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * @author wj
 * @create 2022-07-13 10:49
 */
@Slf4j
public class MsgSink extends RichSinkFunction<AnchorInfo> {

    @Override
    public void invoke(AnchorInfo value, SinkFunction.Context context) throws Exception {

        log.error("sink consume value:{}", JSON.toJSONString(value));

    }
}