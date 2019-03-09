/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.yiqi.zhiyuan.framework.api.dependencies;


import com.suozhang.framework.framework.AM;
import com.suozhang.framework.framework.annotation.ApiScope;

import com.yiqi.zhiyuan.framework.api.UserApi;
import dagger.Module;
import dagger.Provides;

/**
 * @author Moodd
 * @email 420410175@qq.com
 * @date 2017/4/19 10:29
 */

@Module
public class UserApiModule {
    @ApiScope
    @Provides
    public UserApi providerUserCenterApi() {

        return AM.api().createApiService(UserApi.class);
    }
}
