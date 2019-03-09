/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.entity.bo;

import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 用户帐号信息实体
 *
 * @author Moodd
 * @e-mail 420410175@qq.com
 * @date 2017/3/29 10:12
 */

public class UserAccount implements BaseEntity, Cloneable {

    //用户名密码登录
    private String userName; //用户名
    private String password; //密码
    //上级帐号，员工登录时填入
    private String parentUserName;

    //手机号登录
    private String phone; //手机号
    private String code; //验证码

    //第三方登录
    private String openId; //第三方登录的唯一标识，暂定

    public UserAccount() {
    }

    public UserAccount(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public UserAccount(String openId) {
        this.openId = openId;
    }

    public UserAccount(String userName, String password, String parentUserName) {
        this.userName = userName;
        this.password = password;
        this.parentUserName = parentUserName;
    }

    /**
     * 转换到正常的用户账户，如为测试用户（用户名前面包含"cs"或"CS"），去掉前缀后登陆
     *
     * @return
     */
    @JSONField(serialize = false)
    public UserAccount toNormal() {
        if (!isTestUser()) {
            return this;
        }

        UserAccount test = null;
        try {
            test = (UserAccount) this.clone();
        } catch (Exception e) {
        }
        if (test == null) {
            return this;
        }
        //测试模式,去掉前缀
        String userName = test.getUserName();
        test.setUserName(userName.substring(2));
        return test;
    }

    /**
     * 是否测试用户
     *
     * @return
     */
    @JSONField(serialize = false)
    public boolean isTestUser() {
        return isTestUser(this.userName);
    }

    /**
     * 是否测试用户
     *
     * @return
     */
    @JSONField(serialize = false)
    public boolean isTestUser(String userName) {
        if (TextUtils.isEmpty(userName)) {
            return false;
        }
        String prefix = "cs";
        int prefixLength = prefix.length();
        if (userName.length() < prefixLength) {
            return false;
        }
        String starts = userName.substring(0, prefixLength).toLowerCase();
        return TextUtils.equals(prefix, starts);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getParentUserName() {
        return parentUserName;
    }

    public void setParentUserName(String parentUserName) {
        this.parentUserName = parentUserName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }


    @Override
    public String toString() {
        return "UserAccount{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                "parentUserName='" + parentUserName + '\'' +
                ", phone='" + phone + '\'' +
                ", code='" + code + '\'' +
                ", openId='" + openId + '\'' +
                '}';
    }
}
