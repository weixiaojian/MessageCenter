package com.imwj.msg.stream.constant;

/**
 * Flink常量配置
 * @author wj
 * @create 2022-07-13 11:23
 */
public class MsgFlinkConstant {

    /**
     * Kafka 配置信息
     * TODO 使用前需要把broker配置
     */
    public static final String GROUP_ID = "msgLogGroup";
    public static final String TOPIC_NAME = "msgLog";
    public static final String BROKER = "ip:port";


    /**
     * spring配置文件路径
     */
    public static final String SPRING_CONFIG_PATH = "classpath*:msg-spring.xml";


    /**
     * Flink流程常量
     */
    public static final String SOURCE_NAME = "msg_kafka_source";
    public static final String FUNCTION_NAME = "msg_transfer";
    public static final String SINK_NAME = "msg_sink";
    
}
