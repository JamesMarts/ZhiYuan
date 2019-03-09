/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.update;


import com.suozhang.framework.framework.AM;
import com.suozhang.framework.framework.api.CommonApi;
import com.suozhang.framework.component.http.RxDataProcessFactory;
import com.suozhang.framework.entity.bo.AppUpdateInfo;

import io.reactivex.Observable;

/**
 * 项目名称：
 * 类描述：
 * 创建人：Moodd
 * 创建时间：2016/5/4 10:38
 * 修改人：Moodd
 * 修改时间：2016/5/4 10:38
 * 修改备注：
 */
public class AppUpdateModule {

    private CommonApi service;

    public AppUpdateModule() {
        service = AM.api().createApiService(CommonApi.class);
    }

    /**
     * 获取应用更新信息
     */
    public Observable<AppUpdateInfo> getAppUpdateInfo(AppUpdateInfo appUpdateInfo) {
        return service.appUpdate(appUpdateInfo).compose(RxDataProcessFactory.<AppUpdateInfo>dataPrepAndIoToMainTransformer());
    }

}
