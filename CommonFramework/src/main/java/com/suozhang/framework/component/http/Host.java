/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.http;

/**
 * Http请求时的主机头
 * Created by Moodd on 2017/2/10.
 */
public enum Host {
    /**
     * 线上生产环境式地址
     */
    ONLINE("http://weixin.suozhang.net/"),
    /**
     * 线上测试环境地址
     */
    DEBUG_ONLINE("http://csapi.wisdomhotel.net/"),
    /**
     * 本地测试环境地址
     */
    DEBUG_LOCAL("http://10.10.2.99:5556/");

    private final String value;

    Host(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
