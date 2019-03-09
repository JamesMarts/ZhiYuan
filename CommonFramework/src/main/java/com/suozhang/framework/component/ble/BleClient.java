/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.component.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * BLE蓝牙客户端接口
 *
 * @author Moodd
 * @email 420410175@qq.com
 * @date 2017/4/12 18:04
 */

public interface BleClient {

    /**
     * 检测BLE蓝牙状态，包括蓝牙是否开启，是否具有相应权限等
     *
     * @param <T>
     * @return
     */
    public <T> Observable<T> checkBleState();

    /**
     * 扫描蓝牙设备
     *
     * @return
     */
    public Observable<BleScanResult> scanBleDevices();

    /**
     * 扫描蓝牙设备
     *
     * @param scanTimeout 超时时间
     * @param unit        时间单位
     * @return
     */
    public Observable<BleScanResult> scanBleDevices(long scanTimeout, TimeUnit unit);

    /**
     * 扫描指定MAC地址的蓝牙设备
     *
     * @param mac         指定设备的MAC地址
     * @param scanTimeout 超时时间
     * @param unit        时间单位
     * @return
     */
    public Observable<BleScanResult> scanBleDeviceByMac(String mac, long scanTimeout, TimeUnit unit);

    /**
     * 扫描指定名称的蓝牙设备
     *
     * @param name        指定设备的名称
     * @return
     */
    public Observable<BleScanResult> scanBleDeviceByName(String name);

    /**
     * 扫描指定名称的蓝牙设备
     *
     * @param name        指定设备的名称
     * @param scanTimeout 超时时间
     * @param unit        时间单位
     * @return
     */
    public Observable<BleScanResult> scanBleDeviceByName(String name, long scanTimeout, TimeUnit unit);

    /**
     * 连接指定的设备
     *
     * @param mac 指定的MAC地址
     * @return
     */
    public Observable<BleConnectState> connect(String mac);

    /**
     * 连接指定的设备
     *
     * @param device 指定的设备
     * @return
     */
    public Observable<BleConnectState> connect(BluetoothDevice device);

    /**
     * 发送数据
     *
     * @param command 数据包
     * @return
     */
    public Observable<BluetoothGattCharacteristic> sendData(String command);

    /**
     * 发送数据
     *
     * @param state
     * @param command 数据包
     */
    public Observable<BluetoothGattCharacteristic> sendData(BleConnectState state, String command);

    /**
     * 连接并发送数据
     *
     * @param mac     MAC地址
     * @param command 数据
     * @return
     */
    public Observable<BluetoothGattCharacteristic> connectAndSend(String mac, String command);

    /**
     * 连接并发送数据
     *
     * @param device  蓝牙设备
     * @param command 数据
     * @return
     */
    public Observable<BluetoothGattCharacteristic> connectAndSend(BluetoothDevice device, String command);

    /**
     * 指定设备扫描并连接
     *
     * @param mac         指定设备的MAC地址
     * @param scanTimeout 超时时间
     * @param unit        时间单位
     * @return
     */
    public Observable<BleConnectState> scanAndConnect(String mac, long scanTimeout, TimeUnit unit);

    /**
     * 指定设备扫描、连接并发送数据
     *
     * @param mac         指定设备的MAC地址
     * @param command     数据
     * @param scanTimeout 超时时间
     * @param unit        时间单位
     * @return
     */
    public Observable<BluetoothGattCharacteristic> scanConnectAndSend(String mac, String command, long scanTimeout, TimeUnit unit);

    /**
     * 停止扫描
     */
    public void releaseScanner();

    /**
     * 关闭连接
     */
    public void releaseConnector();

    /**
     * 停止扫描，关闭连接，释放资源
     */
    public void release();
}