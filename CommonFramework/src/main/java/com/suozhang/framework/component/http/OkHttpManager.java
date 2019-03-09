/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.http;

import android.text.TextUtils;
import android.util.Log;

import com.suozhang.framework.entity.bo.Result;
import com.suozhang.framework.entity.bo.TokenInfo;
import com.suozhang.framework.framework.AM;
import com.suozhang.framework.framework.api.CommonApi;
import com.suozhang.framework.utils.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author Moodd(420410175[at]qq.com)
 * @date 2017/3/16 16:41
 */

public class OkHttpManager {
    /**
     * 头部参数，授权key,需要授权的接口加上token信息
     */
    private final String HEAD_AUTHORIZATION = "zkAuthorization";
    /**
     * 头部参数，必填，标识客户端类型
     */
    private final String HEAD_APP_TYPE = "AppType";

    /**
     * 构建OkHttp客户端
     *
     * @return
     */
    public OkHttpClient buildOkHttpClient() {

        return new OkHttpClient.Builder()
                .cache(cache)
                .retryOnConnectionFailure(true)//设置出现错误进行重新连接
                .addInterceptor(loggingInterceptor)//日志拦截器
                .addNetworkInterceptor(tokenInterceptor)//添加token拦截器
                .authenticator(authenticator)//未授权拦截器，返回401重新获取token
                .connectTimeout(30, TimeUnit.SECONDS)//超时设置
                .readTimeout(60, TimeUnit.SECONDS)//超时设置
                .writeTimeout(60, TimeUnit.SECONDS)//超时设置
                .build();
    }

    // 指定缓存路径,缓存大小100Mb
    private Cache cache = new Cache(new File(AM.app().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
    //日志拦截器
    private HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            Log.d("DebugLog-http", message);
        }
    }).setLevel(AM.isDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

    private Interceptor tokenInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();

            String token = AM.user().getToken();
            // NOTE: 2017/10/31 修正"null"值  ，如果token为null,则传入""
            token = TextUtils.isEmpty(token) ? "" : token;

            String appType = AM.api().getAppType() + "";

            Request authorised = originalRequest.newBuilder()
                    .header(HEAD_AUTHORIZATION, token)
                    .header(HEAD_APP_TYPE, appType)
                    .build();
            return chain.proceed(authorised);
        }
    };
    //认证401重新刷新Token
    private Authenticator authenticator = new Authenticator() {
        @Override
        public Request authenticate(Route route, Response response) throws IOException {

            //取出本地的refreshToken
            String refreshToken = AM.user().getRefreshToken();
            if (refreshToken == null) {
                return null;
            }
            //重试
            int count = responseCount(response);
            if (count >= 3) {
                return null;
            }

            // 通过一个特定的接口获取新的token，此处要用到同步的retrofit请求
            String newToken = getNewToken(refreshToken);
            if (newToken == null) {
                return null;
            }

            return response.request().newBuilder()
                    .header(HEAD_AUTHORIZATION, newToken)
                    .build();
        }

        private String getNewToken(String refreshToken) {

            CommonApi service = AM.api().createApiService(CommonApi.class);
            Observable<Result<TokenInfo>> observable = service.refreshToken(refreshToken);

            TokenInfo tokenInfo = null;
            //要用retrofit的同步方式
            try {
                tokenInfo = observable.compose(RxDataProcessFactory.<TokenInfo>dataPrepTransformer()).blockingLast();
            } catch (Exception e) {
                Logger.e(e, "同步请求获取新Token出错");
            }

            String newToken = null;

            if (tokenInfo != null) {
                newToken = tokenInfo.getAccessToken();
                //缓存新token
                AM.user().refreshToken(tokenInfo);
            }
            return newToken;
        }

        private int responseCount(Response response) {
            int result = 1;
            while ((response = response.priorResponse()) != null) {
                result++;
            }
            return result;
        }
    };
}
