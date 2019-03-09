/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.update;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.suozhang.framework.R;
import com.suozhang.framework.component.permission.PermissionsManager;
import com.suozhang.framework.component.permission.PermissionsResultAction;
import com.suozhang.framework.entity.bo.AppUpdateInfo;
import com.suozhang.framework.framework.BaseActivity;
import com.suozhang.framework.utils.FileUtil;
import com.suozhang.framework.utils.NetUtil;
import com.suozhang.framework.utils.PermissionHint;
import com.suozhang.framework.utils.T;


/**
 * Created by Moodd on 2016/12/29.
 */
public class AppUpdateTipsActivity extends BaseActivity {


    private TextView mTvVersion;
    private TextView mTvMsg;
    private TextView mTvNetwork;

    private ImageButton mBtnCancel;

    private Button mBtnOK;


    private AppUpdateInfo mAppUpdateInfo;

    private Dialog mNotWifiDialog;

    @Override
    protected boolean isAddStack() {
        //不添加到栈管理
        return false;
    }

    @Override
    protected void initSystemParams() {
        //不设置屏幕方向，解决8.0崩溃问题java.lang.IllegalStateException: Only fullscreen opaque activities can request orientation

        // super.initSystemParams();

        //不弹出输入法键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void initView() {
        super.initView();

        setContentView(R.layout.activity_app_update_tips);
        mTvVersion = (TextView) findViewById(R.id.tv_version);
        mTvMsg = (TextView) findViewById(R.id.tv_msg);
        mTvNetwork = (TextView) findViewById(R.id.tv_network);

        mBtnOK = (Button) findViewById(R.id.btn_ok);
        mBtnCancel = (ImageButton) findViewById(R.id.btn_cancel);
    }

    @Override
    protected void initData() {
        //初始化无WIFI网络提示Dialog
        // initNotWifiDialog();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okClick(v);
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClick(v);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //初始化更新数据
        initAppUpdateData(getIntent());
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_app_update_tips;
    }

    @Override
    protected void initInjector() {

    }

    /**
     * 初始化更新数据
     *
     * @param intent
     */
    private void initAppUpdateData(Intent intent) {
        if (intent == null) return;

        try {
            mAppUpdateInfo = (AppUpdateInfo) intent.getSerializableExtra(AppUpdateManager.CHECK_UPDATE_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //设置默认内容
        if (mTvMsg != null) mTvMsg.setText("更新内容");
        String verName = null;
        float size = 0;
        String url = null;
        String msg = null;
        CharSequence htmlContent = null;

        if (mAppUpdateInfo != null) {
            verName = mAppUpdateInfo.getVersionName();
            size = mAppUpdateInfo.getSize();
            url = mAppUpdateInfo.getUrl();
            msg = mAppUpdateInfo.getMsg();
        }
        try {
            //格式化Html样式的Msg内容
            String downloadLink = "\n如升级失败，请点击使用外部浏览器下载： <a href='" + url + "'>下载地址</a>\n";
            htmlContent = Html.fromHtml(downloadLink + msg);
        } catch (Exception e) {
        }

        String network = NetUtil.isWifi(this) ? "WIFI网络,请放心下载(" + size + "M)." : "当前非WIFI网络，请注意您的流量(" + size + "M).";

        //设置网络提示
        if (mTvNetwork != null) mTvNetwork.setText(network);
        //设置内容
        if (mTvMsg != null && !TextUtils.isEmpty(htmlContent)) {
            mTvMsg.setMovementMethod(LinkMovementMethod.getInstance());
            mTvMsg.setText(htmlContent);
        }
        //设置版本号
        if (mTvVersion != null) {
            mTvVersion.setText(TextUtils.isEmpty(verName) ? "未知版本" : verName);
        }
    }

    /**
     * 浏览器下载
     *
     * @param url
     * @throws Exception
     */
    private void browserDownload(String url) throws Exception {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }

    @Override
    public void onBackPressed() {
        //  注销调用父类方法 使返回键不起作用，失效
        // super.onBackPressed();
    }

    /**
     * 取消更新
     *
     * @param v
     */
    private void cancelClick(View v) {
        finish();
    }

    /**
     * 确定更新
     *
     * @param v
     */
    private void okClick(View v) {
        //6.0以上动态权限申请--蓝牙扫描需要的位置权限
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new PermissionsResultAction() {
                    @Override
                    public void onGranted() {
                        if (!FileUtil.existsSdcard()) {
                            T.showShort("SD卡不可用");
                            return;
                        }
                        //更新
                        update();
                    }

                    @Override
                    public void onDenied(String permission) {
                        PermissionHint.show(AppUpdateTipsActivity.this, false);
                        //T.showShort("请允许设备授权后再试！");
                    }
                });
    }

    private void checkWifi() {
        if (NetUtil.isWifi(this)) {
            //有WIFI直接更新
            update();
        } else {
            //显示无WIFI网络提示Dialog
            if (mNotWifiDialog != null) mNotWifiDialog.show();
        }
    }

    /**
     * 更新
     */
    private void update() {
        String url = mAppUpdateInfo == null ? null : mAppUpdateInfo.getUrl();
        if (TextUtils.isEmpty(url)) {
            T.showShort("文件下载地址有误，更新失败");
            finish();
            return;
        }

        //在服务中下载更新,广播接受传参
        /*Intent intent = new Intent(AppUpdateService.ACTION_DOWNLOAD);
        intent.putExtra(AppUpdateService.ACTION_DOWNLOAD, url);
        //发送应用中有效的广播
        LocalBroadcastManager.getInstance(AppUpdateTipsActivity.this).sendBroadcast(intent);
        */
        AppUpdateManager.getInstance().sendDownloadBroadcast(this, url);

        T.showShort("正在下载···");
        //关闭窗口
        finish();
    }

    /**
     * 初始化无WIFI网络提示Dialog
     */
    private void initNotWifiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("温馨提示");
        builder.setMessage("当前为非WIFI网络，确定要继续更新吗？");
        builder.setPositiveButton("继续更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
                //更新
                update();
            }
        });
        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
                finish();
            }
        });

        mNotWifiDialog = builder.create();
    }

}
