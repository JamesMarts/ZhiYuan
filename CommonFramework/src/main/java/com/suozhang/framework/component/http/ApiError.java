/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.http;

/**
 * Http请求错误码
 *
 * @author Moodd(420410175[at]qq.com)
 * @date 2017/3/22 11:45
 */
public enum ApiError {
    /**
     * NoPermission = 9998,//没有权限
     * SystemError = 9999,//系统错误
     * Success = 10000,//处理成功
     * Miss = 10001,//找不到该条数据
     * InvalidPara = 10002,//无效的参数
     * HasExist = 10003,//已存在的数据
     * TokenHasOvertime = 10004,//token已过期
     */

    //客户端错误
    UNKNOWN(-999, "未知错误"),
    CONNECT_TIME_OUT(-998, "连接超时，请检查您的网络"),
    CONNECT_ERROR(-997, "连接异常，请检查您的网络"),//ConnectException

    //Http错误码Request ApiError
    REQUEST_ERROR(400, "请求出错，请核对后再试！"),
    UNAUTHORIZED(401, "登录过期，请重新登录"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源未找到，请核对后再试！"),
    INTERNAL_SERVER_ERROR(500, "服务器忙，请稍后再试！"),


    //约定错误码
    // NOTE: 2017/6/16 统一转化为ApiException，以服务端返回消息为准

    NO_PERMISSION(9998, "无权限访问"),//暂定,与403一致
    SYSTEM_ERROR(9999, "系统错误"),

    SUCCESS(10000, "成功"),
    NOT_FOUND_DATA(10001, "数据不存在"),
    INVALID_PARAMS(10002, "无效的参数"),
    EXISTING_DATA(10003, "已存在的数据"),
    REFRESH_TOKEN_EXPIRE(10004, "登录过期，请重新登录"),//refreshToken过期

    CODE_OVERRUN(10006, "硬件号已达到上限值,不允许添加"),

    NOT_EXIST_USER(10101, "不存在的用户信息"),
    USER_NAME_OR_PASSWORD_ERROR(10102, "用户名或密码错误"),
    ORG_STATUS_NOT_ENABLED(10104, "机构状态未启用");

    private final int code;
    private final String msg;

    ApiError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return this.code;
    }

    public String msg() {
        return this.msg;
    }

    public static ApiError codeOf(int code) {
        for (ApiError error : values()) {
            if (error.code() == code) {
                return error;
            }
        }
        return UNKNOWN;
    }
}