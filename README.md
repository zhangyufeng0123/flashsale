# B站秒杀项目

[视频链接](https://www.bilibili.com/video/BV1sf4y1L7KE?p=1&vd_source=ca005583d3c88794a5e2fb97c9fa2278)

## 001 课程介绍

### 技术点介绍

- 前端：Thymeleaf, Bootstrap, Jquery
- 后端：SpringBoot, MyBatisPlus, Lombok
- 中间件：RabbitMQ, Redis

### Java秒杀方案

- 项目搭建
- 分布式Session
- 秒杀功能
- 压力测试
- 页面优化
- 服务优化
- 接口安全

## 002 学习目标

- 分布式会话
  - 用户登录
  - 共享Session
- 功能开发
  - 商品列表
  - 商品详情
  - 秒杀
  - 订单详情
- 系统压测
  - JMeter入门
  - 自定义变量
  - 正式压测
- 安全优化
  - 隐藏秒杀地址
  - 验证码
  - 接口限流
- 服务优化
  - RabbitMQ消息队列
  - 接口优化
  - 分布式锁
- 页面优化
  - 缓存
  - 静态化分离

## 003 如何设计一个秒杀系统

- 并发读
- 并发写

## 004 项目创建

配置spring、mybatis-plus、logging

```yaml
spring:
  # thymeleaf 配置
  thymeleaf:
    # 关闭缓存
    cache: false
  # 数据库配置
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/flashsale?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: root
    hikari:
      # 连接池名
      pool-name: DataHikariCP
      # 最小空闲连接数
      minimum-idle: 5
      # 空闲连接存货最大时间，默认600000
      idle-timeout: 180000
      # 最大连接数
      maximum-pool-size: 10
      # 从连接池返回的链接自动提交
      auto-commit: true
      # 连接最大存活时间
      max-lifetime: 180000
      # 连接超时时间
      connection-timeout: 30000
      # 心跳机制
      connection-test-query: SELECT 1

# mybatis plus 配置
mybatis-plus:
  # 配置 Mapper.xml 映射文件
  mapper-locations: classpath*:/mapper/*Mapper.xml
  # 配置Mybatis数据返回类型别名
  type-aliases-package: org.gotomove.flashsale.pojo

# Mybatis SQL 打印日志级别（方法接口所在的包，不是mapper.xml所在的包）
logging:
  level:
    org.gotomove.flashsale.mapper: debug
```

数据库建用户表
```sql
create table t_user(
	`id` BIGINT(20) not null comment '用户ID，手机号码',
	`nickname` VARCHAR(255) not null,
	`password` VARCHAR(32) default null comment 'MDS(MDS(pass明文+固定salt)+salt)',
	`slat` VARCHAR(10) default null,
	`head` VARCHAR(128) default null comment '头像',
	`register_date` datetime null comment '注册时间',
	`last_login_date` datetime default null comment '最后一次登录时间',
	`login_count` int(11) default '0' comment '登录次数',
	primary key(`id`)
)
```

## 005 2次MD5加密

从前端传过来的数据进行第一次加密，将数据传到数据库再进行一次加密。

第一次加密
```java
public static String inputPassToFromPass(String inputPass) {
    String str = salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
    return md5(str);
}
```

第二次加密
```java
public static String fromPassToDBPass(String fromPass, String salt){
    String str = salt.charAt(0) + salt.charAt(2) + fromPass + salt.charAt(5) + salt.charAt(4);
    return md5(str);
}
```

## 006 逆向工程

