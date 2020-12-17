#                                            docker

## 一、docker启动/停止

systemctl start docker

system stop docker 

## 二、常规使用

####1）、镜像操作

docker search mysql（tips:如出现 Get https://index.docker.io/v1/search?q=mysql&n=25: dial tcp: lookup index.docker.io on 100.100.2.136:53: server；==可修改/etc/hosts 末端添加100.100.2.136 index.docker.io==）

docker pull mysql

docker images

docker pull mysql:5.5

**官网：https://hub.docker.com/**

#### 2）、容器操作

运行镜像：产生一个容器

步骤：

```txt
1.搜索镜像
docker search tomcat
2.拉取镜像
docker pull tomcat
3.启动容器
docker run --name myomcat -d tomcat:latest
4.docker ps  查看运行中的容器
5.停止运行中的容器
docker stop 容器ID
6.查看所有容器
docker ps -a
7.启动容器
dockerstart 容器ID
8.删除一个容器
docker rm  容器id
9.启动做了端口映射的Tomcat
docker run -d -p 8081:8080 tomcat
-d:后台运行
-p:将主机的端口映射到容器的端口  主机端口：容器内部端口
10.为了演示简单，关闭了linux防火墙
service firewalld status:查看防火墙状态
service firewalld  stop:关闭防火墙
11.查看容器日志
docker log 容器id
```

mysql操作

```txt
1.docker pull mysql
2.docker run -p 8081:3306 --name mysql03 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7.31(-p指定端口号
--name指定别名 -e指定用户密码  -d后台运行 mysql:版本号)
3.其他操作
docker run --name mysql03 -v /conf/mysql:/etc/mysql/conf.d -e MYSQL-ROOT-PASSWORD-my-pw -d mysql:tag
把主机的/my/custom文件夹挂载到mysqldocker容器的/etc/mysql/conf.d 文件及家里面
改mysql配置文件放在

docker run --name some-mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql:tag --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
```

```yml
spring:
  datasource:
    username: root
    password: 123456
    url: jdbc:mysql://39.107.229.253:8081/jdbc
    driver-class-name: com.mysql.jdbc.Driver

```

上面是spring-boot mysql配置





