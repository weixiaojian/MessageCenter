# GrayLog安装
* 新建docker-compose.yml文件，打开服务器的6379端口
```
version: '3'
services:
    mongo:
      image: mongo:4.2
      networks:
        - graylog
      environment:
        - TZ=Asia/Shanghai 
    elasticsearch:
      image: docker.elastic.co/elasticsearch/elasticsearch-oss:7.10.2
      environment:
        - http.host=0.0.0.0
        - transport.host=localhost
        - network.host=0.0.0.0
        - "ES_JAVA_OPTS=-Dlog4j2.formatMsgNoLookups=true -Xms256m -Xmx256m"
        - TZ=Asia/Shanghai 
      ulimits:
        memlock:
          soft: -1
          hard: -1
      deploy:
        resources:
          limits:
            memory: 1g
      networks:
        - graylog
    graylog:
      image: graylog/graylog:4.2
      environment:
        - GRAYLOG_PASSWORD_SECRET=somepasswordpepper
        - GRAYLOG_ROOT_PASSWORD_SHA2=8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918
        - GRAYLOG_HTTP_EXTERNAL_URI=http://192.168.31.129:9009/ # 这里注意要改ip
        - TZ=Asia/Shanghai 
      entrypoint: /usr/bin/tini -- wait-for-it elasticsearch:9200 --  /docker-entrypoint.sh
      networks:
        - graylog
      restart: always
      depends_on:
        - mongo
        - elasticsearch
      ports:
        - 9009:9000
        - 1514:1514
        - 1514:1514/udp
        - 12201:12201
        - 12201:12201/udp
networks:
    graylog:
      driver: bridge
```
* 编译docker-compose.yml下载redis的镜像并运行（docker-compose.yml文件目录下执行）
```
docker-compose up -d
```
* 查看镜像运行情况 并进入redis容器
```
docker ps 
docker exec -it 容器ID /bin/bash

#容器不停止退出：ctrl+P+Q
```
* 修改GrayLog默认时区
```
配置目录：/usr/share/graylog/data/config/graylog.conf

修改参数：root_timezone = Asia/Shanghai
```

* 访问地址：[http://192.168.31.129:9009/](http://192.168.31.129:9009/)，账号/密码：admin/admin

* inputs配置：System > inputs > GELF UDP > Launch new input > 填写Title > Show receuved messages

# 集成到SpringBoot项目中
* pom依赖
```
    <!--graylog-->
    <dependency>
        <groupId>de.siegmar</groupId>
        <artifactId>logback-gelf</artifactId>
        <version>3.0.0</version>
    </dependency>
```
* logback.xml配置
```
    <appender name="GELF" class="de.siegmar.logbackgelf.GelfUdpAppender">
        <!-- Graylog服务的地址 -->
        <graylogHost>192.168.31.129</graylogHost>
        <!-- UDP Input端口 -->
        <graylogPort>12201</graylogPort>
        <!-- 最大GELF数据块大小（单位：字节），508为建议最小值，最大值为65467 -->
        <maxChunkSize>508</maxChunkSize>
        <!-- 是否使用压缩 -->
        <useCompression>true</useCompression>
        <encoder class="de.siegmar.logbackgelf.GelfEncoder">
            <!-- 是否发送原生的日志信息 -->
            <includeRawMessage>false</includeRawMessage>
            <includeMarker>true</includeMarker>
            <includeMdcData>true</includeMdcData>
            <includeCallerData>false</includeCallerData>
            <includeRootCauseData>false</includeRootCauseData>
            <!-- 是否发送日志级别的名称，否则默认以数字代表日志级别 -->
            <includeLevelName>true</includeLevelName>
            <shortPatternLayout class="ch.qos.logback.classic.PatternLayout">
                <pattern>%m%nopex</pattern>
            </shortPatternLayout>
            <fullPatternLayout class="ch.qos.logback.classic.PatternLayout">
                <pattern>%d - [%thread] %-5level %logger{35} - %msg%n</pattern>
            </fullPatternLayout>

            <!-- 配置应用名称（服务名称），通过staticField标签可以自定义一些固定的日志字段 -->
            <staticField>app_name:MessageCenter</staticField>
        </encoder>
    </appender>
    
    <root level="info">
        <appender-ref ref="GELF"/>
    </root>
```

* 相关查询语法：
  1. 根据字段精确查询：full_message:"6666"
  2. 根据日志级别查询：level_name:"ERROR"
  3. 组合查询：level_name:"INFO" AND full_message:"6666"