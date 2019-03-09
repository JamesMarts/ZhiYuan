/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.component.ble;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;


/**
 * 蓝牙开关状态监听
 */
 class BleAdapterStateObservable {

    public static class BleAdapterState {

        public static final BleAdapterState STATE_ON = new BleAdapterState(true);
        public static final BleAdapterState STATE_OFF = new BleAdapterState(false);
        public static final BleAdapterState STATE_TURNING_ON = new BleAdapterState(false);
        public static final BleAdapterState STATE_TURNING_OFF = new BleAdapterState(false);

        private final boolean isUsable;

        private BleAdapterState(boolean isUsable) {
            this.isUsable = isUsable;
        }

        public boolean isUsable() {
            return isUsable;
        }
    }

    public static Observable<BleAdapterState> bleAdapterStateObservable(final Context context) {
        return Observable.create(new ObservableOnSubscribe<BleAdapterState>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<BleAdapterState> emitter) throws Exception {
                final BroadcastReceiver receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();

                        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                            emitter.onNext(mapToBleAdapterState(state));
                        }
                    }
                };
                context.registerReceiver(receiver, createFilter());
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        context.unregisterReceiver(receiver);
                    }
                });
            }
        });
    }

    private static BleAdapterState mapToBleAdapterState(int state) {

        switch (state) {
            case BluetoothAdapter.STATE_ON:
                return BleAdapterState.STATE_ON;
            case BluetoothAdapter.STATE_TURNING_ON:
                return BleAdapterState.STATE_TURNING_ON;
            case BluetoothAdapter.STATE_TURNING_OFF:
                return BleAdapterState.STATE_TURNING_OFF;
            case BluetoothAdapter.STATE_OFF:
            default:
                return BleAdapterState.STATE_OFF;
        }
    }

    private static IntentFilter createFilter() {
        return new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
    }
}
