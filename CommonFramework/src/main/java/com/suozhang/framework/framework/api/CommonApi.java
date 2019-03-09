/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.framework.api;

import com.suozhang.framework.entity.bo.AppUpdateInfo;
import com.suozhang.framework.entity.bo.LoginRequest;
import com.suozhang.framework.entity.bo.UserAccount;
import com.suozhang.framework.entity.bo.Result;
import com.suozhang.framework.entity.bo.TokenInfo;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 公共Api接口
 *
 * @author Moodd(420410175[at]qq.com)
 * @date 2017/3/17 9:17
 */

public interface CommonApi {

    /**
     * 登录接口 返回值TokenInfo泛型指定为String 可变数据，自行解析
     *
     * @param loginRequest
     * @return
     */
    @POST("api/authorization/login")
    Observable<Result<TokenInfo>> login(@Body LoginRequest<UserAccount> loginRequest);

    /**
     * 刷新Token接口 返回值TokenInfo泛型指定为String 可变数据，自行解析
     *
     * @param refreshToken
     * @return
     */
    @GET("api/authorization/refreshToken/{refreshToken}")
    Observable<Result<TokenInfo>> refreshToken(@Path("refreshToken") String refreshToken);

    /**
     * 应用更新接口
     *
     * @param updateInfo
     * @return
     */
    @POST("api/System/v1/CheckNewVersion")
    Observable<Result<AppUpdateInfo>> appUpdate(@Body AppUpdateInfo updateInfo);

}
