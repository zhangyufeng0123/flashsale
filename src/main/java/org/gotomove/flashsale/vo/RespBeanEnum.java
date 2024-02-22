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
    ERROR(500, "服务端异常"),

    // 登录模块
    LOGIN_ERROR(500210, "用户名或密码错误"),
    MOBILE_ERROR(500211, "手机号码填写不规范"),
    BIND_ERROR(500212, "参数校验异常"),
    MOBILE_NOT_EXIST(500213, "手机号码不存在"),
    PASSWORD_UPDATE_FAIL(500214, "密码更新失败"),
    SESSION_ERROR(500215, "用户未登录"),

    // 商品模块
    EMPTY_STOCK(500310, "库存为0"),
    REPEAT_ERROR(500311, "重复购买"),
    ORDER_NOT_EXIST(500312, "订单不存在"),

    // 服务模块
    REQUEST_ILLEGAL(500410, "请求不合法"),

    ;

    private final Integer code;
    private final String message;
}
