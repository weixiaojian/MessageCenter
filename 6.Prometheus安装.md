# Prometheus
> 是由 SoundCloud 开源监控告警解决方案，要接入prometheus的话，实际上就是开放接口给prometheus拉取数据，然后在web-ui下配置图形化界面进而实现监控的功能。
## 安装
* 新建一个`docker-compose.yml`文件，拉取以下镜像
  1. cadvisor 用于获取docker容器的指标
  2. node-exporter 用户获取服务器的指标
  3. grafana 监控的web-ui好用的可视化组件
  4. alertmanager 告警组件（目前暂未用到)
  5. prometheus 核心监控组件
```
version: '2'

networks:
    monitor:
        driver: bridge

services:
    prometheus:
        image: prom/prometheus
        container_name: prometheus
        hostname: prometheus
        restart: always
        volumes:
            - ./prometheus.yml:/etc/prometheus/prometheus.yml
#            - ./node_down.yml:/usr/local/etc/node_down.yml:rw
        ports:
            - "9090:9090"
        networks:
            - monitor

    alertmanager:
        image: prom/alertmanager
        container_name: alertmanager
        hostname: alertmanager
        restart: always
#        volumes:
#            - ./alertmanager.yml:/usr/local/etc/alertmanager.yml
        ports:
            - "9093:9093"
        networks:
            - monitor

    grafana:
        image: grafana/grafana
        container_name: grafana
        hostname: grafana
        restart: always
        ports:
            - "3000:3000"
        networks:
            - monitor

    node-exporter:
        image: quay.io/prometheus/node-exporter
        container_name: node-exporter
        hostname: node-exporter
        restart: always
        ports:
            - "9100:9100"
        networks:
            - monitor

    cadvisor:
        image: google/cadvisor:latest
        container_name: cadvisor
        hostname: cadvisor
        restart: always
        volumes:
            - /:/rootfs:ro
            - /var/run:/var/run:rw
            - /sys:/sys:ro
            - /var/lib/docker/:/var/lib/docker:ro
        ports:
            - "8899:8080"
        networks:
            - monitor
```
* 新建`prometheus.yml`配置文件，告诉prometheus要去哪个地址中中拉取对应的监控数据（ip填写服务器外网地址，端口与前面的镜像端口对应）
```
global:
  scrape_interval:     15s
  evaluation_interval: 15s
scrape_configs:
  - job_name: 'prometheus'
    static_configs:
    - targets: ['ip:9090']
  - job_name: 'cadvisor'
    static_configs:
    - targets: ['ip:8899']
  - job_name: 'node'
    static_configs:
    - targets: ['ip:9100']
```

* 注意镜像中将`prometheus.yml`配置文件挂载到了`/etc/prometheus/prometheus.yml`，所以`/etc/prometheus`下也要copy一份

* `docker-compose up -d`启动，下载并运行
  1. http://ip:9100/metrics( 查看服务器的指标)
  2. http://ip:8899/metrics（查看docker容器的指标） 
  3. http://ip:9090/(prometheus的原生web-ui)
  4. http://ip:3000/(Grafana开源的监控可视化组件页面，最终使用的ui页面），账号/密码：admin

## Grafana配置监控
* 数据源配置：左侧设置图标 > Configuration > Add data source > URL设置为`http://ip:9090` > 保存
* 配置对应模板：左侧+号图标 > Import > 输入模板id > Load > 输入监控名称 > Import即可
* 常用的模板ID：服务器(8919)、docker(893)、jvm(4701)、springboot(12900)；模板查找地址`https://grafana.com/grafana/dashboards/ `

## SpringBoot集成prometheus
* 添加依赖
```
<!--监控-->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<!--prometheus-->
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```
* 开启监控配置
```
management:
  endpoint:
    health:
      show-details: always
    metrics:
      enabled: true
    prometheus:
      enabled: true
  endpoints:
    web:
      exposure:
        include: '*'
  metrics:
    export:
      prometheus:
        enabled: true
```
* prometheus的`prometheus.yml`增加项目配置信息
```
  - job_name: 'MessageCenter'
    metrics_path: '/actuator/prometheus'
    static_configs:
    - targets: ['ip:port']
```