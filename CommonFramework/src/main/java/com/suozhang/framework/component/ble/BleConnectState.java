/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.component.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Moodd
 * @email 420410175@qq.com
 * @date 2017/4/11 13:34
 */

final class BleConnectState {

    /**
     * @hide
     */
    @IntDef({STATE_CONNECTED,
            STATE_DISCONNECTED,
            STATE_READ_DATA
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

    }

    public static final int STATE_CONNECTED = 0;
    public static final int STATE_DISCONNECTED = 1;
    public static final int STATE_READ_DATA = 2;

    @State
    private int state;
    private BluetoothGatt gatt;
    private BluetoothGattCharacteristic characteristic;

    public BleConnectState(@State int state) {
        this.state = state;
    }

    public BleConnectState(@State int state, BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        this.state = state;
        this.gatt = gatt;
        this.characteristic = characteristic;
    }

    @State
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public BluetoothGatt getGatt() {
        return gatt;
    }

    public void setGatt(BluetoothGatt gatt) {
        this.gatt = gatt;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
    }
}
