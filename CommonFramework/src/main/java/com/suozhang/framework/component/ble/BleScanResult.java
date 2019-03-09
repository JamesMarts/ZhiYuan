/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.ble;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class BleScanResult {
    /**
     * @hide
     */
    @IntDef({STATE_SCAN_SUCCESS,
            STATE_SCAN_FAILURE,
            STATE_SCAN_TIMEOUT
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

    }

    /**
     * 扫描成功
     */
    public static final int STATE_SCAN_SUCCESS = 0;
    /**
     * 扫描失败
     */
    public static final int STATE_SCAN_FAILURE = 1;
    /**
     * 扫描失败
     */
    public static final int STATE_SCAN_TIMEOUT = 2;

    @State
    private int state;
    private BluetoothDevice bleDevice;
    private int rssi;
    private byte[] scanRecord;
    private String hexScanRecord;

    private String deviceName;
    private String mac;

    public BleScanResult(@State int state) {
        this.state = state;
    }

    public BleScanResult(@State int state, BluetoothDevice bleDevice, int rssi, byte[] scanRecords) {
        this.state = state;
        this.bleDevice = bleDevice;
        this.rssi = rssi;
        this.scanRecord = scanRecords;
    }

    @Override
    public int hashCode() {
        return TextUtils.isEmpty(getMac()) ? super.hashCode() : getMac().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof BleScanResult) {
            return getMac().equalsIgnoreCase(((BleScanResult) obj).getMac());
        }
        return super.equals(obj);
    }

    @State
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public BluetoothDevice getBleDevice() {
        return bleDevice;
    }


    public int getRssi() {
        return rssi;
    }


    public byte[] getScanRecord() {
        return scanRecord;
    }

    public String getDeviceName() {
        if (deviceName == null) {
            deviceName = bleDevice == null ? null : bleDevice.getName();
            deviceName = TextUtils.isEmpty(deviceName) ? "" : deviceName;
        }
        return deviceName;
    }

    public String getMac() {
        if (mac == null) {
            mac = bleDevice == null ? null : bleDevice.getAddress();
            mac = TextUtils.isEmpty(mac) ? "" : mac;
        }
        return this.mac;
    }

    /**
     * 获取扫描记录的十六进制字符串
     *
     * @return
     */
    public String getScanRecordToHex() {
        if (hexScanRecord == null) {
            if (scanRecord == null) {
                return "";
            }
            //返回大写的十六进制
            hexScanRecord = BleCommand.byte2Hex(this.scanRecord);
            hexScanRecord = TextUtils.isEmpty(hexScanRecord) ? "" : hexScanRecord;
        }
        return hexScanRecord;
    }

    /**
     * 是否可授权的设备
     */
    public boolean isAuthorizable() {
        return checkName(BleCommand.STATE_FFFFFFFFN);
    }

    /**
     * 是否可安装的设备
     */
    public boolean isInstallable() {
        return checkName(BleCommand.STATE_FFFFFFFFI);
    }

    /**
     * 检查状态-根据扫描记录判断是否包含相应状态，即蓝牙广播出来的名称是否与指定的一致
     * <p>
     * // NOTE: 2017/5/12 因某些手机扫描出来的蓝牙名称可能不会实时改变，因此通过广播数据判断
     * {@link BleCommand#STATE_FFFFFFFFN,BleCommand#STATE_FFFFFFFFI,BleCommand#STATE_FFFFFFFFY}
     *
     * @param name
     * @return
     */

    public boolean checkName(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        String record = getScanRecordToHex();
        if (TextUtils.isEmpty(record)) {
            return false;
        }
        //注意大小写
        if (record.contains(name)) {
            return true;
        }
        return false;
    }
}