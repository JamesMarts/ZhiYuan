/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.http.ex;

/**
 * 服务端返回的错误信息包装，及约定的code不是成功标志时，统一抛出ServerException，由上层业务统一处理后交给UI显示
 * @author Moodd(420410175[at]qq.com)
 * @date 2017/3/20 9:31
 */

public class ServerException extends Exception {
    private int code = -999;

    public ServerException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServerException(String message) {
        super(message);
    }

    public int code() {
        return this.code;
    }

    @Override
    public String toString() {
        return "ServerException{" +
                "code=" + code +
                "} " + super.toString();
    }
}
