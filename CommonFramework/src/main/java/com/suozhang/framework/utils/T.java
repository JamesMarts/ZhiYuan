/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.suozhang.framework.framework.AM;
import com.suozhang.framework.widget.CustomToast;


/**
 * Toast统一管理类
 */
public class T {

    private T() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static CustomToast mToast;

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        showShort(AM.app(), message);
    }


    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(int message) {
        showShort(AM.app(), message);
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        showLong(AM.app(), message);

    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(int message) {
        showLong(AM.app(), message);

    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(CharSequence message, final int duration) {
        show(AM.app(), message, duration);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(int message, final int duration) {
        show(AM.app(), message, duration);
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(final Context context, final CharSequence message) {
        autoPost(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                createToast(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(final Context context, final int message) {

        autoPost(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                createToast(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(final Context context, final CharSequence message) {
        autoPost(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                createToast(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(final Context context, final int message) {
        autoPost(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                createToast(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(final Context context, final CharSequence message, final int duration) {
        autoPost(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(context, message, duration).show();
                createToast(context, message, duration).show();

            }
        });
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(final Context context, final int message, final int duration) {
        autoPost(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(context, message, duration).show();
                createToast(context, message, duration).show();
            }
        });
    }

    /**
     * 创建一个全局的自定义的Toast,此方法必须在主线程中调用
     *
     * @param context
     * @param message  消息内容，可以是字符串，字符序列或引用@StringRes
     * @param duration
     * @return
     */
    private static Toast createToast(final Context context, final Object message, final int duration) {
        if (mToast == null) {
            mToast = CustomToast.createToast(context, getMsg(context, message), duration);
        }
        mToast.setDuration(duration);
        mToast.setMsg(getMsg(context, message));
        return mToast;
    }

    private static CharSequence getMsg(Context context, Object message) {
        CharSequence msg = "";
        try {
            if (message instanceof CharSequence) {
                msg = (CharSequence) message;
            } else if (message instanceof Integer) {
                msg = context.getText((Integer) message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 显示一个短时间独立的Toast
     *
     * @param message
     */
    public static void showShortAloneToast(@StringRes int message) {
        showAloneToast(AM.app(), message, Toast.LENGTH_SHORT);
    }

    /**
     * 显示一个短时间独立的Toast
     *
     * @param message
     */
    public static void showShortAloneToast(CharSequence message) {
        showAloneToast(AM.app(), message, Toast.LENGTH_SHORT);
    }

    /**
     * 显示一个长时间独立的Toast
     *
     * @param message
     */
    public static void showLongAloneToast(CharSequence message) {
        showAloneToast(AM.app(), message, Toast.LENGTH_LONG);
    }

    /**
     * 显示一个长时间独立的Toast
     *
     * @param message
     */
    public static void showLongAloneToast(@StringRes int message) {
        showAloneToast(AM.app(), message, Toast.LENGTH_LONG);
    }

    /**
     * 显示一个独立的Toast
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void showAloneToast(final Context context, @StringRes final int message, final int duration) {
        autoPost(new Runnable() {
            @Override
            public void run() {
                CustomToast.createToast(context, context.getString(message), duration).show();

            }
        });
    }

    /**
     * 显示一个独立的Toast
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void showAloneToast(final Context context, final CharSequence message, final int duration) {
        autoPost(new Runnable() {
            @Override
            public void run() {
                CustomToast.createToast(context, message, duration).show();
            }
        });
    }

    /***
     * 长时间显示Toast
     *
     * @param c    上下文
     * @param msg  显示字段
     * @param flag 显示表示（true显示，false不显示）
     */
    public static void showMsg(Context c, String msg, boolean flag) {
        if (flag)
            Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    private static void autoPost(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            sHandler.post(runnable);
        }
    }

    private static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}