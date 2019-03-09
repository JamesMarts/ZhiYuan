package com.yiqi.zhiyuan.framework;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;


import com.suozhang.framework.component.http.Host;
import com.suozhang.framework.entity.enums.AppType;
import com.suozhang.framework.framework.AM;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.yiqi.zhiyuan.BuildConfig;
import com.yiqi.zhiyuan.R;
import com.yiqi.zhiyuan.framework.api.ApiLib;

//import com.meituan.android.walle.WalleChannelReader;

/**
 * 自定义ApplicationLike类.
 * <p>
 * 注意：这个类是Application的代理类，以前所有在Application的实现必须要全部拷贝到这里<br/>
 *
 * @author wenjiewu
 * @since 2016/11/7
 */
public class SampleApplicationLike extends DefaultApplicationLike {

    public SampleApplicationLike(Application application, int tinkerFlags,
                                 boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                                 long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        initBugly(getApplication());
        init(getApplication());
    }

    private void initBugly(Context context) {

        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁，默认为true
        Beta.canAutoDownloadPatch = true;
        // 设置是否自动合成补丁，默认为true
        Beta.canAutoPatch = true;
        // 设置是否提示用户重启，默认为false
        Beta.canNotifyUserRestart = true;
        // 设置自定义升级对话框UI布局
        Beta.upgradeDialogLayoutId = R.layout.activity_app_update_tips_bugly;
        // 设置自定义tip弹窗UI布局
        Beta.tipsDialogLayoutId = R.layout.dialog_tips_bugly;

        // 设置开发设备，默认为false，上传补丁如果下发范围指定为“开发设备”，需要调用此接口来标识开发设备
        Bugly.setIsDevelopmentDevice(context, false);
        // 多渠道需求塞入
        // String channel = WalleChannelReader.getChannel(getApplication());
        // Bugly.setAppChannel(getApplication(), channel);

        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        Bugly.init(context, "c9d236b681", false);
    }

    private void init(Application app) {
        //debug模式设置
        boolean isDebug = BuildConfig.DEBUG;
        //内存泄露检测工具
        initLeakCanary(app, isDebug);

        //初始化
        AM.init(app, isDebug);
        //设置主机头
        AM.api().setHost(Host.ONLINE);
        //初始化AppType 所有请求头部参数均会带入，用以区分请求类型
        AM.api().setAppType(AppType.CLOUD_MANAGER);

        //初始化Api注入器，为api实现提供全局单例
        ApiLib.initApiComponent();

        //初始化第三方库
        new LibManager().init(app);

    }

    /**
     * 初始化内存泄露检测工具
     *
     * @param app
     */
    private void initLeakCanary(Application app, boolean isDebug) {
        if (!isDebug) {
            return;
        }
//        if (LeakCanary.isInAnalyzerProcess(app)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(app);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        Beta.installTinker(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(
            Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Beta.unInit();
    }
}
