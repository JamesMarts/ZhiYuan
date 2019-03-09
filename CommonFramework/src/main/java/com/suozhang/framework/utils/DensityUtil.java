/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.utils;


import com.suozhang.framework.framework.AM;

/**
 * px dip sp 转换工具类
 * Created by Moodd on 2017/2/20.
 */
public final class DensityUtil {

    private static float density = -1F;
    private static float scaledDensity = -1F;
    private static int widthPixels = -1;
    private static int heightPixels = -1;

    private DensityUtil() {
    }

    /**
     * 获取屏幕密度
     *
     * @return
     */
    public static float getDensity() {
        if (density <= 0F) {
            density = AM.app().getResources().getDisplayMetrics().density;
        }
        return density;
    }

    /**
     * 获取缩放密度，字体缩放比例因子
     *
     * @return
     */
    public static float getScaledDensity() {
        if (scaledDensity <= 0F) {
            scaledDensity = AM.app().getResources().getDisplayMetrics().scaledDensity;
        }
        return scaledDensity;
    }

    /**
     * dip转px
     *
     * @param dpValue
     * @return
     */
    public static int dp2px(float dpValue) {
        return (int) (dpValue * getDensity() + 0.5F);
    }

    /**
     * px转dp
     *
     * @param pxValue
     * @return
     */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / getDensity() + 0.5F);
    }

    /**
     * sp转px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        return (int) (spValue * getScaledDensity() + 0.5f);

    }


    /**
     * px转sp
     *
     * @param pxValue
     * @return
     */
    public static float px2sp(float pxValue) {
        return (int) (pxValue / getScaledDensity() + 0.5f);

    }

    /**
     * 获取屏幕宽 单位：像素
     *
     * @return
     */
    public static int getScreenWidth() {
        if (widthPixels <= 0) {
            widthPixels = AM.app().getResources().getDisplayMetrics().widthPixels;
        }
        return widthPixels;
    }

    /**
     * 获取屏幕高 单位：像素
     *
     * @return
     */
    public static int getScreenHeight() {
        if (heightPixels <= 0) {
            heightPixels = AM.app().getResources().getDisplayMetrics().heightPixels;
        }
        return heightPixels;
    }
}
