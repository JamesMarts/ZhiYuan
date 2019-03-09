package com.yiqi.zhiyuan.common.lib.jpush;

import android.content.Context;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * 极光推送-别名设置结果回调
 *
 * @author moodd
 * @email 420410175@qq.com
 * @date 2018/1/19 15:54
 */
public class JPushTagAliasCallbackReceiver extends JPushMessageReceiver {
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage message) {
        if (message == null) {
            return;
        }
        JpushHelper.sendJPushAliasResult(message);
    }

}
