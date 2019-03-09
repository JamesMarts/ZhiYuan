/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.entity.bo;

import com.suozhang.framework.entity.enums.CredentialType;

/**
 * 登录请求
 *
 * @author Moodd
 * @e-mail 420410175@qq.com
 * @date 2017/3/29 9:40
 */

public class LoginRequest<T> implements BaseEntity {
    /**
     * {"Data":{"userName":"admin","passWord":"bdc5384309bde750d7983cef28e82e82"},"CredentialType":0}
     */
    private T data;//登录数据，根据登录类型指定

    private int credentialType = CredentialType.SYSTEM.value();//认证类型,默认用户名密码登录

    public LoginRequest() {
    }

    public LoginRequest(T data, int credentialType) {
        this.data = data;
        this.credentialType = credentialType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(int credentialType) {
        this.credentialType = credentialType;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "data=" + data +
                ", credentialType=" + credentialType +
                '}';
    }
}
