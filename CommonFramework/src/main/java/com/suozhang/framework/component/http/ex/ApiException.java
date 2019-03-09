/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.http.ex;

/**
 * @author Moodd(420410175[at]qq.com)
 * @date 2017/3/20 9:31
 */

public class ApiException extends Exception {
    private int code = -999;

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(String message) {
        super(message);
    }

    public int code() {
        return this.code;
    }

    @Override
    public String toString() {
        return "ApiException{" +
                "code=" + code +
                "} " + super.toString();
    }
}
