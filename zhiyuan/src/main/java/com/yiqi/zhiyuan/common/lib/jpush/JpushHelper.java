/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.yiqi.zhiyuan.common.lib.jpush;

import android.app.Service;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.text.TextUtils;


import com.suozhang.framework.entity.bo.LoginUserData;
import com.suozhang.framework.framework.AM;
import com.suozhang.framework.utils.logger.Logger;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import com.yiqi.zhiyuan.R;
import com.yiqi.zhiyuan.entity.bo.PushMessage;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * @author Moodd
 * @email 420410175@qq.com
 * @date 2017/6/5 11:40
 */

public class JpushHelper {

    private static Subject<PushMessage> subject = PublishSubject.<PushMessage>create().toSerialized();
    private static Subject<JPushMessage> sJPushAliasSubject = PublishSubject.<JPushMessage>create().toSerialized();

    /**
     * 极光推送别名设置 --别名设置为用户ID
     */
    public static void registerJpushAlias() {
        setJpushAlias(AM.app(), true);
    }

    /**
     * 极光推送别名设置 --不指定别名
     */
    public static void unregisterJpushAlias() {
        setJpushAlias(AM.app(), false);
    }

    /**
     * 发送别名设置结果,此方法主要由回调广播使用
     * {@link JPushTagAliasCallbackReceiver}
     *
     * @param message
     */
    static void sendJPushAliasResult(JPushMessage message) {
        sJPushAliasSubject.onNext(message);
    }

    /**
     * 监听别名设置结果
     *
     * @return
     */
    static Observable<JPushMessage> bindJPushAliasResult() {
        return sJPushAliasSubject.hide();
    }

    /**
     * 极光推送别名设置 --别名设置为用户ID
     */
    private static void setJpushAlias(final Context context, final boolean isRegister) {
        String alias = null;
        if (isRegister) {
            //获取Id
            LoginUserData user = AM.user().getLoginUserData();
            alias = user == null ? null : user.getId();
        }

        if (TextUtils.isEmpty(alias) || TextUtils.equals("null", alias)) {
            alias = "";
        }

        if (alias.contains("-")) {
            alias = alias.replace("-", "");
        }

        setAlias(context, alias);
    }

    private static void setAlias(final Context context, final String alias) {

        Observable.create(new ObservableOnSubscribe<JPushMessage>() {
            @Override
            public void subscribe(ObservableEmitter<JPushMessage> e) throws Exception {
                //设置别名
                Logger.d("开始设置别名为 = " + alias);
                JPushInterface.setAlias(context, 1, alias);
            }
        })
                //监听回调
                .mergeWith(bindJPushAliasResult())
                .filter(new Predicate<JPushMessage>() {
                    @Override
                    public boolean test(JPushMessage jPushMessage) throws Exception {
                        int errorCode = jPushMessage.getErrorCode();
                        if (errorCode != 0) {
                            throw new RuntimeException("极光推送-->设置别名失败ErrorCode = " + errorCode);
                        }
                        return true;
                    }
                })
                //超时时间
                .timeout(30, TimeUnit.SECONDS)
                //重试次数
                .retry(4, new Predicate<Throwable>() {
                    @Override
                    public boolean test(@NonNull Throwable throwable) throws Exception {
                        Logger.e(throwable, "极光推送-->设置别名出错重试");
                        return true;
                    }
                })
                .subscribe(new Observer<JPushMessage>() {
                    private Disposable disposable;

                    private void disposable() {
                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(JPushMessage jPushMessage) {
                        disposable();
                        String msg = jPushMessage.toString();
                        Logger.d("设置别名成功: " + msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable();
                        Logger.e(e, "设置别名失败");
                    }

                    @Override
                    public void onComplete() {
                        disposable();
                    }
                });
    }

    /**
     * 新任务提示
     */
    public static void newTaskTips(PushMessage msg) {
        playerNewTaskTips();
        updateNewTask(msg);
    }

    /**
     * 新任务变化了，在“待办事项”界面，订阅事件
     *
     * @return
     */
    public static Observable<PushMessage> newTaskChanged() {
        return subject.hide();
    }

    private static void updateNewTask(PushMessage msg) {
        subject.onNext(msg);
    }

    private static Vibrator vibrator;
    private static MediaPlayer player;

    /**
     * 新任务提示
     */
    private static void playerNewTaskTips() {
        try {
            //震动
            if (vibrator == null) {
                vibrator = (Vibrator) AM.app().getSystemService(Service.VIBRATOR_SERVICE);
            }
            vibrator.cancel();
            vibrator.vibrate(new long[]{80, 300, 80, 300, 80, 300}, -1);
            //音频播放
            if (player == null) {
//                player = MediaPlayer.create(AM.app(), R.raw.new_task_tips);
            }
            if (!player.isPlaying()) {
                player.start();
            }
        } catch (Throwable e) {
        }
    }
}
