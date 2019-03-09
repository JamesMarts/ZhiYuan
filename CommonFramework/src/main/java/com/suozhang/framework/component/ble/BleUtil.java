/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.component.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * 项目名称：
 * 类描述：蓝牙操作工具类
 * 创建人：Moodd
 * 创建时间：2016/5/11 17:16
 * 修改人：Moodd
 * 修改时间：2016/5/11 17:16
 * 修改备注：
 */
public class BleUtil {

    /**
     * 判断设备是否支持BLE
     *
     * @param context
     * @return true：支持 false：不支持
     */
    public static boolean isSupportBle(Context context) {
        Context appContext = context.getApplicationContext();
        return appContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * 是否开启蓝牙
     *
     * @return true：已开启 false：未开启
     */
    public static boolean isEnabledBluetooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) return false;
        return adapter.isEnabled();
    }

    /**
     * 打开蓝牙-显式
     *
     * @param activity
     * @param requestCode
     */
    public static void enableBluetooth(Activity activity, int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开蓝牙-隐式
     *
     * @param context
     */
    public static void enableBluetooth(Context context) {
        BluetoothAdapter adapter = getBluetoothAdapter(context);
        if (adapter != null && !adapter.isEnabled()) {
            adapter.enable();
        }
    }

    /**
     * 获取BluetoothAdapter
     *
     * @param context
     * @return
     */
    public static BluetoothAdapter getBluetoothAdapter(Context context) {
        Context appContext = context.getApplicationContext();
        BluetoothManager manager = (BluetoothManager) appContext.getSystemService(Context.BLUETOOTH_SERVICE);
        return manager.getAdapter();
    }

    /**
     * 打开或关闭蓝牙-隐式
     *
     * @param context
     * @param enable
     * @return
     */
    public static boolean enableBluetooth(Context context, boolean enable) {
        BluetoothAdapter adapter = getBluetoothAdapter(context);

        if (enable) {
            if (!adapter.isEnabled()) {
                return adapter.enable();
            }
        } else {
            if (adapter.isEnabled()) {
                return adapter.disable();
            }
        }
        return true;
    }

    /**
     * 刷新蓝牙缓存
     *
     * @param gatt
     * @return
     */
    public static boolean refreshDeviceCache(BluetoothGatt gatt) {
        try {
            Method e = BluetoothGatt.class.getMethod("refresh", new Class[0]);
            if (e != null) {
                boolean success = ((Boolean) e.invoke(gatt, new Object[0])).booleanValue();
                Log.i("BluetoothUtil", "Refreshing result: " + success);
                return success;
            }
        } catch (Exception var3) {
            Log.e("BluetoothUtil", "An exception occured while refreshing device", var3);
        }
        return false;
    }

}
