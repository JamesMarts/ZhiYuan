/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.framework;

import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.suozhang.framework.component.permission.PermissionsManager;
import com.suozhang.framework.utils.T;
import com.suozhang.framework.utils.logger.Logger;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;


/**
 * Activity基类
 * 1.已集成友盟统计
 * 2.6.0中权限申请
 * 3.ButterKnife View注入
 * 4.Dagger2依赖注入
 *
 * @Author Moodd
 * @Date 2017/3/9
 */

public abstract class BaseActivity extends RxAppCompatActivity implements BaseView {

    //加载进度条,关联网络请求，返回及关闭监听
    private DialogHelper mDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化
        init();
    }

    /**
     * 初始化
     */
    protected void init() {
        //允许弹出未登录提示
        setLoginTipsOnly(false);
        //加入栈管理
        if (isAddStack()) {
            AM.appManager().pushActivity(this);
        }
        //初始化系统设置，如不允许横屏，在setContentView()之前调用，可以做一些系统参数的初始化设置
        initSystemParams();
        //初始化加载布局,并绑定ButterKnife注入
        initContentView();
        //初始化注入依赖
        initInjector();
        //初始化View
        initView();
        //初始化事件
        initEvent();
        //初始化数据
        initData();
    }

    /**
     * 设置登录提示只显示一次
     *
     * @param isOnly true:只提示一次 false:重复提示
     */
    protected void setLoginTipsOnly(boolean isOnly) {
        AM.setLoginTips(!isOnly);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(this.getClass().getName());//页面统计
//        MobclickAgent.onResume(this);     //友盟 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(this.getClass().getName());
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isAddStack()) {
            AM.appManager().popActivity(this);
        }
        try {
            //销毁前，必须关闭Dialog,否则会造成窗体溢出（android.view.WindowLeaked），导致应用崩溃
            if (mDialogHelper != null) {
                mDialogHelper.recycle();
            }
            mDialogHelper = null;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //权限请求结果，交给PermissionsManager统一处理，
        // Activity或Fragment中直接调用PermissionsManager#requestPermissionsIfNecessaryForResult()
        Logger.i("权限请求结果，交给PermissionsManager统一处理");
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 子类可覆盖，自己实现
     *
     * @param msg
     */
    @Override
    public void showErrorMsg(CharSequence msg) {
        T.showShort(msg);
    }

    /**
     * 子类可覆盖，自己实现
     *
     * @param msgId
     */
    @Override
    public void showErrorMsg(@StringRes int msgId) {
        showErrorMsg(getString(msgId));
    }

    /**
     * 子类可覆盖，自己实现
     *
     * @param msg
     */
    @Override
    public void showMsg(CharSequence msg) {
        T.showShort(msg);
    }

    /**
     * 子类可覆盖，自己实现
     *
     * @param msgId
     */
    @Override
    public void showMsg(@StringRes int msgId) {
        showErrorMsg(getString(msgId));
    }

    @Override
    public void showLoading() {
        showLoading(null);
    }

    @Override
    public void showLoading(Object cancel) {
        if (mDialogHelper == null) {
            mDialogHelper = new DialogHelper(this);
        }
        //显示Dialog,并关联请求,当Dialog关闭或取消时，取消所有请求
        mDialogHelper.showDialog(cancel);
    }

    @Override
    public void dismissLoading() {
        if (mDialogHelper != null) {
            mDialogHelper.dismissDialog();
        }
    }

    /**
     * 默认自动按照生命周期处理，如在onCreate中订阅，则在onDestroy中取消
     * <p>
     * 子类可根据需要重写策略
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

    /**
     * 绑定布局 当返回null时，使用Id注入attachLayoutRes()
     *
     * @return 布局View
     */
    protected View attachLayoutView() {
        return null;
    }

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    @LayoutRes
    protected abstract int attachLayoutRes();

    /**
     * 初始化注入
     */
    protected abstract void initInjector();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 此Activity是否添加到栈管理，默认添加;
     * 子类可重写该方法，返回false,表示不添加到栈管理
     *
     * @return
     */
    protected boolean isAddStack() {
        return true;
    }

    /**
     * 设置系统参数或其他配置，默认已设置：1、不允许横屏，2、隐藏标题栏，3.不弹出输入法键盘
     * 子类可根据需要重写该方法或增加其他初始化配置，如全屏
     */
    protected void initSystemParams() {
        //默认不允许横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //不弹出输入法键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * 初始化加载布局,并绑定ButterKnife注入
     */
    protected void initContentView() {

        View view = attachLayoutView();
        if (view != null) {
            setContentView(view);
        } else {
            int rootViewId = attachLayoutRes();
            setContentView(rootViewId);
        }

        ButterKnife.bind(this);
    }

    /**
     * 初始化View
     */
    protected void initView() {

    }

    /**
     * 初始化事件
     */
    protected void initEvent() {

    }

    /**
     * 添加 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * 添加 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 设置tag，不然下面 findFragmentByTag(tag)找不到
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    /**
     * 替换 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * 替换 Fragment0
     *
     * @param containerViewId
     * @param fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // 设置tag
            fragmentTransaction.replace(containerViewId, fragment, tag);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // 这里要设置tag，上面也要设置tag
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        } else {
            // 存在则弹出在它上面的所有fragment，并显示对应fragment
            getSupportFragmentManager().popBackStack(tag, 0);
        }
    }

    /**
     * 初始化 Toolbar,按返回关闭界面，标题居中
     *
     * @param toolbar
     * @param resTitle
     */
    protected void initToolBar(Toolbar toolbar, @StringRes int resTitle) {
        initToolBar(toolbar, getString(resTitle));
    }

    /**
     * 初始化 Toolbar,按返回关闭界面，标题居中
     *
     * @param toolbar
     * @param title
     */
    protected void initToolBar(Toolbar toolbar, CharSequence title) {
        initToolBar(toolbar, title, true, true);
    }

    /**
     * 初始化 Toolbar
     *
     * @param toolbar
     * @param title
     * @param homeAsUpEnabled
     */
    protected void initToolBar(Toolbar toolbar, CharSequence title, boolean homeAsUpEnabled) {
        initToolBar(toolbar, title, homeAsUpEnabled, false);
    }

    /**
     * 初始化 Toolbar
     *
     * @param toolbar
     * @param resTitle
     * @param homeAsUpEnabled
     */
    protected void initToolBar(Toolbar toolbar, @StringRes int resTitle, boolean homeAsUpEnabled) {
        initToolBar(toolbar, getString(resTitle), homeAsUpEnabled, false);
    }

    /**
     * 初始化 Toolbar
     *
     * @param toolbar
     * @param resTitle
     * @param homeAsUpEnabled
     * @param isTitleCenter
     */
    protected void initToolBar(Toolbar toolbar, @StringRes int resTitle, boolean homeAsUpEnabled, boolean isTitleCenter) {
        initToolBar(toolbar, getString(resTitle), homeAsUpEnabled, isTitleCenter);
    }

    /**
     * 初始化 Toolbar
     *
     * @param toolbar
     * @param title
     * @param homeAsUpEnabled
     * @param isTitleCenter
     */
    protected void initToolBar(Toolbar toolbar, CharSequence title, boolean homeAsUpEnabled, boolean isTitleCenter) {
        initToolBar(toolbar, title, homeAsUpEnabled, isTitleCenter, false);
    }

    /**
     * 初始化 Toolbar
     *
     * @param toolbar
     * @param resTitle
     * @param homeAsUpEnabled
     * @param isTitleCenter
     */
    protected void initToolBar(Toolbar toolbar, @StringRes int resTitle, boolean homeAsUpEnabled, boolean isTitleCenter, boolean isSetSupportActionBar) {
        initToolBar(toolbar, getString(resTitle), homeAsUpEnabled, isTitleCenter, isSetSupportActionBar);
    }

    /**
     * 初始化 Toolbar
     *
     * @param toolbar
     * @param title
     * @param homeAsUpEnabled
     * @param isTitleCenter
     */
    protected void initToolBar(Toolbar toolbar, CharSequence title, boolean homeAsUpEnabled, boolean isTitleCenter, boolean isSetSupportActionBar) {
        toolbar.setTitle(title);
        if (isTitleCenter) {
            setTitleCenter(toolbar);
        }
        if (isSetSupportActionBar) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
        } else {
            if (homeAsUpEnabled) {
                setBackFinsh(toolbar);
            }
        }
    }

    /**
     * 设置Toolbar按返回关闭界面
     *
     * @param toolbar
     */
    protected void setBackFinsh(Toolbar toolbar) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    /**
     * 设置Toolbar标题居中
     *
     * @param toolbar
     */
    protected void setTitleCenter(Toolbar toolbar) {
        int childCount = toolbar.getChildCount();
        int left = toolbar.getContentInsetStartWithNavigation();
        //int left1 = toolbar.getContentInsetLeft();
        int deviceWidth = toolbar.getContext().getResources().getDisplayMetrics().widthPixels;
        String toolbarText = toolbar.getTitle().toString();

        for (int i = 0; i < childCount; i++) {
            View child = toolbar.getChildAt(i);
            if (child instanceof TextView) {
                TextView childTitle = (TextView) child;
                String childText = childTitle.getText().toString();
                if (TextUtils.equals(childText, toolbarText)) {
                    Paint p = childTitle.getPaint();
                    float textWidth = p.measureText(childText);
                    float tx = (deviceWidth - textWidth) / 2.0f - left;
                    childTitle.setTranslationX(tx);
                    break;
                }
            }
        }
    }


}
