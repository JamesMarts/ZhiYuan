/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.yiqi.zhiyuan.common.lib.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.suozhang.framework.utils.logger.Logger;

import cn.jpush.android.api.JPushInterface;
import com.yiqi.zhiyuan.entity.bo.PushMessage;

/**
 * @author Moodd
 * @e-mail 420410175@qq.com
 * @date 2017/3/27 10:00
 */

public class JPushReceiver extends BroadcastReceiver {
    public static final String TAG = "JPUSH";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Logger.d("收到极光推送广播-------->>>>> Action = " + action);

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(action)) {
            String regId = intent.getStringExtra(JPushInterface.EXTRA_REGISTRATION_ID);
            Logger.d("[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
            String jsonMsg = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
            processCustomMessage(context, jsonMsg);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action)) {
            int notifactionId = intent.getIntExtra(JPushInterface.EXTRA_NOTIFICATION_ID, -1);
            Logger.d("接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
            Logger.d("[MyReceiver] 用户点击打开了通知");
            openNotification(context, intent);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(action)) {
            Logger.d("[JpushReceiver] 用户收到到RICH PUSH CALLBACK: " + intent.getStringExtra(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(action)) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Logger.d("[JpushReceiver]" + " 连接状态变化 connected = " + connected);

        } else {
            Logger.d("[JpushReceiver] Unhandled intent - " + action);
        }
    }

    /**
     * 通知打开
     *
     * @param context
     * @param bundle
     */
    private void openNotification(Context context, Intent bundle) {

    }

    /**
     * 收到自定义消息
     *
     * @param context
     * @param jsonMsg
     */
    private void processCustomMessage(Context context, String jsonMsg) {
       /* ///////////////////////////test//////////////////////////////////
        //弹出任务提示窗口
        Intent i = new Intent(context, HasTaskTipsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        if (true) return;
        ///////////////////////////test//////////////////////////////////
        */

        //{"type":1,"content":{"key1":value1,"key2":"value2"}}

        Logger.e("处理推送的自定义消息： " + jsonMsg);

        if (TextUtils.isEmpty(jsonMsg)) {
            return;
        }

        PushMessage pushMessage = null;

        try {
            pushMessage = JSON.parseObject(jsonMsg, PushMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (pushMessage == null) {
            return;
        }

        //验证用户
        if (!pushMessage.verifyUser()) {
            return;
        }

        if (pushMessage.getType() == PushMessage.MSG_TYPE_BOOKING_CONDITION) {
            //待办事项任务处理
            bookingConditionTask(context, pushMessage);
        }
    }

    /**
     * 待办事项任务处理
     *
     * @param context
     * @param msg
     */
    private void bookingConditionTask(Context context, PushMessage msg) {
        JpushHelper.newTaskTips(msg);
    }


}
