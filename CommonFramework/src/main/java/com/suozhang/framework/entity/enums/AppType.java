/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.entity.enums;

/**
 * Api请求时的头部参数，用于标识App类型，服务端会根据App类型判断是否拥有相应Api调用权限
 * Created by Moodd on 2017/2/24.
 */

public enum AppType {

    CLOUD_MANAGER(1),//锁掌云管理
    SMART_ALLIANCE(2),//锁掌智慧联盟
    INTERNAL_SYSTEM(3),//内部系统
    WX(4),//微信
    RENTAL_MANAGER(5),//出租管理
    RENTAL_USER(6);//出租屋用户端
    private final int value;

    AppType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
