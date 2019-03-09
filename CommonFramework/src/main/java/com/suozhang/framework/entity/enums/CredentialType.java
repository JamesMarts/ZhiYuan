/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.entity.enums;

/**
 * 用户登录类型,登录时指定的参数
 */
public enum CredentialType {
    UNKNOWN(-1),//未定义，无此登录方式
    SYSTEM(0),//系统预留登录方式（用户名，邮箱，电话）
    PHONE(1),//手机验证码动态登录
    QQ(2),//QQ第三方登录
    WX(3),//WX第三方登录
    WEIBO(4);//WX第三方登录
    private final int value;

    CredentialType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static CredentialType typeOf(int value) {
        for (CredentialType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
