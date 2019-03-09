/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.app;

import android.app.Activity;

import com.suozhang.framework.framework.AM;
import com.suozhang.framework.utils.logger.Logger;

import java.util.Stack;


/**
 * Activity栈管理
 * 不要单独new对象，由Dagger2提供依赖管理，{@link AppManagerModule}提供单例的实例化，并在Application中注入
 * 最终在{@link AM#appManager()}提供全局唯一访问
 * <p>
 * Created by Moodd on 2017/3/14.
 */

public class AppManagerImpl implements AppManager {

    private static final Stack<Activity> mActivityStack = new Stack<>();

    @Override
    public void pushActivity(Activity activity) {
        Logger.d("栈大小 ： " + mActivityStack.size());
        if (activity == null) return;
        try {
            mActivityStack.push(activity);
            Logger.d("入栈Activity= " + activity.getLocalClassName() + " 栈顶Activity= " + mActivityStack.peek().getLocalClassName() +
                    "  栈大小 ： " + mActivityStack.size());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    public void popActivity(Activity activity) {
        try {
            Logger.d("出栈Activity= " + activity.getLocalClassName() +
                    " 栈顶Activity= " + mActivityStack.peek().getLocalClassName() + " 栈大小： " + mActivityStack.size());
            Activity peekActivity = mActivityStack.peek();
            if (activity == peekActivity) {
                mActivityStack.pop().finish();
                Logger.d("出栈Activity= " + activity.getLocalClassName() + " 已关闭   剩余：" + mActivityStack.size()
                        + " 栈顶Activity= " + mActivityStack.peek().getLocalClassName());
            } else {
                activity.finish();
                mActivityStack.remove(activity);
                Logger.d("出栈Activity= " + activity.getLocalClassName() + " 已关闭   剩余：" + mActivityStack.size()
                        + " 栈顶Activity= " + mActivityStack.peek().getLocalClassName());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    public void popActivity() {
        try {
            Activity activity = mActivityStack.pop();
            activity.finish();
            Logger.d("出栈Activity= " + activity.getLocalClassName() + "已关闭   剩余：" + mActivityStack.size());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    public Activity peekActivity() {
        Activity activity = null;
        try {
            activity = mActivityStack.peek();
            Logger.d("查看栈顶Activity= " + activity.getLocalClassName() + "栈大小：" + mActivityStack.size());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return activity;
    }


    @Override
    public void exitApp() {
        try {
            int size = mActivityStack.size();
            for (int i = 0; i < size; i++) {
                popActivity();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            //应用退出时，先保存统计数据。
            //  MobclickAgent.onKillProcess(AM.app());
            //退出应用，杀死进程
            System.exit(0);
            // android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


}
