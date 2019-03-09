/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.suozhang.framework.R;

import java.io.File;


/**
 * @author Moodd
 * @e-mail 420410175@qq.com
 * @date 2017/3/27 15:46
 */

public class ImageManagerImpl implements ImageManager {

    private static final int LOADING = R.drawable.ic_loading;
    private static final int ERROR = R.drawable.ic_error;

    //默认配置
    private RequestOptions options = new RequestOptions()
            .placeholder(LOADING)
            .error(ERROR)
            .diskCacheStrategy(DiskCacheStrategy.ALL);


    private static ImageManagerImpl instance;

    private ImageManagerImpl() {
    }


    public static ImageManagerImpl getInstance() {
        if (instance == null) {
            synchronized (ImageManagerImpl.class) {
                if (instance == null) {
                    instance = new ImageManagerImpl();
                }
            }
        }
        return instance;
    }

    /**
     * 构建Builder
     *
     * @param context
     * @param type
     * @param <T>
     * @return
     */
    private <T> RequestBuilder<Drawable> builder(Context context, T type, RequestOptions options) {
        return Glide.with(context)
                .load(type)
                .apply(options)
                ;
    }

    /**
     * 构建圆角Builder
     *
     * @param context
     * @param type
     * @param round
     * @param <T>
     * @return
     */
    private <T> RequestBuilder<Drawable> builder(Context context, T type, int round) {
        if (round > 0) {
            RequestOptions op = options.clone().transform(new GlideRoundTransform(context, round));
            return builder(context, type, op);
        } else {
            return builder(context, type, options);
        }
    }

    /**
     * 构建圆形Builder
     *
     * @param context
     * @param type
     * @param <T>
     * @return
     */
    private <T> RequestBuilder<Drawable> builderCircle(Context context, T type) {
        RequestOptions op = options.clone().transform(new GlideCircleTransform(context));
        return builder(context, type, op);
    }

    @Override
    public <T> void bindObject(T modelType, ImageView view) {
        builder(view.getContext(), modelType,options).into(view);
    }

    @Override
    public <T> void bindObject(T modelType, int round, ImageView view) {


        builder(view.getContext(), modelType, round).into(view);
    }

    @Override
    public <T> void bindToCircleObject(T modelType, ImageView view) {
        builderCircle(view.getContext(), modelType).into(view);
    }

    @Override
    public void bind(String url, ImageView view) {
        bindObject(url, view);
    }

    @Override
    public void bind(Integer resourceId, ImageView view) {
        bindObject(resourceId, view);
    }

    @Override
    public void bind(Uri uri, ImageView view) {
        bindObject(uri, view);
    }

    @Override
    public void bind(File file, ImageView view) {
        bindObject(file, view);
    }

    @Override
    public void bind(byte[] bytes, ImageView view) {
        bindObject(bytes, view);
    }

    @Override
    public void bind(String url, int round, ImageView view) {
        bindObject(url, round, view);
    }


    @Override
    public void bind(File file, int round, ImageView view) {
        bindObject(file, round, view);
    }

    @Override
    public void bind(Integer resourceId, int round, ImageView view) {
        bindObject(resourceId, round, view);
    }
}
