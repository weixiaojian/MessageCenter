# apollo安装
> apollo能够将配置的信息分离于自身的系统，而这些信息又能被应用实时获取得到；服务端基于Spring Boot和Spring Cloud开发
* 官网：[https://www.apolloconfig.com/#/zh/README](https://www.apolloconfig.com/#/zh/README)
* 将`https://github.com/apolloconfig/apollo/tree/master/scripts/docker-quick-start`下的docker-compose.yml、sql文件下载到linux服务器上
```
version: '2'

services:
  apollo-quick-start:
    image: nobodyiam/apollo-quick-start
    container_name: apollo-quick-start
    depends_on:
      - apollo-db
    ports:
      - "8080:8080"
      - "8090:8090"
      - "8070:8070"
    links:
      - apollo-db

  apollo-db:
    image: mysql:5.7
    container_name: apollo-db
    environment:
      TZ: Asia/Shanghai
      MYSQL_ALLOW_EMPTY_PASSWORD: 'yes'
    depends_on:
      - apollo-dbdata
    ports:
      - "13306:3306"
    volumes:
      - ./sql:/docker-entrypoint-initdb.d
    volumes_from:
      - apollo-dbdata

  apollo-dbdata:
    image: alpine:latest
    container_name: apollo-dbdata
    volumes:
      - /var/lib/mysql
```
* 执行命令`docker-compose up -d`，8070是后台管理端口 8080是服务端请求端口；浏览器打开http://127.0.0.1:8070  账号/密码：apollo/admin

# SpringBoot接入apollo
* 引入依赖
```
<dependency>
  <groupId>com.ctrip.framework.apollo</groupId>
  <artifactId>apollo-client-config-data</artifactId>
  <version>1.9.1</version>
</dependency>
```
* 增加配置文件信息，app.id对应后台配置的app.id；namespaces对应后台配置的namespaces(自己增加的)
```
app:
  id: MessageCenter
apollo:
  bootstrap:
    enabled: true
    namespaces: message.center
```

* 启动类增加apollo地址信息
```
@SpringBootApplication
public class MsgApplication {


    public static void main(String[] args) {
        // apollo的地址
        System.setProperty("apollo.config-service", "http://127.0.0.1:8080");

        SpringApplication.run(MsgApplication.class, args);
    }
}
```

* 项目中使用`@ApolloConfig`注入apollo配置，也可以直接使用`@Value("${key}")`来注入
```
@Service
public class DiscardMessageService {

    /**
     * 配置样例：key=discard   value=[1,2,3]
     */
    private static final String DISCARD_MESSAGE_KEY = "discard";

    @ApolloConfig("message.center")
    private Config config;

    /**
     * 丢弃消息，配置在apollo
     * @param taskInfo
     * @return
     */
    public boolean isDiscard(TaskInfo taskInfo) {
        JSONArray array = JSON.parseArray(config.getProperty(DISCARD_MESSAGE_KEY,
                MessageCenterConstant.APOLLO_DEFAULT_VALUE_JSON_ARRAY));
        //如果模板id在apollo的配置中配置了要丢弃  则丢弃该消息
        if (array.contains(String.valueOf(taskInfo.getMessageTemplateId()))) {
            return true;
        }
        return false;
    }
}
```