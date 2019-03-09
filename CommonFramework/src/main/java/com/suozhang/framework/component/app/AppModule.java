/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.app;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Moodd(420410175[at]qq.com)
 * @date 2017/3/14 14:59
 */
@Module
public class AppModule {
    private Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Application provideApp() {
        return this.app;
    }
}
