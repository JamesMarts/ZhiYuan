/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.component.ble;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Process;

/**
 * @author Moodd
 * @email 420410175@qq.com
 * @date 2017/4/12 16:35
 */
public class LocationServicesStatus {
    private Context context;
    private LocationManager locationManager;

    public LocationServicesStatus(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    }

    /**
     * 位置权限是否OK
     *
     * @return
     */
    public boolean isLocationPermissionOk() {

        return !isLocationPermissionGrantedRequired() || isLocationPermissionGranted();
    }

    /**
     * 位置服务是否开启
     *
     * @return
     */
    public boolean isLocationProviderOk() {
        return !isLocationProviderEnabledRequired() || isLocationProviderEnabled();
    }

    /**
     * 是否必须有位置权限
     *
     * @return
     */
    private boolean isLocationPermissionGrantedRequired() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 是否必须启用位置提供
     *
     * @return
     */
    private boolean isLocationProviderEnabledRequired() {
        return targetSdk() >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 是否有位置权限
     *
     * @return
     */
    private boolean isLocationPermissionGranted() {
        return isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
                || isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * 检查权限
     *
     * @param permission
     * @return
     */
    private boolean isPermissionGranted(String permission) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }

        return context.checkPermission(permission, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isLocationProviderEnabled() {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private int targetSdk() {
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).targetSdkVersion;
        } catch (Throwable catchThemAll) {
            return Integer.MAX_VALUE;
        }
    }

}
