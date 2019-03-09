/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.http;


import com.suozhang.framework.component.http.ex.ApiException;
import com.suozhang.framework.component.http.ex.ServerException;
import com.suozhang.framework.entity.bo.Result;
import com.suozhang.framework.entity.bo.TokenInfo;
import com.suozhang.framework.framework.BaseView;
import com.suozhang.framework.utils.T;
import com.suozhang.framework.utils.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 对Http请求返回的数据预处理，过滤数据，统一异常、错误处理，切换线程等操作；
 * 参见：
 * <p>
 * 1.{@link RxDataProcessFactory#statusFilterTransformer()}主要过滤状态码，根据服务端约定的状态码{@link ApiError}判断是否成功，失败则抛出异常{@link ServerException};
 * 2.{@link RxDataProcessFactory#errorInterceptFunction()}统一异常处理器，处理所有请求过程中出现的异常，UI层只需在onError()拿到msg显示即可；
 * 3.{@link RxDataProcessFactory#ioToMainTransformer()}线程切换模版，避免重新写io--main线程切换代码
 * <p>
 * 一次完整Http请求按照以上3步骤即可完成数据的预处理(注意1-2严格按顺序调用)，调用者只需关心onNext()或者onError()方法拿到结果或展示错误消息
 * <p>
 * 参见用例：{@link RxDataProcessFactory#test()}
 *
 * @author Moodd(420410175[at]qq.com)
 * @date 2017/3/20 10:14
 */

public class RxDataProcessFactory {

    /**
     * 跟compose()配合使用,主要用于网络请求切换线程，网络请求等耗时操作在io线程，UI显示等在主线程，
     * <p>
     * 调用方式：Observable.compose(ioToMainTransformer())
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> ioToMainTransformer() {

        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 跟compose()配合使用，主要用于过滤网络请求返回的数据，适用于标准格式数据，
     * 参见 {@link Result}{@link Result#isOk()},
     * 根据约定的状态码过滤出data数据部分，上层业务直接取data部分数据即可，
     * 如服务端返回错误码，则抛出异常{@link ApiException}，由上层业务统一处理
     * <p>
     * 注意：建议此方法在io线程执行，拿到网络请求返回结果首先调用此方法再调用{@link RxDataProcessFactory#ioToMainTransformer()}即可
     * <p>
     * 调用方式：Observable.compose(statusFilterTransformer())
     *
     * @param <T> 实际数据
     * @return
     */
    public static <T> ObservableTransformer<Result<T>, T> statusFilterTransformer() {

        return new ObservableTransformer<Result<T>, T>() {

            @Override
            public ObservableSource<T> apply(Observable<Result<T>> upstream) {
                return upstream.map(new Function<Result<T>, T>() {
                    @Override
                    public T apply(Result<T> t) throws Exception {

                        return filter(t);
                    }
                });
            }
        };
    }

    /**
     * 过滤状态码，判断服务端约定返回码，如不成功则抛出ApiException交给上层业务统一处理
     *
     * @param result
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> T filter(Result<T> result) throws Exception {
        Logger.i("数据转换,去壳: Result<T>----->T");
        //判断服务端约定返回码，如不成功则抛出ApiException交给上层业务统一处理
        if (!result.isOk()) {
            throw new ServerException(result.getCode(), result.getMsg());
        }
        T data = result.getData();
       /* if (data instanceof String) {
            if (data == null) {
                return (T) "";
            }
        }*/
        return data;
    }

    /**
     * 错误统一拦截处理,所有异常均会转化为{@link ApiException},UI层在Observer.onError()方法中直接获取e.getMessage()展示即可
     * <p>
     * 调用方式：Observable.onErrorResumeNext(errorInterceptFunction())
     * <p>
     * 注意onErrorResumeNext()与onExceptionResumeNext()的区别:
     * 1.onErrorResumeNext()会拦截Throwable顶级异常包括Error及Exception,
     * 2.与onExceptionResumeNext()只会拦截Exception异常，Error会交给观察者onError()自己处理
     * 为了拦截所有异常，达到统一异常处理的目的，我们这里使用onErrorResumeNext()；
     * <p>
     * 建议此方法在io线程中调用，UI线程只关心成功或失败的数据展示
     *
     * @param <T>
     * @return
     */
    private static <T> Function<Throwable, ObservableSource<? extends T>> errorInterceptFunction() {
        return new Function<Throwable, ObservableSource<? extends T>>() {
            @Override
            public ObservableSource<? extends T> apply(Throwable throwable) throws Exception {
                //统一异常处理
                return Observable.error(ExceptionProcessFactory.process(throwable));
            }
        };
    }

    /**
     * 异常拦截模版
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> errorInterceptTransformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {

                return upstream.onErrorResumeNext(RxDataProcessFactory.<T>errorInterceptFunction());
            }
        };
    }

    /**
     * 对Http请求返回的数据预处理，过滤数据，统一错误处理
     * 参见：{@link RxDataProcessFactory#statusFilterTransformer(),RxDataProcessFactory#errorInterceptFunction()}
     * <p>
     * 调用方式：Observable.compose(dataPrepTransformer())
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<Result<T>, T> dataPrepTransformer() {

        return new ObservableTransformer<Result<T>, T>() {

            @Override
            public ObservableSource<T> apply(Observable<Result<T>> upstream) {
                return upstream.compose(RxDataProcessFactory.<T>statusFilterTransformer())
                        .compose(RxDataProcessFactory.<T>errorInterceptTransformer());
            }
        };
    }

    /**
     * 对Http请求返回的数据预处理，过滤数据，统一错误处理，并切换线程
     * 参见：{@link RxDataProcessFactory#statusFilterTransformer(),RxDataProcessFactory#errorInterceptFunction(),RxDataProcessFactory#ioToMainTransformer()}
     * <p>
     * 调用方式：Observable.compose(dataPrepAndIoToMainTransformer())
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<Result<T>, T> dataPrepAndIoToMainTransformer() {

        return new ObservableTransformer<Result<T>, T>() {

            @Override
            public ObservableSource<T> apply(Observable<Result<T>> upstream) {
                return upstream.compose(RxDataProcessFactory.<T>dataPrepTransformer())
                        .compose(RxDataProcessFactory.<T>ioToMainTransformer());
            }
        };
    }

    public abstract static class AutoLoadObserver<T> implements Observer<T> {
        private BaseView view;
        private boolean isAutoCancel;

        private AutoLoadObserver() {
        }

        public AutoLoadObserver(BaseView view) {
            this.view = view;
            this.isAutoCancel = true;
        }

        public AutoLoadObserver(BaseView view, boolean isAutoCancel) {
            this.view = view;
            this.isAutoCancel = isAutoCancel;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            if (isAutoCancel) {
                view.showLoading(d);
            } else {
                view.showLoading();
            }
        }

        @Override
        public abstract void onNext(@NonNull T t);

        @Override
        public void onError(@NonNull Throwable e) {
            view.dismissLoading();
            view.showErrorMsg(e.getMessage());
        }

        @Override
        public void onComplete() {
            view.dismissLoading();
        }
    }

    //测试用例
    private void test() {

        Observable.just(new Result<TokenInfo>())
                .delay(1, TimeUnit.SECONDS)//模拟网络延时
                .compose(RxDataProcessFactory.<TokenInfo>dataPrepAndIoToMainTransformer())//数据预处理
                .subscribe(new Observer<TokenInfo>() {//接受数据
                    @Override
                    public void onSubscribe(Disposable d) {
                        Logger.i("onSubscribe---- " + d);

                    }

                    @Override
                    public void onNext(TokenInfo value) {
                        Logger.i("onNext---- " + value);
                        //正常返回的数据，交给业务逻辑处理

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, e.getMessage());
                        // 这里e为数据预处理后的包装异常ApiException，直接拿到Message展示即可
                        T.showShort(e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        Logger.i("onComplete---- ");
                    }
                });

    }


}
