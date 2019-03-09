/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.app;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Moodd(420410175[at]qq.com)
 * @date 2017/3/14 11:42
 */
@Module
public class AppManagerModule {

    @Provides
    @Singleton
    public AppManager providerAppManager() {
        return new AppManagerImpl();
    }


}
