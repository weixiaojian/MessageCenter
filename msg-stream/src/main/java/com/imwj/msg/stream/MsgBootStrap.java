package com.imwj.msg.stream;

import com.imwj.msg.common.domain.AnchorInfo;
import com.imwj.msg.stream.constant.MsgFlinkConstant;
import com.imwj.msg.stream.function.MsgFlatMapFunction;
import com.imwj.msg.stream.sink.MsgSink;
import com.imwj.msg.stream.utils.FlinkUtils;
import com.imwj.msg.stream.utils.MessageQueueUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.springframework.context.ApplicationContext;

/**
 * flink启动类
 * @author wj
 * @create 2022-07-13 10:49
 */
@Slf4j
public class MsgBootStrap {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        /**
         * 1.获取KafkaConsumer
         */
        KafkaSource<String> kafkaConsumer = MessageQueueUtils.getKafkaConsumer(MsgFlinkConstant.TOPIC_NAME, MsgFlinkConstant.GROUP_ID, MsgFlinkConstant.BROKER);
        DataStreamSource<String> kafkaSource = env.fromSource(kafkaConsumer, WatermarkStrategy.noWatermarks(), MsgFlinkConstant.SOURCE_NAME);

        /**
         * 2. 数据转换处理
         */
        SingleOutputStreamOperator<AnchorInfo> dataStream = kafkaSource.flatMap(new MsgFlatMapFunction()).name(MsgFlinkConstant.FUNCTION_NAME);

        /**
         * 3. 将实时数据多维度写入Redis(已实现)，离线数据写入hive(未实现)
         */
        dataStream.addSink(new MsgSink()).name(MsgFlinkConstant.SINK_NAME);
        env.execute(MsgFlinkConstant.JOB_NAME);
    }
}
