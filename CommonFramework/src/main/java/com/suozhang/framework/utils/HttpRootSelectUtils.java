/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.suozhang.framework.component.http.Host;
import com.suozhang.framework.framework.AM;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Moodd on 2016/12/24.
 */

public class HttpRootSelectUtils {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);


    /**
     * 选择服务器地址
     */
    public static void selectHttpRoot(Context context, final ResetHostListener resetHostListener,
                                      DialogInterface.OnDismissListener dismissListener) {

        sNextGeneratedId.set(1);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (dismissListener != null) {
            builder.setOnDismissListener(dismissListener);
        }
        builder.setCancelable(false);
        builder.setTitle("请选择服务器地址");

        final RadioGroup group = new RadioGroup(context);
        group.setOrientation(LinearLayout.VERTICAL);

        RadioButton button1 = new RadioButton(context);
        button1.setText("线上测试：" + Host.DEBUG_ONLINE.value());
        button1.setChecked(true);
        button1.setId(generateViewId());

        RadioButton button2 = new RadioButton(context);
        button2.setText("本地测试：" + Host.DEBUG_LOCAL.value());
        button2.setId(generateViewId());

        RadioButton button4 = new RadioButton(context);
        button4.setText("正式：" + Host.ONLINE.value());
        button4.setId(generateViewId());

        final TextView text = new TextView(context);
        text.setPadding(30, 10, 0, 10);
        text.setTextColor(0xff0000ff);

        String selectHttpRoot = AM.api().getHost();
        text.setText("  已设置的地址：" + selectHttpRoot);

        if (TextUtils.equals(Host.DEBUG_ONLINE.value(), selectHttpRoot)) {
            button1.setChecked(true);

        } else if (TextUtils.equals(Host.DEBUG_LOCAL.value(), selectHttpRoot)) {
            button2.setChecked(true);
        }  else if (TextUtils.equals(Host.ONLINE.value(), selectHttpRoot)) {
            button4.setChecked(true);
        }


        group.addView(button1);
        group.addView(button2);
        group.addView(button4);

        group.addView(text);

        builder.setView(group);

        builder.setPositiveButton("使用已设置的地址", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Field field = null;
                try {
                    //通过反射获取dialog中的私有属性mShowing
                    field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                    field.setAccessible(true);//设置该属性可以访问
                    //设置dialog不可关闭
                    field.set(dialog, false);
                    dialog.dismiss();
                } catch (Exception ex) {
                }

                //获取保存的地址
                String httpRoot = AM.api().getHost();

                //获取选择的地址
                Host selectHttpHost = getSelectHttpRoot(group);

                if (TextUtils.isEmpty(httpRoot)) {
                    T.showShort("没有设置过地址，请使用切换地址！");
                    return;
                }

                if (selectHttpHost == null || !TextUtils.equals(httpRoot, selectHttpHost.value())) {
                    T.showShort("已设置的地址与选择地址不一致，请切换地址！");
                    return;
                }

                try {
                    //关闭
                    field.set(dialog, true);
                    dialog.dismiss();
                } catch (Exception ex) {
                }
            }
        });
        builder.setNegativeButton("切换地址", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Field field = null;
                try {
                    //通过反射获取dialog中的私有属性mShowing
                    field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                    field.setAccessible(true);//设置该属性可以访问
                    //设置dialog可关闭
                    field.set(dialog, true);
                    dialog.dismiss();
                } catch (Exception ex) {
                }

                try {
                    //关闭
                    field.set(dialog, true);
                    dialog.dismiss();
                } catch (Exception ex) {
                }
                Host selectHost = getSelectHttpRoot(group);
                if (selectHost == null) {
                    T.showShort("地址选择有误！");
                    return;
                }
                if (resetHostListener==null){
                    T.showShort("切换无效");
                    return;
                }
                //重新设置api地址
                resetHostListener.onResetHost(selectHost);
               // ((CloudManagerApplication) AM.app()).resetHost(selectHost);
            }
        });

        builder.create().show();
    }

    private static Host getSelectHttpRoot(RadioGroup group) {
        Host selectHost = null;
        int id = group.getCheckedRadioButtonId();
        switch (id) {
            case 1:
                selectHost = Host.DEBUG_ONLINE;
                break;

            case 2:
                selectHost = Host.DEBUG_LOCAL;
                break;
            case 3:
                selectHost = Host.ONLINE;
                break;
        }
        return selectHost;
    }

    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1;
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public interface ResetHostListener {
        void onResetHost(Host host);
    }
}
