/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.framework;

import android.app.Application;
import android.content.res.Resources;
import android.text.TextUtils;

import com.suozhang.framework.component.app.AppComponent;
import com.suozhang.framework.component.app.AppManager;
import com.suozhang.framework.component.app.AppModule;
import com.suozhang.framework.component.app.DaggerAppComponent;
import com.suozhang.framework.component.ble.BleClient;
import com.suozhang.framework.component.ble.BleClientImpl;
import com.suozhang.framework.component.http.ApiManager;
import com.suozhang.framework.component.http.ExceptionProcessFactory;
import com.suozhang.framework.component.image.ImageManager;
import com.suozhang.framework.component.image.ImageManagerImpl;
import com.suozhang.framework.component.uesr.UserController;
import com.suozhang.framework.component.update.AppUpdateManager;
import com.suozhang.framework.utils.logger.LogLevel;
import com.suozhang.framework.utils.logger.Logger;


/**
 * Application Manager APP管理类
 * 提供全局静态功能访问入口
 *
 * @author Moodd
 * @date 2017/3/9
 */

public class AM {
    /**
     * 用于隐式启动登录界面的Action,登录界面需配置此Action方可有效
     * <p>
     * packageName + ".intent.action.LOGIN"
     * 参见：
     * <p>
     * {@link ExceptionProcessFactory#showLoginActivity()}
     */
    private static String ACTION_LOGIN;

    private static AppComponent appComponent;

    /**
     * 登录提示,设为false 401时不再弹出提示窗口
     */
    private static boolean isLoginTips = true;

    private static boolean isAllowSetLoginTips = true;

    /**
     * 调试模式控制
     */
    private static boolean isDebug = true;

    /**
     * 初始化
     *
     * @param app
     * @param isDebug
     */
    public static void init(Application app, boolean isDebug) {
        buildComponent(app);
        setDebug(isDebug);
        initLog("DebugLog", isDebug);
    }


    public static String getLoginAction() {
        if (TextUtils.isEmpty(ACTION_LOGIN)) {
            initLoginAction(AM.app());
        }
        return ACTION_LOGIN;
    }

    private static void initLoginAction(Application app) {
        String packageName = app.getPackageName();
        ACTION_LOGIN = packageName + ".intent.action.LOGIN";
    }

    /**
     * 注入App AppManager的注入
     */
    public static void buildComponent(Application app) {
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(app)).build();
    }

    /**
     * 向外提供appComponent,方便其他依赖appComponent的component构建
     *
     * @return
     */
    public static AppComponent appComponent() {
        return appComponent;
    }

    /**
     * 是否登录提示
     *
     * @return
     */
    public static boolean isLoginTips() {
        return isLoginTips;
    }

    /**
     * 设为false 401时不再弹出提示窗口
     *
     * @param isTips
     */
    public static void setLoginTips(boolean isTips) {
        if (isAllowSetLoginTips) {
            AM.isLoginTips = isTips;
        } else {
            AM.isLoginTips = false;
        }

    }

    /**
     * 允许设置登录过期提示
     *
     * @param isAllow
     */
    public static void allowSetLoginTips(boolean isAllow) {
        AM.isAllowSetLoginTips = isAllow;
    }


    /**
     * 设置调试模式 发布时设为false
     *
     * @param isDebug
     */
    private static void setDebug(boolean isDebug) {
        AM.isDebug = isDebug;
    }

    /**
     * 是否调试模式，控制Log等
     *
     * @return
     */
    public static boolean isDebug() {
        return AM.isDebug;
    }

    /**
     * 设置日志打印控制
     *
     * @param tag     TAG
     * @param isDebug true:打印日志，false:不打印
     */
    private static void initLog(String tag, boolean isDebug) {
        Logger.init(tag).logLevel(isDebug ? LogLevel.FULL : LogLevel.NONE);
    }

    public static Application app() {
        return appComponent.getApp();
    }

    /**
     * AppManager 提供Activity栈管理功能
     *
     * @return
     */
    public static AppManager appManager() {
        return appComponent.getAppManager();
    }

    /**
     * 获取Resources
     */
    public static Resources res() {
        return app().getResources();
    }

    /**
     * Retrofit 网络请求管理
     *
     * @return
     */
    public static ApiManager api() {
        return appComponent.getApiManager();

    }

    /**
     * 用户管理
     *
     * @return
     */
    public static UserController user() {
        return UserController.getInstance();
    }

    /**
     * 应用更新管理
     *
     * @return
     */
    public static AppUpdateManager appUpdate() {
        return AppUpdateManager.getInstance();
    }


    /**
     * 图片加载
     *
     * @return
     */
    public static ImageManager image() {
        return ImageManagerImpl.getInstance();
    }

    /**
     * BLE蓝牙操作
     *
     * @return
     */
    public static BleClient ble() {
        return BleClientImpl.getInstance(app());
    }


}
