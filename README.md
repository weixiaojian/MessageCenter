# 消息发送中心
## 端口使用情况
* 项目端口：80
* zookeeper：2181；kafka：9092；kafka-manager：9000
* redis端口：6379；密码：123456
* apollo服务请求端口：8080；后台地址：http://127.0.0.1:8070；账号/密码：apollo/admin
* GrayLog服务地址：http://192.168.31.129:9009/
* Prometheus服务地址：http://ip:3000/
* Swagger服务地址：http://127.0.0.1/swagger-ui/

## 项目中的技术点
* SpringBoot、Mybatis Plus、MySql、Kafka、Redis、Elk、Prometheus
* SpringBoot：更加敏捷地开发Spring应用程序，专注于应用程序的功能，不用在Spring的配置上多花功夫（约定大于配置）
* Mybatis Plus：为简化开发而生，只做增强不做改变，引入它不会对现有工程产生影响，如丝般顺滑。
* MySql：最流行的关系型数据库
* Kafka：是一种高吞吐量的分布式发布订阅消息系统，它可以处理消费者在网站中的所有动作流数据（解耦、异步和削峰）
* Redis：基于内存亦可持久化的高性能内存读写、Key-Value数据库
* Elk：Elasticsearch , Logstash, Kibana的缩写，目前主流的一种日志系统（排查处理问题）
* Prometheus：是一个开源的服务监控系统和时间序列数据库（监控告警系统）

## 项目构成
* msg-common：公共Module 主要存放一些公共的实体、常量、枚举等等
* msg-handler：处理程序Module 业务的实际处理程序项目如：kafka消费者、调用第三方SDK等等
* msg-service-api：接口Module 供web层调用，只提供接口 不做具体实现
* msg-service-api-impl：接口实现Module，service的实际实现 如：责任链处理类、service-api接口的实现
* msg-support：业务支持Module，如：dao操作数据库、责任链串联和实际执行
* msg-web：对外暴露Module，如：web、controller等操作

## 消息发送流程
* 调用web的/sms/sendSmsTest接口(传入手机号和数据库中的模板表的id)
* 进发送消息任务模型封装、责任链上下文封装，PipelineConfig类配置责任链的内容
* 执行责任链(前置参数校验、组装参数、后置参数校验、发送消息至MQ)，将数据推送到Kafka中
* 自定义消费者信息，配置多个线程池 对应 多个kafka监听线程，这里的话根据ChannelType * MessageType来决定生成多少个线程池和kafka监听线程
* kafka监听类：MsgReceiver，多个kafka监听线程配置类：ReceiverStart，多个线程池配置类：TaskPendingHolder
* MsgReceiver监听到数据后比对groupId是否和kafka中的一致，一致就封装好Task数据 然后准备推送
* 此处并不是直接推送，根据groupId获取指定的线程池 然后线程执行Task任务，Task调用实际的实现类中的handler然后进行推送和记录发送日志
