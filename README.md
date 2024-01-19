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

## 007 功能开发前期准备

本章节主要介绍了login的前端，以及post返回的结果

前端利用thymeleaf
```html
<!DOCTYPE html>
<html lang="en"
      xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <!-- jquery -->
    <script type="text/javascript" th:src="@{/js/jquery.min.js}"></script>
    <!-- bootstrap -->
    <link rel="stylesheet" type="text/css" th:href="@{/bootstrap/css/bootstrap.min.css}"/>
    <script type="text/javascript" th:src="@{/bootstrap/js/bootstrap.min.js}"></script>
    <!-- jquery-validator -->
    <script type="text/javascript" th:src="@{/jquery-validation/jquery.validate.min.js}"></script>
    <script type="text/javascript" th:src="@{/jquery-validation/localization/messages_zh.min.js}"></script>
    <!-- layer -->
    <script type="text/javascript" th:src="@{/layer/layer.js}"></script>
    <!-- md5.js -->
    <script type="text/javascript" th:src="@{/js/md5.min.js}"></script>
    <!-- common.js -->
    <script type="text/javascript" th:src="@{/js/common.js}"></script>
</head>
<body>
<form name="loginForm" id="loginForm" method="post" style="width:50%; margin:0 auto">

    <h2 style="text-align:center; margin-bottom: 20px">用户登录</h2>

    <div class="form-group">
        <div class="row">
            <label class="form-label col-md-4">请输入手机号码</label>
            <div class="col-md-5">
                <input id="mobile" name="mobile" class="form-control" type="text" placeholder="手机号码" required="true"
                />
                <!--             取消位数限制          minlength="11" maxlength="11"-->
            </div>
            <div class="col-md-1">
            </div>
        </div>
    </div>

    <div class="form-group">
        <div class="row">
            <label class="form-label col-md-4">请输入密码</label>
            <div class="col-md-5">
                <input id="password" name="password" class="form-control" type="password" placeholder="密码"
                       required="true"
                />
                <!--             取消位数限制            minlength="6" maxlength="16"-->
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-5">
            <button class="btn btn-primary btn-block" type="reset" onclick="reset()">重置</button>
        </div>
        <div class="col-md-5">
            <button class="btn btn-primary btn-block" type="submit" onclick="login()">登录</button>
        </div>
    </div>
</form>
</body>
<script>
    function login() {
        $("#loginForm").validate({
            submitHandler: function (form) {
                doLogin();
            }
        });
    }

    function doLogin() {
        g_showLoading();

        var inputPass = $("#password").val();
        var salt = g_passsword_salt;
        var str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        var password = md5(str);

        $.ajax({
            url: "/login/doLogin",
            type: "POST",
            data: {
                mobile: $("#mobile").val(),
                password: password
            },
            success: function (data) {
                layer.closeAll();
                if (data.code == 200) {
                    layer.msg("成功");
                    console.log(data);
                    document.cookie = "userTicket=" + data.object;
                    window.location.href = "/goods/toList";
                } else {
                    layer.msg(data.message);
                }
            },
            error: function () {
                layer.closeAll();
            }
        });
    }
</script>
</html>
```

后端介绍了post返回结果，利用一个enum类型来定义有返回结果的代码和消息

```java
package org.gotomove.flashsale.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Author zhang
 * @Date 2024/1/19
 * @Description 公共返回对象枚举
 */

@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "服务端异常");

    private final Integer code;
    private final String message;
}

```

再设置一个类来响应返回结果
```java
package org.gotomove.flashsale.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zhang
 * @Date 2024/1/19
 * @Description 公共返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private Integer code;
    private String message;
    private Object obj;

    // 成功返回结果
    public static RespBean success() {
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBean.success().getMessage(), null);
    }

    // 成功返回结果
    public static RespBean success(Object obj) {
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBean.success().getMessage(), obj);
    }

    // 失败返回结果
    public static RespBean error(RespBeanEnum respBeanEnum) {
        return new RespBean(respBeanEnum.getCode(), respBeanEnum.getMessage(), null);
    }

    // 失败返回结果
    public static RespBean error(RespBeanEnum respBeanEnum, Object obj) {
        return new RespBean(respBeanEnum.getCode(), respBeanEnum.getMessage(), obj);
    }
}

```

**关于为什么成功没有枚举类型的参数而失败有的原因**
> 成功的code只有一个，但是失败有多种，且原因各不相同，所以要想获取具体的失败原因，就得获取具体的枚举参数

## 008 开发登录功能

利用前端传过来的手机号和密码，进行验证是否已经存在数据库中