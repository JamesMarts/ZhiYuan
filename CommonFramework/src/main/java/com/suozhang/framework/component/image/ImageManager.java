/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.image;

import android.net.Uri;
import android.widget.ImageView;

import java.io.File;

/**
 * 图片加载管理
 *
 * @author Moodd
 * @e-mail 420410175@qq.com
 * @date 2017/3/27 15:24
 */

public interface ImageManager {


    /**
     * 加载图片 泛型参考Glide支持的load类型
     * <p>
     * 在不明确图片类型的时候，不建议使用此方法
     *
     * @param modelType 类型
     * @param view
     */
    @Deprecated
    <T> void bindObject(T modelType, ImageView view);

    /**
     * 加载圆角图片 泛型参考Glide支持的load类型
     * <p>
     * 在不明确图片类型的时候，不建议使用此方法
     *
     * @param modelType 类型
     * @param round     圆角大小 dp
     * @param view
     */
    @Deprecated
    <T> void bindObject(T modelType, int round, ImageView view);

    /**
     * 加载圆形图片 泛型参考Glide支持的load类型
     * <p>
     * 在不明确图片类型的时候，不建议使用此方法
     *
     * @param modelType 类型
     * @param view
     */
    @Deprecated
    <T> void bindToCircleObject(T modelType, ImageView view);

    /**
     * 加载网络图片
     *
     * @param url  图片地址
     * @param view
     */
    void bind(String url, ImageView view);

    /**
     * 加载资源图片
     *
     * @param resourceId
     * @param view
     */
    void bind(Integer resourceId, ImageView view);

    /**
     * 加载Uri包装的图片
     *
     * @param uri
     * @param view
     */
    void bind(Uri uri, ImageView view);

    /**
     * 加载文件类型图片
     *
     * @param file
     * @param view
     */
    void bind(File file, ImageView view);

    /**
     * 加载字节类型图片
     *
     * @param bytes
     * @param view
     */
    void bind(byte[] bytes, ImageView view);


    /**
     * 加载圆角图片
     *
     * @param url   图片地址
     * @param round 图片地址
     * @param view
     */
    void bind(String url, int round, ImageView view);

    /**
     * 加载圆角图片
     *
     * @param file  图片文件
     * @param round 图片地址
     * @param view
     */
    void bind(File file, int round, ImageView view);

    /**
     * 加载圆角图片
     *
     * @param resourceId 图片资源
     * @param round      图片地址
     * @param view
     */
    void bind(Integer resourceId, int round, ImageView view);


}
