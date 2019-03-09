/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.app;

import android.app.Application;

import com.suozhang.framework.component.http.ApiManager;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * @author Moodd(420410175[at]qq.com)
 * @date 2017/3/14 11:55
 */
@Singleton
@Component(modules = {AppModule.class, AppManagerModule.class, ApiManagerModule.class})
public interface AppComponent {

    // void inject(BaseApplication app);

    Application getApp();

    AppManager getAppManager();

    OkHttpClient getOkHttpClient();

    ApiManager getApiManager();
}
