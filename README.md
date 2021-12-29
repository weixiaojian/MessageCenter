# 消息发送中心
## kafka安装
* docker安装：[http://imwj.club/article/95](http://imwj.club/article/95)
* Docker compose环境安装
    1. 下载当前稳定版
    ```
    sudo curl -L "https://github.com/docker/compose/releases/download/1.24.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    ```
    2. 将可执行权限应用于二进制文件/创建软链：
                      
    ```
    sudo chmod +x /usr/local/bin/docker-compose
    sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
    ```
    3. 测试是否安装成功
       
    ```
    docker-compose --version
    ```
* 新建搭建kafka环境的docker-compose.yml文件，文件中的TODO的ip需要换成自己服务器的外网ip，同时要打开服务器的2181、9092、9000端口
```
version: '3'
services:
  zookepper:
    image: wurstmeister/zookeeper                    # 原镜像`wurstmeister/zookeeper`
    container_name: zookeeper                        # 容器名为'zookeeper'
    volumes:                                         # 数据卷挂载路径设置,将本机目录映射到容器目录
      - "/etc/localtime:/etc/localtime"
    ports:                                           # 映射端口
      - "2181:2181"

  kafka:
    image: wurstmeister/kafka                                # 原镜像`wurstmeister/kafka`
    container_name: kafka                                    # 容器名为'kafka'
    volumes:                                                 # 数据卷挂载路径设置,将本机目录映射到容器目录
      - "/etc/localtime:/etc/localtime"
    environment:                                                       # 设置环境变量,相当于docker run命令中的-e
      KAFKA_BROKER_ID: 0                                               # 在kafka集群中，每个kafka都有一个BROKER_ID来区分自己
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://ip:9092 # TODO 将kafka的地址端口注册给zookeeper
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092                        # 配置kafka的监听端口
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181                # zookeeper地址
      KAFKA_CREATE_TOPICS: "hello_world"
    ports:                              # 映射端口
      - "9092:9092"
    depends_on:                         # 解决容器依赖启动先后问题
      - zookepper

  kafka-manager:
    image: sheepkiller/kafka-manager                         # 原镜像`sheepkiller/kafka-manager`
    container_name: kafka-manager                            # 容器名为'kafka-manager'
    environment:                        # 设置环境变量,相当于docker run命令中的-e
      ZK_HOSTS: zookeeper:2181  #  zookeeper地址
      APPLICATION_SECRET: xxxxx
      KAFKA_MANAGER_AUTH_ENABLED: "true"  # 开启kafka-manager权限校验
      KAFKA_MANAGER_USERNAME: admin       # 登陆账户
      KAFKA_MANAGER_PASSWORD: 123456      # 登陆密码
    ports:                              # 映射端口
      - "9000:9000"
    depends_on:                         # 解决容器依赖启动先后问题
      - kafka
```
* 编译docker-compose.yml下载zookepper、kafka的镜像并运行（docker-compose.yml文件目录下执行）
```
docker-compose up -d
```
* 查看镜像运行情况 并进入kafka容器
```
docker ps 
docker exec -it kafka sh
```
* 创建topic
```
$KAFKA_HOME/bin/kafka-topics.sh --create --topic MESSAGE_CENTER --partitions 4 --zookeeper zookeeper:2181 --replication-factor 1 
```
* 查询topic信息
```
$KAFKA_HOME/bin/kafka-topics.sh --zookeeper zookeeper:2181 --describe --topic MESSAGE_CENTER
```
* 启动消费者
```
$KAFKA_HOME/bin/kafka-console-consumer.sh --bootstrap-server kafka:9092 --from-beginning --topic MESSAGE_CENTER
```
* 启动生产者
```
$KAFKA_HOME/bin/kafka-console-producer.sh --topic=MESSAGE_CENTER --broker-list kafka:9092
```