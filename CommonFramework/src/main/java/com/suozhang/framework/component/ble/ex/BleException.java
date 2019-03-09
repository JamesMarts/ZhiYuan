/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.component.ble.ex;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Exception emitted as a result of faulty scan operation. The reason describes the situation in details.
 */
public class BleException extends RuntimeException {

    /**
     * @hide
     */
    @IntDef({BLUETOOTH_SCAN_CANNOT_START,
            BLUETOOTH_DISABLED,
            BLUETOOTH_NOT_AVAILABLE,
            LOCATION_PERMISSION_MISSING,
            LOCATION_SERVICES_DISABLED,
            BLUETOOTH_TIME_OUT,
            BLUETOOTH_GATT_CONNECT_ERROR,
            BLUETOOTH_GATT_SEND_DATA_ERROR,
            BLUETOOTH_RESULT_ERROR,
            BLUETOOTH_RESULT_UNAUTHOR,
            BLUETOOTH_RESULT_UNINSTALL,
            BLUETOOTH_RAW_DATA_ERROR

    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Reason {

    }

    /**
     * 扫描出错
     */
    public static final int BLUETOOTH_SCAN_CANNOT_START = 0;

    /**
     * 蓝牙未开启
     */
    public static final int BLUETOOTH_DISABLED = 1;

    /**
     * 蓝牙不可用
     */
    public static final int BLUETOOTH_NOT_AVAILABLE = 2;

    /**
     * 没有位置权限
     */
    public static final int LOCATION_PERMISSION_MISSING = 3;

    /**
     * 位置服务未启用
     */
    public static final int LOCATION_SERVICES_DISABLED = 4;

    /**
     * 超时
     */
    public static final int BLUETOOTH_TIME_OUT = 5;

    /**
     * GATT连接错误
     */
    public static final int BLUETOOTH_GATT_CONNECT_ERROR = 6;

    /**
     * 发送数据出错
     */
    public static final int BLUETOOTH_GATT_SEND_DATA_ERROR = 7;

    /**
     * 返回错误数据结果-错误
     */
    public static final int BLUETOOTH_RESULT_ERROR = 8;
    /**
     * 返回错误数据结果-未授权
     */
    public static final int BLUETOOTH_RESULT_UNAUTHOR = 9;
    /**
     * 返回错误数据结果-未安装
     */
    public static final int BLUETOOTH_RESULT_UNINSTALL = 10;

    /**
     * 原始数据有误
     */
    public static final int BLUETOOTH_RAW_DATA_ERROR = 11;

    @Reason
    private final int reason;


    public BleException(@Reason int reason) {
        this.reason = reason;
    }

    public BleException(@Reason int reason, Throwable causeException) {
        super(causeException);
        this.reason = reason;
    }

    /**
     * 原因
     *
     * @return
     */
    @Reason
    public int getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "BleException{"
                + "reason=" + reasonDescription()
                + '}';
    }

    @Override
    public String getMessage() {
        return reasonDescription();
    }

    private String reasonDescription() {
        switch (reason) {
            case BLUETOOTH_SCAN_CANNOT_START:
                return "蓝牙扫描出错";
            case BLUETOOTH_DISABLED:
                return "蓝牙未开启";
            case BLUETOOTH_NOT_AVAILABLE:
                return "蓝牙不可用";
            case LOCATION_PERMISSION_MISSING:
                return "位置权限不允许";
            case LOCATION_SERVICES_DISABLED:
                return "位置服务未启用";
            case BLUETOOTH_TIME_OUT:
                return "超时";
            case BLUETOOTH_GATT_CONNECT_ERROR:
                return "GATT连接错误";
            case BLUETOOTH_GATT_SEND_DATA_ERROR:
                return "发送数据出错";
            case BLUETOOTH_RESULT_ERROR:
                return "返回错误结果";
            case BLUETOOTH_RESULT_UNAUTHOR:
                return "门锁未授权";
            case BLUETOOTH_RESULT_UNINSTALL:
                return "门锁未安装";
            case BLUETOOTH_RAW_DATA_ERROR:
                return "源数据有误";
            default:
                return "未知异常";
        }
    }
}
