package com.imwj.msg.stream.constant;

/**
 * Flink常量配置
 * @author wj
 * @create 2022-07-13 11:23
 */
public class MsgFlinkConstant {

    /**
     * Kafka 配置信息
     * TODO 使用前配置kafka broker ip:port
     */
    public static final String GROUP_ID = "msgLogGroup";
    public static final String TOPIC_NAME = "MESSAGE_LOG";
    public static final String BROKER = "192.168.156.128:9092";


    /**
     * redis 配置
     * TODO 使用前配置redis ip:port:password
     */
    public static final String REDIS_IP = "192.168.156.128";
    public static final String REDIS_PORT = "6379";
    public static final String REDIS_PASSWORD = "123456";


    /**
     * Flink流程常量
     */
    public static final String SOURCE_NAME = "msg_kafka_source";
    public static final String FUNCTION_NAME = "msg_transfer";
    public static final String SINK_NAME = "msg_sink";
    public static final String JOB_NAME = "MsgBootStrap";
    
}
