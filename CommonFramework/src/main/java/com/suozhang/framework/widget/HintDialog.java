/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.suozhang.framework.R;


/**
 * 自定义提示Dialog
 * 参数相关设置参见{@link Builder}
 * Created by Moodd on 2017/2/27.
 */

public class HintDialog extends Dialog implements View.OnClickListener {
    private boolean isUseNewUi;//是否使用新UI

    private TextView mTvTitle;//标题
    private TextView mTvMsg;//消息内容
    private View mDivTop;//顶部分割条
    private View mDivMiddle;//底部按钮中间的分割条
    private Button mBtnSubmit;//确定
    private Button mBtnCancel;//取消
    private View mLayoutBtnGroup;//

    private CharSequence mTitle;//标题
    private CharSequence mMessage;//消息
    private CharSequence mSubmit;//确定
    private CharSequence mCancel;//取消

    private CharSequence mEditHint;//编辑提示
    private int mEditInputType = InputType.TYPE_CLASS_TEXT;//编辑输入类型
    private int mEditMaxLength = Integer.MAX_VALUE;//编辑输入最大值


    private OnClickListener mOnSubmitClickListener;//确定监听
    private OnClickListener mOnCancelClickListener;//取消监听

    private EditText mEdtContent;
    private boolean isEditModel;
    private OnEditTextInputResultListener mOnEditTextInputResultListener;//输入结果监听


    public interface OnEditTextInputResultListener {
        void onInputResult(DialogInterface dialog, int which, String result);
    }

    public HintDialog(Context context) {
        this(context, R.style.DialogSemitransparent);
    }

    public HintDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();

        //根据标题内容判断是否显示标题
        setShowTitle();
        //设置按钮显示状态
        setShowButton();
        //消息模式
        initMsgModel();
    }


    /**
     * 初始化布局view
     */
    private void initView() {
        //设置布局
        int layoutResId = isUseNewUi ? R.layout.dialog_hint_custom_new : R.layout.dialog_hint_custom;

        setContentView(layoutResId);

        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvMsg = (TextView) findViewById(R.id.tv_msg);
        mTvMsg.setMovementMethod(ScrollingMovementMethod.getInstance());

        mDivTop = findViewById(R.id.v_div_top);
        mDivMiddle = findViewById(R.id.v_div_middle);

        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);

        mLayoutBtnGroup = findViewById(R.id.ll_btn_group);

        mEdtContent = (EditText) findViewById(R.id.edt_content);
    }

    /**
     * 初始化小时模式
     */
    private void initMsgModel() {
        if (isEditModel) {
            mTvMsg.setVisibility(View.GONE);
            mEdtContent.setVisibility(View.VISIBLE);
        } else {
            mTvMsg.setVisibility(View.VISIBLE);
            mEdtContent.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mTvTitle.setText(TextUtils.isEmpty(mTitle) ? "" : mTitle);
        mTvMsg.setText(TextUtils.isEmpty(mMessage) ? "" : mMessage);
        mBtnSubmit.setText(TextUtils.isEmpty(mSubmit) ? "" : mSubmit);
        mBtnCancel.setText(TextUtils.isEmpty(mCancel) ? "" : mCancel);

        setEditTextParams(mMessage, mEditHint, mEditMaxLength, mEditInputType);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mBtnSubmit.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    /**
     * 根据标题内容判断是否显示标题
     */
    private void setShowTitle() {
        if (TextUtils.isEmpty(mTitle)) {
            mTvTitle.setVisibility(View.GONE);
            mDivTop.setVisibility(View.GONE);
        } else {
            mTvTitle.setVisibility(View.VISIBLE);
            mDivTop.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置按钮的显示状态
     */
    private void setShowButton() {
        //确定按钮
        if (TextUtils.isEmpty(mSubmit)) {
            mBtnSubmit.setVisibility(View.GONE);
        } else {
            mBtnSubmit.setVisibility(View.VISIBLE);
        }
        //取消按钮
        if (TextUtils.isEmpty(mCancel)) {
            mBtnCancel.setVisibility(View.GONE);
        } else {
            mBtnCancel.setVisibility(View.VISIBLE);

        }

        if (!isUseNewUi) {
            //根据“确定”按钮显示与否，设置“取消”按钮的背景样式
            if (mBtnSubmit.getVisibility() == View.VISIBLE) {
                mBtnCancel.setBackgroundResource(R.drawable.selector_btn_hintdialog_cancel);
            } else {
                mBtnCancel.setBackgroundResource(R.drawable.selector_btn_hintdialog_single);
            }
            //根据“取消”按钮显示与否，设置“确定”按钮的背景样式
            if (mBtnCancel.getVisibility() == View.VISIBLE) {
                mBtnSubmit.setBackgroundResource(R.drawable.selector_btn_hintdialog_submit);
            } else {
                mBtnSubmit.setBackgroundResource(R.drawable.selector_btn_hintdialog_single);
            }
        }

        //中间分割线显示状态
        if (mBtnSubmit.getVisibility() == View.VISIBLE && mBtnCancel.getVisibility() == View.VISIBLE) {
            mDivMiddle.setVisibility(View.VISIBLE);
        } else {
            mDivMiddle.setVisibility(View.GONE);
        }
        //底部Button容器显示状态
        if (mBtnSubmit.getVisibility() == View.GONE && mBtnCancel.getVisibility() == View.GONE) {
            mLayoutBtnGroup.setVisibility(View.GONE);
        } else {
            mLayoutBtnGroup.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show() {
        super.show();
    }

    /**
     * 当设置点击事件的时候，由监听自行处理dismiss，否则自动dismiss
     * 确定与单个按钮的确定共享一个点击监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_submit) {//点击确定
            if (isEditModel) {
                if (mOnEditTextInputResultListener != null) {
                    String result = mEdtContent.getText().toString().trim();
                    mOnEditTextInputResultListener.onInputResult(this, BUTTON_POSITIVE, result);
                } else {
                    this.dismiss();
                }
            } else {
                if (mOnSubmitClickListener != null) {
                    mOnSubmitClickListener.onClick(this, BUTTON_POSITIVE);
                } else {
                    this.dismiss();
                }
            }

        } else if (id == R.id.btn_cancel) {
            if (mOnCancelClickListener != null) {
                mOnCancelClickListener.onClick(this, BUTTON_NEGATIVE);
            } else {
                this.dismiss();
            }
        }
    }

    /**
     * 设置标题
     *
     * @param title 标题
     * @return
     */
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (mTvTitle != null) {
            mTvTitle.setText(TextUtils.isEmpty(mTitle) ? "" : mTitle);
            setShowTitle();
        }
    }

    /**
     * 设置消息
     *
     * @param msg 内容
     * @return
     */
    public void setMessage(CharSequence msg) {
        mMessage = msg;
        if (mTvMsg != null) {
            mTvMsg.setText(TextUtils.isEmpty(mMessage) ? "" : mMessage);
        }
    }

    /**
     * 设置编辑框参数
     *
     * @param msg
     * @param hint
     * @param maxLength
     */
    public void setEditTextParams(CharSequence msg, CharSequence hint, int maxLength, int inputType) {
        mMessage = msg;
        mEditHint = hint;
        mEditMaxLength = maxLength < 0 ? 0 : maxLength;
        this.mEditInputType = inputType;
        if (mEdtContent != null) {
            mEdtContent.setText(TextUtils.isEmpty(mMessage) ? "" : mMessage);
            mEdtContent.setHint(TextUtils.isEmpty(mEditHint) ? "" : mEditHint);
            mEdtContent.setInputType(mEditInputType);
            mEdtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mEditMaxLength)});
        }
    }

    public static class Builder {
        private HintDialog dialog;
        private Context context;

        /**
         * 根据默认主题创建Dialog
         *
         * @param context
         */
        public Builder(Context context) {
            this(context, false);
        }

        /**
         * 根据默认主题,指定消息模式创建Dialog
         *
         * @param context
         * @param isEditModel 是否编辑模式 true 是
         */
        public Builder(Context context, boolean isEditModel) {
            this.context = context;
            dialog = new HintDialog(context);
            setMsgModel(isEditModel);
        }

        /**
         * 根据指定主题创建Dialog
         *
         * @param context
         * @param themeResId
         */
        public Builder(Context context, int themeResId) {
            dialog = new HintDialog(context, themeResId);
        }

        /**
         * 返回构建HintDialog的实例
         *
         * @return
         */
        public HintDialog builder() {
            return dialog;
        }

        /**
         * 设置消息模式
         *
         * @param isEditModel
         */
        public Builder setMsgModel(boolean isEditModel) {
            dialog.isEditModel = isEditModel;
            return this;
        }

        /**
         * 设置是否使用新UI
         *
         * @param isUseNewUi
         */
        public Builder setUseNewUi(boolean isUseNewUi) {
            dialog.isUseNewUi = isUseNewUi;
            return this;
        }

        /**
         * 设置确定输入结果点击监听
         *
         * @param name                          按钮文字
         * @param onEditTextInputResultListener 监听器
         * @return
         */
        public Builder setOnEditTextInputResultListener(CharSequence name, OnEditTextInputResultListener onEditTextInputResultListener) {
            dialog.mOnEditTextInputResultListener = onEditTextInputResultListener;
            dialog.mSubmit = name;
            return this;
        }

        /**
         * 设置确定输入结果点击监听
         *
         * @param resId                         按钮文字
         * @param onEditTextInputResultListener 监听器
         * @return
         */
        public Builder setOnEditTextInputResultListener(@StringRes int resId, OnEditTextInputResultListener onEditTextInputResultListener) {
            return setOnEditTextInputResultListener(context.getString(resId), onEditTextInputResultListener);
        }

        /**
         * 设置编辑框参数
         *
         * @param msg
         * @param hint
         * @param maxLength
         */
        public Builder setEditTextParams(CharSequence msg, CharSequence hint, int maxLength, int inputType) {
            dialog.setEditTextParams(msg, hint, maxLength, inputType);
            return this;
        }

        /**
         * 设置标题
         *
         * @param title 标题
         * @return
         */
        public Builder setTitle(CharSequence title) {
            dialog.mTitle = title;
            return this;
        }

        /**
         * 设置消息
         *
         * @param msg 内容
         * @return
         */
        public Builder setMessage(CharSequence msg) {
            dialog.mMessage = msg;
            return this;
        }

        /**
         * 设置确定点击监听
         *
         * @param name            按钮文字
         * @param onClickListener 监听器
         * @return
         */
        public Builder setOnSubmitClickListener(CharSequence name, OnClickListener onClickListener) {
            dialog.mOnSubmitClickListener = onClickListener;
            dialog.mSubmit = name;
            return this;
        }

        /**
         * 设置取消点击监听
         *
         * @param name            按钮文字
         * @param onClickListener 监听器
         * @return
         */
        public Builder setOnCancelClickListener(CharSequence name, OnClickListener onClickListener) {
            dialog.mOnCancelClickListener = onClickListener;
            dialog.mCancel = name;
            return this;
        }

        /**
         * 设置确定点击监听
         *
         * @param resId           按钮文字
         * @param onClickListener 监听器
         * @return
         */
        public Builder setOnSubmitClickListener(@StringRes int resId, OnClickListener onClickListener) {
            dialog.mOnSubmitClickListener = onClickListener;
            dialog.mSubmit = context.getString(resId);
            return this;
        }

        /**
         * 设置取消点击监听
         *
         * @param resId           按钮文字
         * @param onClickListener 监听器
         * @return
         */
        public Builder setOnCancelClickListener(@StringRes int resId, OnClickListener onClickListener) {
            dialog.mOnCancelClickListener = onClickListener;
            dialog.mCancel = context.getString(resId);
            return this;
        }

        /**
         * 设置Window Type  如：当需要全局弹出Dialog时，设置WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
         *
         * @param type
         * @return
         */
        public Builder setWindowType(int type) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setType(type);
            }
            return this;
        }

        public Builder setCancelable(boolean flag) {
            dialog.setCancelable(flag);
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean cancel) {
            dialog.setCanceledOnTouchOutside(cancel);
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            dialog.setOnCancelListener(onCancelListener);
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            dialog.setOnDismissListener(onDismissListener);
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            dialog.setOnKeyListener(onKeyListener);
            return this;
        }

    }
}
