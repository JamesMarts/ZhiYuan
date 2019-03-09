/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.component.app;

import com.suozhang.framework.component.http.ApiManager;
import com.suozhang.framework.component.http.OkHttpManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 *
 * @author Moodd(420410175[at]qq.com)
 * @date 2017/3/15 10:23
 */
@Module
public class ApiManagerModule {

    @Provides
    @Singleton
    public ApiManager provideApiManager(OkHttpClient okHttpClient) {
        return new ApiManager(okHttpClient);
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {

        return new OkHttpManager().buildOkHttpClient();
    }
}
