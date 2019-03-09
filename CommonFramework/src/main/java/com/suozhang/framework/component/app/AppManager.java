/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.app;

import android.app.Activity;

/**
 * Created by Moodd on 2016/12/1.
 */

public  interface AppManager   {


    /**
     * 入栈，把Activity压入栈顶，在Activity创建的时候加入（onCreate()）
     *
     * @param activity
     */
    public  void pushActivity(Activity activity);

    /**
     * 出栈，移除栈顶Activity 即移除最后加入栈的Activity,在Activity退出的时候（onDestroy()）移除
     */
    public  void popActivity(Activity activity);

    /**
     * 出栈，移除栈顶Activity 即移除最后加入栈的Activity,在Activity退出的时候（onDestroy()）移除
     */
    public  void popActivity();


    /**
     * 查看栈顶Activity,并返回栈顶Activity,当栈为空时，返回null
     *
     * @return
     */
    public Activity peekActivity();

    /**
     * 关闭所有Activity,退出应用
     */
    public  void exitApp();

}
