/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.framework;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suozhang.framework.utils.T;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Fragment基类，宿主Activity必须为{@link BaseActivity},才能配合使用加载进度条Dialog的显示及取消请求
 * 1.已集成友盟统计
 * 2.6.0中权限申请
 * 3.ButterKnife View注入
 * 4.Dagger2依赖注入
 *
 * @author Moodd
 * @date 2017/3/
 */
public abstract class BaseFragment extends RxFragment implements BaseView {


    private Unbinder mUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(attachLayoutRes(), container, false);
        mUnbinder = ButterKnife.bind(this, rootView);//绑定framgent
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        initInjector();
        initView();
        initData();
        initEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        // MobclickAgent.onPageStart(this.getClass().getName()); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        //  MobclickAgent.onPageEnd(this.getClass().getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null && isUnbind()) {
            mUnbinder.unbind();
        }
    }

    /**
     * 是否解绑，返回true将调用ButterKnife Unbinder.unbind()
     *
     * @return
     */
    protected boolean isUnbind() {
        return true;
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
        Activity activity = getActivity();
        if (activity != null && activity instanceof BaseActivity) {
            ((BaseActivity) activity).showLoading(cancel);
        }
    }

    @Override
    public void dismissLoading() {
        Activity activity = getActivity();
        if (activity != null && activity instanceof BaseActivity) {
            ((BaseActivity) activity).dismissLoading();
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
     * 获取布局Id
     *
     * @return
     */
    public abstract int attachLayoutRes();

    /**
     * 初始化注入
     */
    protected abstract void initInjector();

    /**
     * 初始化View
     */
    protected void initView() {

    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化事件
     */
    protected abstract void initEvent();

}
