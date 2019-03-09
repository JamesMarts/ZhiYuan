/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * 无权限时提醒
 *
 * @author Moodd
 * @email 420410175@qq.com
 * @date 2017/4/7 15:37
 */

public class PermissionHint {

    /**
     * 弹出权限请求提示框
     *
     * @param context
     * @param isFinish 是否关闭当前Activity
     */
    public static void show(final Context context, final boolean isFinish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。\n请点击“设置”-“权限”-打开所需权限（部分机型权限管理入口可能不一致）");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startSettingActivity(context);
                if (isFinish) {
                    try {
                        ((Activity) context).finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isFinish) {
                    try {
                        ((Activity) context).finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        builder.setCancelable(false);

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private static void startSettingActivity(Context context) {
        try {
            Uri packageURI = Uri.parse("package:" + context.getPackageName());
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
            context.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
