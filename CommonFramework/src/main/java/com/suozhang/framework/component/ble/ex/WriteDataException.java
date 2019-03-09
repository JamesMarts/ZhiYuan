/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.component.ble.ex;

/**
 * @author Moodd
 * @email 420410175@qq.com
 * @date 2017/4/10 18:05
 */

public class WriteDataException extends RuntimeException {
    public WriteDataException() {
        super();
    }

    public WriteDataException(String message) {
        super(message);
    }

    public WriteDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
