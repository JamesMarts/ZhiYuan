/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.widget;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.suozhang.framework.R;
import com.suozhang.framework.framework.AM;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Moodd
 * @email 420410175@qq.com
 * @date 2017/4/18 14:27
 */

public class EmptyView {

    @IntDef({TYPE_EMPTY,
            TYPE_LOADING,
            TYPE_ERROR
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    /**
     * 无数据状态
     */
    public final static int TYPE_EMPTY = 1;
    /**
     * 加载状态
     */
    public final static int TYPE_LOADING = 2;
    /**
     * 错误状态
     */
    public final static int TYPE_ERROR = 3;

    private ViewGroup mViewGroup;//RecyclerView

    private ViewGroup mLoadingView;
    private ViewGroup mEmptyView;

    private ViewGroup mErrorView;

    private View.OnClickListener mLoadingButtonClickListener;
    private View.OnClickListener mEmptyButtonClickListener;
    private View.OnClickListener mErrorButtonClickListener;

    private CharSequence mErrorMessage = "出错啦~";
    private CharSequence mEmptyMessage = "暂无数据";
    private CharSequence mLoadingMessage = "正在加载···";

    private CharSequence mErrorBtnText = "重试";
    private CharSequence mEmptyBtnText = "刷新";
    private CharSequence mLoadingBtnText = "取消";


    public EmptyView(ViewGroup viewGroup) {
        this.mViewGroup = viewGroup;
    }


    //点击监听
    public void setLoadingButtonClickListener(View.OnClickListener listener) {
        this.mLoadingButtonClickListener = listener;
    }

    public void setEmptyButtonClickListener(View.OnClickListener listener) {
        this.mEmptyButtonClickListener = listener;
    }

    public void setErrorButtonClickListener(View.OnClickListener listener) {
        this.mErrorButtonClickListener = listener;
    }

    public void setLoadingButtonClickListener(CharSequence btnText, View.OnClickListener listener) {
        this.mLoadingBtnText = btnText;
        this.mLoadingButtonClickListener = listener;
    }

    public void setEmptyButtonClickListener(CharSequence btnText, View.OnClickListener listener) {
        this.mEmptyBtnText = btnText;
        this.mEmptyButtonClickListener = listener;
    }

    public void setErrorButtonClickListener(CharSequence btnText, View.OnClickListener listener) {
        this.mErrorBtnText = btnText;
        this.mErrorButtonClickListener = listener;
    }

    public void setErrorMessage(CharSequence mErrorMessage) {
        this.mErrorMessage = mErrorMessage;
    }

    public void setEmptyMessage(CharSequence mEmptyMessage) {
        this.mEmptyMessage = mEmptyMessage;
    }

    public void setLoadingMessage(CharSequence mLoadingMessage) {
        this.mLoadingMessage = mLoadingMessage;
    }

    //获取View
    public View getLoadingView() {
        return get(TYPE_LOADING);
    }

    public View getEmptyView() {
        return get(TYPE_EMPTY);
    }

    public View getErrorView() {
        return get(TYPE_ERROR);
    }

    public View getLoadingView(CharSequence message) {
        return getView(TYPE_LOADING, message);
    }

    public View getEmptyView(CharSequence message) {

        return getView(TYPE_EMPTY, message);
    }

    public View getErrorView(CharSequence message) {
        return getView(TYPE_ERROR, message);
    }

    public View getView(@Type int type, CharSequence message) {
        switch (type) {
            case TYPE_LOADING:
                this.mLoadingMessage = message;
                break;
            case TYPE_EMPTY:
                this.mEmptyMessage = message;
                break;
            case TYPE_ERROR:
                this.mErrorMessage = message;
                break;
        }
        return get(type);
    }

    private View get(@Type int type) {
        View view = null;
        switch (type) {
            case TYPE_LOADING:
                if (mLoadingView == null) {
                    mLoadingView = (ViewGroup) LayoutInflater.from(mViewGroup.getContext()).inflate(R.layout.view_loading, mViewGroup, false);
                    ImageView ico = (ImageView) mLoadingView.findViewById(R.id.imageViewLoading);
                    AM.image().bind(R.drawable.ic_loading_gif, ico);
                }
                setMsg(mLoadingView, mLoadingMessage);
                setBtnOnClick(mLoadingView, mLoadingButtonClickListener, mLoadingBtnText);
                view = mLoadingView;
                break;

            case TYPE_EMPTY:
                if (mEmptyView == null) {
                    mEmptyView = (ViewGroup) LayoutInflater.from(mViewGroup.getContext()).inflate(R.layout.view_empty, mViewGroup, false);
                }
                setMsg(mEmptyView, mEmptyMessage);
                setBtnOnClick(mEmptyView, mEmptyButtonClickListener, mEmptyBtnText);
                view = mEmptyView;
                break;

            case TYPE_ERROR:
                if (mErrorView == null) {
                    mErrorView = (ViewGroup) LayoutInflater.from(mViewGroup.getContext()).inflate(R.layout.view_error, mViewGroup, false);
                }
                setMsg(mErrorView, mErrorMessage);
                setBtnOnClick(mErrorView, mErrorButtonClickListener, mErrorBtnText);
                view = mErrorView;
                break;
        }
        return view;
    }

    private void setBtnOnClick(View view, View.OnClickListener listener, CharSequence btnText) {
        Button button = (Button) view.findViewById(R.id.btn_click);
        if (button == null) {
            return;
        }
        if (listener == null) {
            button.setVisibility(View.GONE);
        } else {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(listener);
            if (TextUtils.isEmpty(btnText)) {
                button.setText("");
            } else {
                button.setText(btnText);
            }
        }
    }

    private void setMsg(View view, CharSequence msg) {
        TextView tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        if (tvMsg == null) {
            return;
        }
        if (TextUtils.isEmpty(msg)) {
            //默认
            tvMsg.setText("");
        } else {
            tvMsg.setText(msg);
        }
    }

    /**
     * 设置空View
     */
    public void showEmptyView() {
        showView(TYPE_EMPTY);
    }

    /**
     * 设置空View
     */
    public void showLoadingView() {
        showView(TYPE_LOADING);
    }

    /**
     * 设置空View
     */
    public void showErrorView() {
        showView(TYPE_ERROR);
    }

    /**
     * 设置空View
     */
    public void showEmptyView(CharSequence message) {
        this.mEmptyMessage = message;
        showView(TYPE_EMPTY);
    }

    /**
     * 设置空View
     */
    public void showLoadingView(CharSequence message) {
        this.mLoadingMessage = message;
        showView(TYPE_LOADING);
    }

    /**
     * 设置空View
     */
    public void showErrorView(CharSequence message) {
        this.mErrorMessage = message;
        showView(TYPE_ERROR);
    }

    /**
     * 显示View
     *
     * @param type
     * @param message
     */
    public void showView(@Type int type, CharSequence message) {
        switch (type) {
            case TYPE_LOADING:
                this.mLoadingMessage = message;
                break;
            case TYPE_EMPTY:
                this.mEmptyMessage = message;
                break;
            case TYPE_ERROR:
                this.mErrorMessage = message;
                break;
        }
        showView(type);
    }

    private FrameLayout mEmptyLayout;


    /**
     * 设置空View
     *
     * @param type
     */
    public void showView(@Type int type) {
        View emptyView = get(type);

        boolean insert = false;
        if (mEmptyLayout == null) {
            mEmptyLayout = new FrameLayout(mViewGroup.getContext());
            final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
            final ViewGroup.LayoutParams lp = emptyView.getLayoutParams();
            if (lp != null) {
                layoutParams.width = lp.width;
                layoutParams.height = lp.height;
            }
            mEmptyLayout.setLayoutParams(layoutParams);
            insert = true;
        }
        mEmptyLayout.removeAllViews();
        mEmptyLayout.addView(emptyView);

        if (insert) {
            //ListView GridView等
            if (mViewGroup instanceof AdapterView) {
                ViewGroup parent = (ViewGroup) mViewGroup.getParent();
                parent.addView(mEmptyLayout);
                ((AdapterView) mViewGroup).setEmptyView(mEmptyLayout);
            }
            //RecyclerView等
            //.....
        }

    }

}
