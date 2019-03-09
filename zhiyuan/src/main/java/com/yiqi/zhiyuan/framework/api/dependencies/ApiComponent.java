/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.yiqi.zhiyuan.framework.api.dependencies;


import com.suozhang.framework.framework.annotation.ApiScope;
import com.suozhang.framework.framework.api.CommonApi;
import com.suozhang.framework.framework.api.CommonApiModule;

import com.yiqi.zhiyuan.framework.api.UserApi;
import dagger.Component;

/**
 * 所有Api的依赖注入器，在Application中初始化，并提供api的全局单例，可添加多个module,在子Component中依赖，并注入相应的依赖中
 *
 * @author Moodd
 * @email 420410175@qq.com
 * @date 2017/3/30 8:56
 */
@ApiScope
@Component(modules = {
        CommonApiModule.class,
        UserApiModule.class

})
public interface ApiComponent {

    CommonApi getCommonApi();


    UserApi getUserApi();

}
