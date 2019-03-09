/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.component.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import com.suozhang.framework.component.ble.ex.BleException;
import com.suozhang.framework.utils.logger.Logger;

import java.util.Arrays;
import java.util.UUID;
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
import io.reactivex.subjects.Subject;

/**
 * BLE蓝牙连接器
 *
 * @author Moodd
 * @email 420410175@qq.com
 * @date 2017/4/10 10:17
 */
final class BleConnectionConnector {

    //发送通知的描述UUID descriptor
    public UUID UUID_NOTIFICATION_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    //服务的UUID
    // public UUID UUID_SERVICE = UUID.fromString("0000FEE7-0000-1000-8000-00805f9b34fb");//微信
    public UUID UUID_SERVICE = UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb");
    //写数据
    public UUID UUID_CHAR_WRITE = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    //notify方式 读取。
    public UUID UUID_CHAR_READ = UUID.fromString("0000fff4-0000-1000-8000-00805f9b34fb");

    private final Context mContext;
    private final BleAdapterWrapper mBleAdapterWrapper;

    private volatile BluetoothGatt mBluetoothGatt;


    //将最新的数据发送给订阅者，订阅者只会接受到最新的数据
    private final Subject<BleConnectState> publishSubject = PublishSubject.<BleConnectState>create().toSerialized();


    public BleConnectionConnector(Context context, BleAdapterWrapper bleAdapterWrapper) {
        this.mContext = context;
        this.mBleAdapterWrapper = bleAdapterWrapper;
    }

    private Observable<BleConnectState> asObservable() {
        return publishSubject.hide();
    }

    private synchronized void onNext(BleConnectState result) {
        publishSubject.onNext(result);
    }


    /**
     * 连接错误 关闭连接，传递连接断开状态
     */
    private synchronized void connectError(BluetoothGatt gatt) {
        //传递连接断开状态
        onNext(new BleConnectState(BleConnectState.STATE_DISCONNECTED, gatt, null));
    }

    /**
     * 连接设备
     *
     * @param device
     * @throws Exception
     */
    private synchronized void connectDevice(BluetoothDevice device) throws Exception {
        //连接设备
        mBluetoothGatt = device.connectGatt(mContext, false, bluetoothGattCallback);

        //校验
        if (mBluetoothGatt == null) {
            throw (new BleException(BleException.BLUETOOTH_GATT_CONNECT_ERROR));
        }
    }

    /**
     * 关闭连接，释放资源
     */
    public synchronized void close() {
        if (mBluetoothGatt != null) {
            // NOTE: 2017/4/12  调用disconnect(),会造成gatt回调中连接状态发生改变；默认处理：当连接断开时，调用close()关闭连接，释放资源；
            // NOTE: 2017/4/12  当某些机型中出现异常时，可尝试调用BluetoothGatt.disconnect()、BluetoothGatt.close()，并处理连接状态改变后的动作
            // mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        close();
    }

    /**
     * 连接并发送数据
     *
     * @param device
     * @param command
     * @return
     */
    public synchronized Observable<BluetoothGattCharacteristic> connectAndSend(BluetoothDevice device, final String command) {
        //校验
      /*  if (device == null || TextUtils.isEmpty(command)) {
            Logger.d("device或command不能为null");
            return Observable.error(new BleException(BleException.BLUETOOTH_GATT_CONNECT_ERROR));
        }*/

        return connectOnInternal(device)
                .flatMap(new Function<BleConnectState, ObservableSource<BluetoothGattCharacteristic>>() {
                    @Override
                    public ObservableSource<BluetoothGattCharacteristic> apply(@NonNull BleConnectState state) throws Exception {
                        return sendDataOnInternal(state, command);
                    }
                }).doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        //关闭
                        close();
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        //关闭
                        close();
                    }
                });
    }


    /**
     * 连接设备
     *
     * @param device
     * @return
     */
    public synchronized Observable<BleConnectState> connect(BluetoothDevice device) {
        return connectOnInternal(device)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        //关闭
                        close();
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        //关闭
                        close();
                    }
                });
    }

    /**
     * 连接设备 内部使用
     *
     * @param device
     * @return
     */
    private synchronized Observable<BleConnectState> connectOnInternal(final BluetoothDevice device) {
        //校验
        if (device == null) {
            return Observable.error(new BleException(BleException.BLUETOOTH_GATT_CONNECT_ERROR));
        }

        return filterConnectStateObservable(BleConnectState.STATE_CONNECTED)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        // NOTE: 2017/12/4  连接之前，关闭释放上一次连接资源
                        close();

                        //连接设备
                        try {
                            connectDevice(device);
                        } catch (Exception e) {
                            //失败抛出异常
                            Logger.e(e, "连接设备失败");
                            throw (new BleException(BleException.BLUETOOTH_GATT_CONNECT_ERROR));
                        }
                    }
                });
    }

    /**
     * 发送数据
     */
    public synchronized Observable<BluetoothGattCharacteristic> sendData(String command) {
        BleConnectState state = new BleConnectState(
                BleConnectState.STATE_CONNECTED,
                mBluetoothGatt,
                getCharacteristic(mBluetoothGatt, UUID_CHAR_WRITE));

        return sendData(state, command);
    }

    /**
     * 发送数据
     *
     * @param state
     * @param command
     */
    public synchronized Observable<BluetoothGattCharacteristic> sendData(BleConnectState state, String command) {
        return sendDataOnInternal(state, command)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        //关闭
                        close();
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        //关闭
                        close();
                    }
                });
    }

    /**
     * 发送数据
     *
     * @param state
     * @param command
     */
    private synchronized Observable<BluetoothGattCharacteristic> sendDataOnInternal(BleConnectState state, String command) {
        Observable<BleConnectState> sendDataObservable = sendDataObservable(state, command);

        return filterConnectStateObservable(BleConnectState.STATE_READ_DATA)
                .mergeWith(sendDataObservable)
                //超时
                // .mergeWith(timeoutObservable(20,TimeUnit.SECONDS))
                .flatMap(new Function<BleConnectState, ObservableSource<BluetoothGattCharacteristic>>() {
                    @Override
                    public ObservableSource<BluetoothGattCharacteristic> apply(@NonNull BleConnectState bleConnectState) throws Exception {

                        return Observable.just(bleConnectState.getCharacteristic());
                    }
                });
    }

    /**
     * 过滤连接断开的状态，将之转换为异常，不拦截指定的状态
     *
     * @param allowState 允许的状态，不拦截
     * @return 过滤后的Observable
     */
    private Observable<BleConnectState> filterConnectStateObservable(@BleConnectState.State final int allowState) {
        return asObservable()
                .filter(new Predicate<BleConnectState>() {
                    @Override
                    public boolean test(@NonNull BleConnectState bleConnectState) throws Exception {
                        if (BleConnectState.STATE_DISCONNECTED == bleConnectState.getState()) {
                            throw new BleException(BleException.BLUETOOTH_GATT_CONNECT_ERROR);
                        } else if (allowState == bleConnectState.getState()) {
                            return true;
                        }
                        return false;
                    }
                });
    }

    /**
     * 超时
     *
     * @param timeout
     * @param unit
     * @return
     */
    private Observable<BleConnectState> timeoutObservable(long timeout, TimeUnit unit) {
        return Observable.timer(timeout, unit).flatMap(new Function<Long, ObservableSource<BleConnectState>>() {
            @Override
            public ObservableSource<BleConnectState> apply(@NonNull Long aLong) throws Exception {

                return Observable.error(new BleException(BleException.BLUETOOTH_TIME_OUT));
            }
        });
    }

    /**
     * 发送数据
     * <p>
     * 门锁要求，每次发送20个字节，不足20，在后面补0或F，超过20字节，分多次发送，协议中数据包最大长度为32字节；
     * <p>
     * 所以，对任何数据包，均补足40字节，超过40的不补码，以实际长度为准，数据分多次次发送，每次20字节
     *
     * @param state
     * @param writeData
     * @return
     */
    private Observable<BleConnectState> sendDataObservable(final BleConnectState state, String writeData) {
        //数据校验，转换
        final byte[] command = BleCommand.getCommand(writeData, 40);

        if (command == null || command.length == 0) {
            return Observable.error(new BleException(BleException.BLUETOOTH_RAW_DATA_ERROR));
        }
        //计算发送次数，每20字节发一次
        final int packetSize = 20;
        final int commandLength = command.length;
        int count = commandLength / packetSize;
        final int sendCount = commandLength % packetSize == 0 ? count : count + 1;

        Logger.d("数据包信息：commandLength = " + commandLength + " count = " + count + " sendCount = " + sendCount);

        //数据拆包，分2次发送,间隔300ms，当开启通知后，需要延时200ms后发送，否则连续发送数据过快，会造成其他异常，如接受不到数据等
        Observable<BleConnectState> sendDataObservable = Observable.interval(200, 300, TimeUnit.MILLISECONDS)
                .take(sendCount)//发送次数
                .flatMap(new Function<Long, ObservableSource<? extends BleConnectState>>() {
                    @Override
                    public ObservableSource<BleConnectState> apply(@NonNull Long aLong) throws Exception {
                        byte[] subCommand = null;
                        try {
                            int packetStart = (int) (aLong * packetSize);
                            if (aLong == sendCount - 1) {
                                subCommand = Arrays.copyOfRange(command, packetStart, commandLength);
                            } else {
                                int packetEnd = (int) ((aLong + 1) * packetSize);
                                subCommand = Arrays.copyOfRange(command, packetStart, packetEnd);
                            }
                        } catch (Throwable e) {
                            Logger.e(e, "数据拆包出错");
                        }

                        //发送数据
                        if (subCommand != null) {
                            boolean isSuccess = writeCharacteristic(state, subCommand);
                            Logger.d("发送数据 isSuccess = " + isSuccess);
                            if (isSuccess) {
                                //成功
                                return Observable.never();
                            } else {
                                //失败
                                return Observable.error(new BleException(BleException.BLUETOOTH_GATT_SEND_DATA_ERROR));
                            }
                        } else {
                            Logger.d("子数据包有误 subCommand = null");
                            return Observable.error(new BleException(BleException.BLUETOOTH_GATT_SEND_DATA_ERROR));
                        }
                    }
                });
        return sendDataObservable;

    }

    /**
     * 发送数据
     *
     * @param state
     * @param value
     * @return
     */
    private synchronized boolean writeCharacteristic(BleConnectState state, byte[] value) {
        if (state == null) {
            return false;
        }
        BluetoothGatt iGatt = state.getGatt();
        BluetoothGattCharacteristic iCharacteristic = state.getCharacteristic();

        BluetoothGatt gatt = iGatt == null ? mBluetoothGatt : iGatt;
        BluetoothGattCharacteristic characteristic = iCharacteristic == null ? getCharacteristic(gatt, UUID_CHAR_WRITE) : iCharacteristic;
        if (gatt == null || characteristic == null) {
            return false;
        }

        characteristic.setValue(value);
        boolean status = gatt.writeCharacteristic(characteristic);

        return status;
    }

    //蓝牙GATT回调
    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Logger.d("onConnectionStateChange gatt = " + gatt + " status = " + status + " newStatus = " + newState);

            //连接状态改变
            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                //启动搜索服务
                if (gatt != null) {
                    gatt.discoverServices();
                } else {
                    Logger.e("启动搜索服务失败");
                    //断开，释放资源
                    connectError(gatt);
                }
            } else {
                //断开，释放资源
                Logger.e("连接断开");
                connectError(gatt);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Logger.d("onServicesDiscovered gatt = " + gatt + " status = " + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //搜索服务成功，开启通知，当有新数据会通过onCharacteristicChanged()回调返回
                boolean isSuccess = startNotification(gatt);
                Logger.d("开启通知 isSuccess = " + isSuccess);
                if (isSuccess) {
                    //发送数据
                    BluetoothGattCharacteristic characteristic = getCharacteristic(gatt, UUID_CHAR_WRITE);
                    if (characteristic != null) {
                        onNext(new BleConnectState(BleConnectState.STATE_CONNECTED, gatt, characteristic));
                    } else {
                        //断开，释放资源
                        Logger.e("未获取到写数据的BluetoothGattCharacteristic，连接断开");
                        connectError(gatt);
                    }
                } else {
                    Logger.e("开启通知失败");
                    connectError(gatt);
                }
            } else {
                //失败，断开连接
                Logger.e("发现服务失败");
                connectError(gatt);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (UUID_CHAR_READ.equals(characteristic.getUuid())) {
                //读取成功  由远程设备（门锁）通过Notify方式返回
                onNext(new BleConnectState(BleConnectState.STATE_READ_DATA, gatt, characteristic));
            }
        }

    };


    /**
     * 开启通知
     *
     * @return
     */
    private synchronized boolean startNotification(BluetoothGatt gatt) {

        if (gatt == null) {
            return false;
        }

        BluetoothGattCharacteristic txChar = getCharacteristic(gatt, UUID_CHAR_READ);
        if (txChar == null) {
            return false;
        }

        boolean isNotification = gatt.setCharacteristicNotification(txChar, true);
        if (!isNotification) {
            return false;
        }

        BluetoothGattDescriptor descriptor = txChar.getDescriptor(UUID_NOTIFICATION_DESCRIPTOR);
        boolean isWrite = false;
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);//BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
            isWrite = gatt.writeDescriptor(descriptor);
        }

        return isNotification || isWrite;

    }

    /**
     * 根据UUID获取BluetoothGattCharacteristic
     *
     * @param gatt
     * @param characteristicUUID
     * @return
     */
    private BluetoothGattCharacteristic getCharacteristic(BluetoothGatt gatt, UUID characteristicUUID) {
        BluetoothGattCharacteristic characteristic = null;
        try {
            characteristic = gatt.getService(UUID_SERVICE).getCharacteristic(characteristicUUID);
        } catch (Exception e) {
            Logger.e(e, "获取BluetoothGattCharacteristic失败");
        }
        return characteristic;
    }
}
