# redis安装
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
* 新建redis文件夹，redis下新建data文件以及redis.conf配置文件(requirepass 123456是redis的登录密码)
```
protected-mode no
port 6379
timeout 0
save 900 1 
save 300 10
save 60 10000
rdbcompression yes
dbfilename dump.rdb
dir /data
appendonly yes
appendfsync everysec
requirepass 123456
```

* 新建docker-compose.yml文件，打开服务器的6379端口
```
version: '3'
services:
  redis:
    image: redis:latest
    container_name: redis
    restart: always
    ports:
      - 6379:6379
    volumes:
      - ./redis.conf:/usr/local/etc/redis/redis.conf:rw
      - ./data:/data:rw
    command:
      /bin/bash -c "redis-server /usr/local/etc/redis/redis.conf "
```
* 编译docker-compose.yml下载redis的镜像并运行（docker-compose.yml文件目录下执行）
```
docker-compose up -d
```
* 查看镜像运行情况 并进入redis容器
```
docker ps 
docker exec -it redis redis-cli

#容器不停止退出：ctrl+P+Q
```
* 此时操作redis命令还不行 需要登录redis
```
auth 123456
```