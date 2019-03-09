/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.framework;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.suozhang.framework.R;
import com.suozhang.framework.utils.logger.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.disposables.Disposable;

public class DialogHelper implements DialogInterface.OnKeyListener, DialogInterface.OnDismissListener {

    private Context context;
    private Dialog dialog;
    private Set<Disposable> disposables = new HashSet<>();

    //计数器，添加请求时+1，关闭时-1
    private AtomicInteger disposableCount = new AtomicInteger(0);

    public DialogHelper(Context context) {
        this.context = context;
    }

    /**
     * 初始化正在获取数据Dialog
     */
    private Dialog createDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.DialogTranslucent);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCancelable(false);
        //设置返回键监听---取消网络请求
        dialog.setOnKeyListener(this);
        dialog.setOnDismissListener(this);
        return dialog;
    }

    /**
     * 释放资源
     */
    public synchronized void recycle() {
        disposableCount.set(0);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (disposables != null) {
            disposables.clear();
        }
        dialog = null;
        context = null;
        disposables = null;
    }

    /**
     * 显示进度条，并将请求加入集合
     *
     * @param cancel
     */
    public synchronized void showDialog(Object cancel) {
        if (dialog == null) {
            dialog = createDialog(context);
        }
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
        if (cancel instanceof Disposable) {
            addDisposable(((Disposable) cancel));
        }
    }


    /**
     * 关闭Dialog
     */
    public synchronized void dismissDialog() {
        //计数器-1
        int count = disposableCount.decrementAndGet();
        Logger.d("关闭 " + count);
        //还有未完成的请求，不关闭
        if (count > 0) {
            return;
        }
        //关闭
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 添加请求
     *
     * @param d
     */
    private void addDisposable(Disposable d) {
        if (disposables != null && d != null && !d.isDisposed()) {
            disposables.add(d);
            //增加一个任务，计数+1
            int count = disposableCount.incrementAndGet();
            Logger.d("添加一个取消请求- - -" + disposables.size() + " cancelableCount = " + count);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        int count = disposableCount.get();
        Logger.d("关闭了 count = " + count);
        if (count > 0) {
            return;
        }
        //遍历取消
        cancelAll();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //遍历取消
            cancelAll();
            //关闭dialog
            if (dialog != null) {
                dialog.dismiss();
            }
            return true;
        }
        return false;
    }


    /**
     * 取消所有请求
     */
    private void cancelAll() {
        if (disposables == null || disposables.isEmpty()) {
            return;
        }
        for (Disposable d : disposables) {
            if (d != null && !d.isDisposed()) {
                d.dispose();
            }
        }
        //清空集合
        disposables.clear();
        //清空计数器
        disposableCount.set(0);
        Logger.d("取消所有请求");
    }

}