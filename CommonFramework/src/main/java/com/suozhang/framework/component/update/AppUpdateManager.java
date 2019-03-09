/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.update;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.suozhang.framework.entity.bo.AppUpdateInfo;
import com.suozhang.framework.framework.AM;
import com.suozhang.framework.utils.AppUtil;
import com.suozhang.framework.utils.NetUtil;
import com.suozhang.framework.utils.T;
import com.suozhang.framework.utils.logger.Logger;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 项目名称：
 * 类描述：应用更新管理
 * 创建人：Moodd
 * 创建时间：2016/6/12 16:07
 * 修改人：Moodd
 * 修改时间：2016/12/30 10:06
 * 修改备注：
 * <p>
 * 外部调用checkUpdate()方法检查更新，调用方法时会先判断下载服务是否启动，如未启动则会先启动下载服务再检查更新
 */
public class AppUpdateManager {

    /**
     * 系统应用类型--安卓版本
     * 应用更新时，systemType参数，标示当前需更新的应用为：android应用
     */
    private static final String SYSTEM_TYPE = "android";//ios

    //检查更新数据
    public static final String CHECK_UPDATE_DATA = "com.suozhang.CHECK_UPDATE_DATA";

    private String mHotelId;//酒店用户升级需要传入此参数，用于区分不同酒店的App

    private static AppUpdateManager instance;

    private AppUpdateModule updateModule;

    private AppUpdateManager() {
        updateModule = new AppUpdateModule();
    }

    public static AppUpdateManager getInstance() {
        if (instance == null) {
            synchronized (AppUpdateManager.class) {
                if (instance == null) {
                    instance = new AppUpdateManager();
                }
            }
        }
        return instance;
    }


    /**
     * 检查版本更新-WIFI网络下不自动升级，弹出提示框
     *
     * @param context
     */
    public void checkUpdate(Context context) {
        this.checkUpdate(context, false, null);
    }


    /**
     * 检查版本更新-WIFI网络下不自动升级，弹出提示框
     *
     * @param context
     * @param hotelId 酒店用户app升级需要传入此参数，用于区分不同酒店的App
     */
    public void checkUpdate(Context context, String hotelId) {

        this.checkUpdate(context, false, hotelId);
    }

    /**
     * 检查版本更新
     *
     * @param context
     * @param isAutoUpdate WIFI网络下是否自动升级
     */
    public void checkUpdate(Context context, boolean isAutoUpdate) {
        this.checkUpdate(context, isAutoUpdate, null);
    }

    /**
     * 检查版本更新
     *
     * @param context
     * @param isAutoUpdate WIFI网络下是否自动升级
     * @param hotelId      酒店用户app升级需要传入此参数，用于区分不同酒店的App
     */
    public void checkUpdate(Context context, boolean isAutoUpdate, String hotelId) {

        setHotelId(hotelId);

        boolean isServiceRunning = AppUtil.isServiceRunning(context, AppUpdateService.class);
        if (!isServiceRunning) {
            //更新服务未启动，启动服务立即检查更新
            Logger.i("服务未启动----启动服务，检查更新");
            startAppUpdateServer(context, true, isAutoUpdate);
        } else {
            //从服务端获取更新信息，检查更新
            Logger.i("服务已启动------->检查更新");
            checkUpdateFromServer(context, isAutoUpdate);
        }
    }

    /**
     * 从服务端获取应用更新信息
     *
     * @param context
     */
    public Observable<AppUpdateInfo> getAppUpdateInfo(Context context) {
        return this.getAppUpdateInfo(context, null);
    }

    /**
     * 从服务端获取应用更新信息
     *
     * @param context
     * @param hotelId 酒店用户app升级需要传入此参数，用于区分不同酒店的App
     */
    public Observable<AppUpdateInfo> getAppUpdateInfo(Context context, String hotelId) {

        setHotelId(hotelId);

        String channel = AppUtil.getAppMetaData(context, "UMENG_CHANNEL");

        channel = TextUtils.isEmpty(channel) ? "suozhang" : channel;

        int appType = AM.api().getAppType();
        String osType = SYSTEM_TYPE;
        int versionCode = AppUtil.getVersionCode(context);
        String versionName = AppUtil.getVersionName(context);
        Logger.i("本地App信息appType = " + appType + " channel = " + channel + " versionCode = " + versionCode + " versionName = " + versionName);

        AppUpdateInfo info = new AppUpdateInfo();
        info.setAppType(appType);
        info.setOsType(osType);//android ,ios
        info.setChannelId(channel);
        info.setVersionCode(versionCode);
        info.setVersionName(versionName);

        info.setHotelId(mHotelId);

        return updateModule.getAppUpdateInfo(info);
    }

    private void setHotelId(String mHotelId) {
        this.mHotelId = mHotelId;
    }

    /**
     * 从服务端获取更新信息，检查版本更新
     *
     * @param isAutoUpdate WIFI网络下是否自动升级
     */

    /*package*///包访问权限
    void checkUpdateFromServer(final Context context, final boolean isAutoUpdate) {
        getAppUpdateInfo(context, mHotelId).subscribe(new Observer<AppUpdateInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                Logger.e("onSubscribe-----");
            }

            @Override
            public void onNext(AppUpdateInfo result) {
                if (result == null) {
                    // T.showShort(mContext,"获取版本更新数据失败");
                    return;
                }
                update(context, result, isAutoUpdate);
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, e.getMessage());
            }

            @Override
            public void onComplete() {
                Logger.e("onComplete-----");
            }
        });
    }

    /**
     * 更新
     *
     * @param context
     * @param serverInfo
     * @param isAutoUpdate
     */
    private void update(Context context, AppUpdateInfo serverInfo, boolean isAutoUpdate) {
        if (serverInfo == null) {
            return;
        }
        String url = serverInfo.getUrl();

        if (TextUtils.isEmpty(url)) {
            T.showShort("下载地址有误，升级失败");
            return;
        }
        int serverVer = serverInfo.getVersionCode();
        int localVer = AppUtil.getVersionCode(context);

        //网络版本大于本地版本才提示升级
        if (serverVer > localVer) {
            //WIFI下自动升级
            if (isAutoUpdate && NetUtil.isWifi(context)) {
                //自动升级-发送下载广播
                sendDownloadBroadcast(context, url);
            } else {
                //弹出升级提示窗口
                showUpdateTipsDialog(context, serverInfo);
            }
        }
    }

    /**
     * 启动APP更新服务
     *
     * @param context
     * @param isCheckUpdate 是否检查更新
     * @param isAutoUpdate  WIFI网络下是否自动更新
     */
    private void startAppUpdateServer(Context context, boolean isCheckUpdate, boolean isAutoUpdate) {
        //启动更新服务
        Intent intent = new Intent(context, AppUpdateService.class);
        intent.putExtra(AppUpdateService.IS_CHECK_UPDATE, isCheckUpdate);
        intent.putExtra(AppUpdateService.IS_AUTO_UPDATE, isAutoUpdate);
        context.startService(intent);
    }

    /**
     * 发送检查版本更新广播
     *
     * @param context
     * @param url     升级地址
     */
     /*package*/ void sendDownloadBroadcast(Context context, String url) {
        //在服务中下载更新,广播接受传参
        Intent intent = new Intent(AppUpdateService.ACTION_DOWNLOAD);
        intent.putExtra(AppUpdateService.ACTION_DOWNLOAD, url);
        //发送应用中有效的广播
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 弹出更新提示窗口
     *
     * @param context
     * @param info    更新信息
     */
    private void showUpdateTipsDialog(Context context, AppUpdateInfo info) {
        Intent intent = new Intent(context, AppUpdateTipsActivity.class);
        intent.putExtra(CHECK_UPDATE_DATA, info);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
