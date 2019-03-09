/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import com.suozhang.framework.component.ble.ex.BleException;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * BLE蓝牙客户端
 * <p>
 * 注意：因为扫描及连接采用了{@link PublishSubject}实现，调用完毕，必须调用取消订阅方法{@link Disposable#dispose()},
 * 及每个观察者在不再使用时必须取消订阅，否则，会造成内存溢出
 *
 * @author Moodd
 * @e-mail 420410175@qq.com
 * @date 2017/3/28 10:42
 */

public class BleClientImpl implements BleClient {

    private static final Object sLock = new Object();
    //默认扫描超时时间 单位：分钟
    private static final int DEFAULT_SCAN_TIMEOUT = 30;


    private Context mContext;
    private LocationServicesStatus locationServicesStatus;

    private BleAdapterWrapper mBleAdapterWrapper;
    private BLeDeviceScanner mBLeDeviceScanner;
    private BleConnectionConnector mBleConnectionConnector;

    private Observable<BleAdapterStateObservable.BleAdapterState> bleAdapterStateObservable;

    private static BleClientImpl instance;

    public static BleClientImpl getInstance(Context context) {
        if (instance == null) {
            synchronized (sLock) {
                if (instance == null) {
                    instance = new BleClientImpl(context);
                }
            }
        }
        return instance;
    }

    private BleClientImpl() {
        throw new RuntimeException("单例模式，请调用getInstance()获取实例");
    }

    private BleClientImpl(Context context) {
        this.mContext = context.getApplicationContext();
        initialize(mContext);
    }

    /**
     * 初始化BluetoothAdapter
     *
     * @return 成功 true 失败 false
     */
    private void initialize(Context context) {
        if (mBleAdapterWrapper == null) {
            locationServicesStatus = new LocationServicesStatus(context);
            bleAdapterStateObservable = BleAdapterStateObservable.bleAdapterStateObservable(context);

            BluetoothAdapter adapter = BleUtil.getBluetoothAdapter(context);
            mBleAdapterWrapper = new BleAdapterWrapper(adapter);

            if (mBleAdapterWrapper.hasBluetoothAdapter()) {
                mBLeDeviceScanner = new BLeDeviceScanner(mBleAdapterWrapper);
                mBleConnectionConnector = new BleConnectionConnector(context, mBleAdapterWrapper);
            }
        }
    }

    /**
     * 检测BLE蓝牙状态
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> Observable<T> checkBleState() {

        if (!mBleAdapterWrapper.hasBluetoothAdapter()) {
            return Observable.error(new BleException(BleException.BLUETOOTH_NOT_AVAILABLE));
        } else if (!mBleAdapterWrapper.isBluetoothEnabled()) {
            return Observable.error(new BleException(BleException.BLUETOOTH_DISABLED));
        } else if (!locationServicesStatus.isLocationPermissionOk()) {
            return Observable.error(new BleException(BleException.LOCATION_PERMISSION_MISSING));
        } else {
            return null;
        }
        // NOTE: 2017/12/27 去掉位置服务开启校验，解决未手动开启“位置服务”或部分机型获取不到状态时，权限校验失败导致蓝牙操作直接提示失败的问题
       /* else if (!locationServicesStatus.isLocationProviderOk()) {
            return Observable.error(new BleException(BleException.LOCATION_SERVICES_DISABLED));
        }*/
    }

    /**
     * 蓝牙开关监听，当蓝牙关闭时，发送错误
     *
     * @return
     */
    private <T> Observable<T> bleAdapterStateObservable() {

        return bleAdapterStateObservable.filter(new Predicate<BleAdapterStateObservable.BleAdapterState>() {
            @Override
            public boolean test(@NonNull BleAdapterStateObservable.BleAdapterState bleAdapterState) throws Exception {
                return bleAdapterState != BleAdapterStateObservable.BleAdapterState.STATE_ON;
            }
        }).flatMap(new Function<BleAdapterStateObservable.BleAdapterState, ObservableSource<T>>() {
            @Override
            public ObservableSource<T> apply(@NonNull BleAdapterStateObservable.BleAdapterState bleAdapterState) throws Exception {
                return Observable.error(new BleException(BleException.BLUETOOTH_DISABLED));
            }
        });
    }

    /**
     * 扫描蓝牙设备 默认超时时间30分钟
     *
     * @return
     */
    @Override
    public Observable<BleScanResult> scanBleDevices() {
        return scanBleDevices(DEFAULT_SCAN_TIMEOUT, TimeUnit.MINUTES);
    }

    /**
     * 扫描蓝牙设备
     *
     * @param scanTimeout 超时时间
     * @param unit        时间单位
     * @return
     */
    @Override
    public Observable<BleScanResult> scanBleDevices(long scanTimeout, TimeUnit unit) {
        return scanBleDeviceByMac(null, scanTimeout, unit);
    }

    /**
     * 扫描指定MAC地址的蓝牙设备
     *
     * @param mac         指定设备的MAC地址
     * @param scanTimeout 超时时间
     * @param unit        时间单位
     * @return
     */
    @Override
    public Observable<BleScanResult> scanBleDeviceByMac(String mac, long scanTimeout, TimeUnit unit) {
        Observable<BleScanResult> error = checkBleState();
        if (error != null) {
            return error;
        }
        return mBLeDeviceScanner.scanBleDevices(mac, null, scanTimeout, unit)
                .mergeWith(this.<BleScanResult>bleAdapterStateObservable());
    }

    @Override
    public Observable<BleScanResult> scanBleDeviceByName(String name) {
        return scanBleDeviceByName(name, DEFAULT_SCAN_TIMEOUT, TimeUnit.MINUTES);
    }

    @Override
    public Observable<BleScanResult> scanBleDeviceByName(String name, long scanTimeout, TimeUnit unit) {
        Observable<BleScanResult> error = checkBleState();
        if (error != null) {
            return error;
        }
        return mBLeDeviceScanner.scanBleDevices(null, name, scanTimeout, unit)
                .mergeWith(this.<BleScanResult>bleAdapterStateObservable());
    }

    /**
     * 连接指定的设备
     *
     * @param mac 指定的MAC地址
     * @return
     */
    @Override
    public Observable<BleConnectState> connect(String mac) {
        //MAC地址不合法，会抛出异常 BluetoothAdapter.checkBluetoothAddress(mac)
        BluetoothDevice device = null;
        try {
            device = mBleAdapterWrapper.getRemoteDevice(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connect(device);
    }

    /**
     * 连接指定的设备
     *
     * @param device 指定的设备
     * @return
     */
    @Override
    public Observable<BleConnectState> connect(BluetoothDevice device) {
        Observable<BleConnectState> error = checkBleState();
        if (error != null) {
            return error;
        }
        return mBleConnectionConnector.connect(device)
                .mergeWith(this.<BleConnectState>bleAdapterStateObservable());
    }

    /**
     * 发送数据
     *
     * @param command 数据包
     * @return
     */
    @Override
    public Observable<BluetoothGattCharacteristic> sendData(String command) {
        Observable<BluetoothGattCharacteristic> error = checkBleState();
        if (error != null) {
            return error;
        }
        return mBleConnectionConnector.sendData(command)
                .mergeWith(this.<BluetoothGattCharacteristic>bleAdapterStateObservable());
    }

    /**
     * 发送数据
     *
     * @param state
     * @param command 数据包
     */
    @Override
    public Observable<BluetoothGattCharacteristic> sendData(BleConnectState state, String command) {
        Observable<BluetoothGattCharacteristic> error = checkBleState();
        if (error != null) {
            return error;
        }
        return mBleConnectionConnector.sendData(state, command)
                .mergeWith(this.<BluetoothGattCharacteristic>bleAdapterStateObservable());
    }

    /**
     * 连接并发送数据
     *
     * @param mac     MAC地址
     * @param command 数据
     * @return
     */
    @Override
    public Observable<BluetoothGattCharacteristic> connectAndSend(String mac, String command) {
        //MAC地址不合法，会抛出异常 BluetoothAdapter.checkBluetoothAddress(mac)
        BluetoothDevice device = null;
        try {
            device = mBleAdapterWrapper.getRemoteDevice(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.connectAndSend(device, command);
    }

    /**
     * 连接并发送数据
     *
     * @param device  蓝牙设备
     * @param command 数据
     * @return
     */
    @Override
    public Observable<BluetoothGattCharacteristic> connectAndSend(BluetoothDevice device, String command) {
        Observable<BluetoothGattCharacteristic> error = checkBleState();
        if (error != null) {
            return error;
        }
        return mBleConnectionConnector.connectAndSend(device, command)
                .mergeWith(this.<BluetoothGattCharacteristic>bleAdapterStateObservable());
    }

    /**
     * 指定设备扫描并连接
     *
     * @param mac         指定设备的MAC地址
     * @param scanTimeout 超时时间
     * @param unit        时间单位
     * @return
     */
    @Override
    public Observable<BleConnectState> scanAndConnect(final String mac, long scanTimeout, TimeUnit unit) {

        return scanBleDeviceByMac(mac, scanTimeout, unit)
                .flatMap(new Function<BleScanResult, ObservableSource<BleConnectState>>() {
                    @Override
                    public ObservableSource<BleConnectState> apply(@NonNull BleScanResult bleScanResult) throws Exception {
                        BluetoothDevice device = bleScanResult.getBleDevice();
                        String address = device == null ? mac : device.getAddress();
                        return connect(address);
                    }
                });
    }

    /**
     * 指定设备扫描、连接并发送数据
     *
     * @param mac         指定设备的MAC地址
     * @param command     数据
     * @param scanTimeout 超时时间
     * @param unit        时间单位
     * @return
     */
    @Override
    public Observable<BluetoothGattCharacteristic> scanConnectAndSend(final String mac, final String command, long scanTimeout, TimeUnit unit) {
        return scanBleDeviceByMac(mac, scanTimeout, unit)
                .flatMap(new Function<BleScanResult, ObservableSource<BluetoothGattCharacteristic>>() {
                    @Override
                    public ObservableSource<BluetoothGattCharacteristic> apply(@NonNull BleScanResult bleScanResult) throws Exception {
                        BluetoothDevice device = bleScanResult.getBleDevice();
                        String address = device == null ? mac : device.getAddress();
                        return connectAndSend(address, command);
                    }
                });
    }

    @Override
    public void releaseScanner() {
        mBLeDeviceScanner.release();
    }

    @Override
    public void releaseConnector() {
        mBleConnectionConnector.release();
    }

    /**
     * 停止扫描，关闭连接，释放资源
     */
    @Override
    public void release() {
        mBLeDeviceScanner.release();
        mBleConnectionConnector.release();
    }
}
