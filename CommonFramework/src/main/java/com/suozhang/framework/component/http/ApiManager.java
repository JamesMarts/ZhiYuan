/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.http;

import android.support.annotation.NonNull;

import com.suozhang.framework.entity.enums.AppType;
import com.suozhang.framework.framework.AM;
import com.suozhang.framework.component.http.converter.FastJsonConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Api服务管理类，使用Retrofit框架
 * 使用Dagger2注入，保证单例,通过{@link AM#api()}提供统一访问入口
 *
 * @author Moodd(420410175[at]qq.com)
 * @date 2017/3/10
 */

public class ApiManager {
    //默认主机头
    private String mHost = Host.ONLINE.value();
    //默认app类型
    private int mAppType = AppType.CLOUD_MANAGER.value();

    private OkHttpClient mOkHttpClient;
    private Retrofit defaultRetrofit;

    //缓存Api服务
    //private Map<Class, Object> mServices = new HashMap<>();

    public ApiManager(OkHttpClient mOkHttpClient) {
        this.mOkHttpClient = mOkHttpClient;
    }

    /**
     * 创建Api服务
     *
     * @param serviceClass
     * @return
     */
    public <T> T createApiService(Class<T> serviceClass) {

        return getDefaultRetrofit().create(serviceClass);
      /*  T service = (T) mServices.get(serviceClass);
        if (service == null) {
            service = getDefaultRetrofit().create(serviceClass);
            mServices.put(serviceClass, service);
        }
        return service;*/
    }

    /**
     * 创建Api服务
     *
     * @param host
     * @param serviceClass
     * @param <T>
     * @return
     */
    public <T> T createApiService(String host, Class<T> serviceClass) {

        return buildRetrofit(mOkHttpClient, host).build().create(serviceClass);
    }

    /**
     * 创建Api服务
     *
     * @param okHttpClient
     * @param host
     * @param serviceClass
     * @param <T>
     * @return
     */
    public <T> T createApiService(OkHttpClient okHttpClient, String host, Class<T> serviceClass) {

        return buildRetrofit(okHttpClient, host).build().create(serviceClass);

    }

    public Retrofit newRetrofit() {
        return buildRetrofit(mOkHttpClient, mHost).build();
    }

    /**
     * 获取默认Retrofit
     *
     * @return
     */
    private Retrofit getDefaultRetrofit() {
        if (defaultRetrofit == null) {
            defaultRetrofit = buildRetrofit(mOkHttpClient, mHost).build();
        }
        return defaultRetrofit;
    }

    /**
     * 构建Retrofit
     *
     * @param okHttpClient
     * @param host
     * @return
     */
    private Retrofit.Builder buildRetrofit(OkHttpClient okHttpClient, String host) {

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(host)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为json的支持(以实体类返回)
                .addConverterFactory(FastJsonConverterFactory.create())
                //增加返回值为Oservable<T>的支持
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                ;
    }

    /**
     * 设置默认主机头
     * 外部不建议使用此方法设置主机头，host参数一般都是限定{@link Host}枚举值。
     * 参见{@link ApiManager#setHost(Host)}
     *
     * @param host
     */
    @Deprecated
    public void setHost(@NonNull String host) {
        this.mHost = host;
    }

    /**
     * 设置默认主机头
     *
     * @param host
     */
    public void setHost(@NonNull Host host) {
        setHost(host.value());
    }

    /**
     * 获取默认主机头
     *
     * @return
     */
    public String getHost() {
        return this.mHost;
    }

    /**
     * 获取App类型
     *
     * @return
     */
    public int getAppType() {
        return this.mAppType;
    }

    /**
     * 设置默认App类型
     * 外部不建议使用此方法设置App类型，appType参数一般都是限定{@link AppType}枚举值。
     * 参见{@link ApiManager#setAppType(AppType)}
     *
     * @param appType
     */
    @Deprecated
    public void setAppType(@NonNull int appType) {
        this.mAppType = appType;
    }

    /**
     * 设置默认App类型
     *
     * @param appType
     */
    public void setAppType(@NonNull AppType appType) {
        setAppType(appType.value());
    }
}
