/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.http;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.suozhang.framework.component.http.ex.ApiException;
import com.suozhang.framework.component.http.ex.ServerException;
import com.suozhang.framework.framework.AM;
import com.suozhang.framework.utils.logger.Logger;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Request;
import okhttp3.Response;
import retrofit2.HttpException;

/**
 * Api请求，统一异常处理工厂，将所有异常转换为ApiException，上层业务只需获取具体错误消息展示即可
 *
 * @author Moodd(420410175[at]qq.com)
 * @date 2017/3/22 11:45
 */

public class ExceptionProcessFactory {


    /**
     * 异常处理，加工，将所有异常转换为ApiException
     *
     * @param e
     * @return
     */
    public static Throwable process(@NonNull Throwable e) {
        Logger.e(e, "统一错误处理捕捉到的原始异常");
        ApiError error = null;
        if (e instanceof HttpException) {

            //HttpException 40x 50x等
            error = getHttpExceptionMsg((HttpException) e);

        } else if (e instanceof SocketTimeoutException) {
            //请求超时
            error = ApiError.CONNECT_TIME_OUT;
        } else if (e instanceof ServerException) {

            //ServerException 约定异常 100x 200x 300x等
            // NOTE: 2017/5/23 直接显示服务端返回的msg

            ServerException exception = (ServerException) e;
            error = ApiError.codeOf(exception.code());
            String msg = exception.getMessage();
            msg = TextUtils.isEmpty(msg) ? error.msg() : msg;
            return new ApiException(exception.code(), msg);
        } else if (e instanceof ConnectException || e instanceof UnknownHostException||e instanceof NoRouteToHostException) {
            //连接异常
            error = ApiError.CONNECT_ERROR;
        } else {
            //其他错误
            error = ApiError.UNKNOWN;
        }

        error = error == null ? ApiError.UNKNOWN : error;
        return new ApiException(error.code(), error.msg());
    }

    /**
     * 获取Http请求异常信息
     *
     * @param e
     * @return
     */
    private static ApiError getHttpExceptionMsg(HttpException e) {
        int code = e.code();
        ApiError error = ApiError.codeOf(code);
        if (error == ApiError.UNAUTHORIZED || error == ApiError.REFRESH_TOKEN_EXPIRE) {
            //登录过期，清除登录信息
            AM.user().loginOut();
            //跳转到登录页面
            showLoginActivity();
        }

        return error;
    }

    private static String getErrorMsg(HttpException e) {
        StringBuilder sb = new StringBuilder("异常信息：\n");

        try {
            Response response = e.response().raw();
            Request request = response.request();
            sb.append(request.method());
            sb.append(request.url());
        } catch (Exception e1) {
            e1.printStackTrace();
            sb.append("响应码：");
            sb.append(e.code());
            sb.append("\n");
            sb.append("错误信息：");
            sb.append(e.message());
        }

        return sb.toString();
    }

    /**
     * 启动登录界面
     */
    public static void showLoginActivity() {
        if (AM.isLoginTips()) {
            try {
                //隐式启动，在登录入口Activity配置IntentFilter指定action为："com.suozhang.framework.ACTION_LOGIN"
                Intent intent = new Intent(AM.getLoginAction());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AM.app().startActivity(intent);
            } catch (Throwable e) {
                Logger.e(e, e.getMessage());
                // T.showShortAloneToast(ApiError.UNAUTHORIZED.msg());//用户信息过期或已在其他设备登录，请重新登录！
            }
        } else {
            // T.showShortAloneToast(ApiError.UNAUTHORIZED.msg());//用户信息过期或已在其他设备登录，请重新登录！
        }

    }

}
