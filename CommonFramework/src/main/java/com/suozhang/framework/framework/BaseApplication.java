/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.framework;

import android.app.Application;

import com.suozhang.framework.component.app.AppComponent;

/**
 * @author Moodd(420410175[at]qq.com)
 * @date 2017/3/14 14:29
 */

 class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
       // AM.init(this,true);
       // Stetho.initializeWithDefaults(this);
    }

    /**
     * 向外提供appComponent,方便其他依赖appComponent的component构建
     *
     * @return
     */
    public  AppComponent appComponent() {
        return AM.appComponent();
    }

}
