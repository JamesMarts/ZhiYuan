/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.framework.api;

import com.suozhang.framework.framework.AM;
import com.suozhang.framework.framework.annotation.ApiScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author Moodd
 * @email 420410175@qq.com
 * @date 2017/3/30 8:49
 */
@Module
public class CommonApiModule {
    @ApiScope
    @Provides
    public CommonApi providerCommonApi() {
        return AM.api().createApiService(CommonApi.class);
    }
}
