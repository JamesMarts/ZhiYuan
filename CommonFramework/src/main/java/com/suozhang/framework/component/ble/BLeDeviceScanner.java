/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import com.suozhang.framework.component.ble.ex.BleException;
import com.suozhang.framework.utils.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * BLE蓝牙扫描器
 *
 * @author Moodd
 * @e-mail 420410175@qq.com
 * @date 2017/3/28 15:07
 */

final class BLeDeviceScanner {

    private final BleAdapterWrapper bleAdapterWrapper;

    //是否开始扫描
    private volatile boolean isStarted = false;

    //将最新的数据发送给订阅者，订阅者只会接受到最新的数据
    private final PublishSubject<BleScanResult> publishSubject = PublishSubject.create();

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            //扫描到设备，回调接口
            BleScanResult result = new BleScanResult(BleScanResult.STATE_SCAN_SUCCESS, device, rssi, scanRecord);
            //发送数据
            onNext(result);
        }
    };

    public BLeDeviceScanner(BleAdapterWrapper bleAdapterWrapper) {
        this.bleAdapterWrapper = bleAdapterWrapper;
    }

    /**
     * 获取可观察的Observable
     *
     * @return
     */
    private Observable<BleScanResult> asObservable() {
        return publishSubject.hide();
    }

    /**
     * 创建超时Observable
     *
     * @param timeout
     * @param unit
     * @return
     */
    private Observable<BleScanResult> timeoutObservable(long timeout, TimeUnit unit) {
        return Observable.timer(timeout, unit).flatMap(new Function<Long, ObservableSource<BleScanResult>>() {
            @Override
            public ObservableSource<BleScanResult> apply(@NonNull Long aLong) throws Exception {

                return Observable.just(new BleScanResult(BleScanResult.STATE_SCAN_TIMEOUT));
            }
        }).filter(new Predicate<BleScanResult>() {
            @Override
            public boolean test(@NonNull BleScanResult bleScanResult) throws Exception {
                //如果已经停止扫描，不发送超时状态
                if (isStarted) {
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 扫描指定MAC地址的蓝牙设备
     *
     * @param mac     指定设备的MAC地址
     * @param name     指定设备的名称
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return
     */
    public Observable<BleScanResult> scanBleDevices(final String mac, final String name, long timeout, TimeUnit unit) {

        return asObservable()
                .mergeWith(timeoutObservable(timeout, unit))
                .filter(new Predicate<BleScanResult>() {
                    @Override
                    public boolean test(@NonNull BleScanResult bleScanResult) throws Exception {
                        //过滤扫描对象，失败的抛出异常通知观察者
                        if (BleScanResult.STATE_SCAN_FAILURE == bleScanResult.getState()) {
                            throw new BleException(BleException.BLUETOOTH_SCAN_CANNOT_START);
                        } else if (BleScanResult.STATE_SCAN_TIMEOUT == bleScanResult.getState()) {
                            throw new BleException(BleException.BLUETOOTH_TIME_OUT);
                        }
                        //Logger.d("扫描到设备 = "+bleScanResult.getMac()+" "+bleScanResult.getScanRecordToHex());
                        //过滤出指定MAC的设备，其他全部拦截
                        if (!TextUtils.isEmpty(mac)) {
                            BluetoothDevice device = bleScanResult.getBleDevice();
                            String address = device == null ? null : device.getAddress();
                            if (!TextUtils.isEmpty(address) && mac.equalsIgnoreCase(address)) {
                                stopScan();
                                return true;
                            } else {
                                return false;
                            }
                        }
                        //过滤出指定名称的设备
                        if (!TextUtils.isEmpty(name)) {
                            boolean isFind = bleScanResult.checkName(name);
                            if (isFind){
                                stopScan();
                            }
                            return isFind;
                        }

                        return true;
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        startScan();
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Logger.d("取消订阅了");
                        stopScan();
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Logger.d("扫描出错了");
                        stopScan();
                    }
                });

    }

    /**
     * 开始扫描
     */
    private synchronized void startScan() {
        Logger.d("开始扫描----->");
        try {
            boolean isStartedStatus = bleAdapterWrapper.startLeScan(leScanCallback);
            if (!isStartedStatus) {
                //扫描失败
                onNext(new BleScanResult(BleScanResult.STATE_SCAN_FAILURE));
            } else {
                isStarted = true;
            }
        } catch (Throwable e) {
            isStarted = true;
            Logger.e(e, "开始扫描-->蓝牙扫描异常,请查看错误日志");
            onNext(new BleScanResult(BleScanResult.STATE_SCAN_FAILURE));
        }
    }

    /**
     * 停止扫描
     */
    private synchronized void stopScan() {

        if (isStarted) {
            bleAdapterWrapper.stopLeScan(leScanCallback);

            isStarted = false;
            Logger.d("停止扫描----->");
        }
    }

    private void onNext(BleScanResult result) {
        publishSubject.onNext(result);
    }

    public void release() {
        stopScan();
    }
}
