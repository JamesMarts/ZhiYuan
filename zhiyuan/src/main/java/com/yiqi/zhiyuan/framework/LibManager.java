/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.yiqi.zhiyuan.framework;

import android.content.Context;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.suozhang.framework.framework.AM;
import com.suozhang.framework.utils.DynamicTimeFormat;

import cn.jpush.android.api.JPushInterface;
import com.yiqi.zhiyuan.R;

/**
 * @author Moodd
 * @e-mail 420410175@qq.com
 * @date 2017/3/27 14:15
 */

public class LibManager {

    public void init(Context app) {
        //初始化第三方库
        initJPush(app);
        initUM(app);
        //其他库...
        initSmartRefreshLayout();

    }

    /**
     * 友盟分享
     */
    private void initUM(Context app) {
//        UMShareAPI.get(app);
//        PlatformConfig.setWeixin("wxef85048d8c35625c", "a6bb1732017a0641f1fe980109a0a205");
//        PlatformConfig.setQQZone("1106596401", "5CUE38PEgkSvKFew");
    }

    /**
     * 极光推送
     */
    private void initJPush(Context app) {
        // 设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(AM.isDebug());
        // 初始化 JPush
        JPushInterface.init(app);
        JPushInterface.stopCrashHandler(app);

    }
    private  void initSmartRefreshLayout() {
        //static 代码段可以防止内存泄露

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //全局设置主题颜色
                layout.setPrimaryColorsId(R.color.primary_highlight, android.R.color.white);
                layout.setReboundDuration(100);
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context)
                        .setTimeFormat(new DynamicTimeFormat("更新于 %s"));
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20)
                        .setAccentColorId(R.color.primary_highlight);
                //  return new BallPulseFooter(context);
            }
        });
    }
}
