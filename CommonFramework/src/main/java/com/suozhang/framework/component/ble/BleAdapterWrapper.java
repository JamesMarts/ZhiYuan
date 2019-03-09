/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.Nullable;

import java.util.Set;

 class BleAdapterWrapper {

    private final BluetoothAdapter bluetoothAdapter;

    public BleAdapterWrapper(@Nullable BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    /**
     * 根据MAC地址获取远程设备
     *
     * @param macAddress MAC地址
     * @return 远程设备
     * @throws Exception //MAC地址不合法，会抛出异常 BluetoothAdapter.checkBluetoothAddress(mac)
     */
    public BluetoothDevice getRemoteDevice(String macAddress) throws Exception {
        return bluetoothAdapter.getRemoteDevice(macAddress);
    }

    /**
     * 是否初始化了BluetoothAdapter
     *
     * @return true:已初始化 false:未初始化
     */
    public boolean hasBluetoothAdapter() {
        return bluetoothAdapter != null;
    }

    /**
     * 蓝牙是否开启
     *
     * @return true:已开启 false:未开启
     */
    public boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    /**
     * 开始扫描
     *
     * @param leScanCallback
     * @return
     */
    public boolean startLeScan(BluetoothAdapter.LeScanCallback leScanCallback) {
        return bluetoothAdapter.startLeScan(leScanCallback);
    }

    /**
     * 停止扫描
     *
     * @param leScanCallback
     */
    public void stopLeScan(BluetoothAdapter.LeScanCallback leScanCallback) {
        bluetoothAdapter.stopLeScan(leScanCallback);
    }

    /**
     * 获取BondedDevices
     *
     * @return
     */
    public Set<BluetoothDevice> getBondedDevices() {
        return bluetoothAdapter.getBondedDevices();
    }
}
