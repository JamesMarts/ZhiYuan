/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.yiqi.zhiyuan.entity.bo;

import android.support.annotation.IntDef;
import android.text.TextUtils;

import com.suozhang.framework.entity.bo.BaseEntity;
import com.suozhang.framework.entity.bo.LoginUserData;
import com.suozhang.framework.framework.AM;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Moodd
 * @email 420410175@qq.com
 * @date 2017/6/5 9:35
 */

public class PushMessage<T> implements BaseEntity {
    @IntDef({MSG_TYPE_BOOKING_CONDITION})
    @Retention(RetentionPolicy.SOURCE)
    @interface MsgType {
    }

    /**
     * 待办事项
     */
    public static final int MSG_TYPE_BOOKING_CONDITION = 1;


    //{"type":1,"content":{"key1":value1,"key2":"value2"}}

    private String userId;      //当前登录的用户Id,或员工Id,使用别名推送时用于确认身份
    @MsgType
    private int type;           //消息类型，如：待办事项1.退换房 2.呼叫服务员 3.活动消息 4...
    private T content;          //内容数据 --json格式{"key1":value1,"key2":"value2"}

    /**
     * 验证用户，验证推送的用户跟当前登录用户是否一致
     *
     * @return true:一致 false:不一致
     */
    public boolean verifyUser() {
        if (TextUtils.isEmpty(this.userId)) {
            //推送的消息中不包含用户Id,直接返回false,
            return false;
        }
        LoginUserData user = AM.user().getLoginUserData();

        String id = user == null ? null : user.getId();

        if (TextUtils.equals(this.userId, id)) {
            return true;
        }
        return false;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @MsgType
    public int getType() {
        return type;
    }

    public void setType(@MsgType int type) {
        this.type = type;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PushMessage{" +
                "userId='" + userId + '\'' +
                ", type=" + type +
                ", content=" + content +
                '}';
    }
}
