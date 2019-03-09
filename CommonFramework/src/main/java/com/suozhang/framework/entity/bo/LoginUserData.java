/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.entity.bo;

/**
 * @author Moodd
 * @e-mail 420410175@qq.com
 * @date 2017/3/28 17:08
 */

public class LoginUserData implements BaseEntity {
    /**
     * loginName : admin
     * org_Id : c93e3021-a748-48a7-b7e8-6581aacbafb5
     * email : 6666@qq.com
     * phone : 123
     * nickName : 6666
     * id : 2AC68197-F5F2-4892-81BF-B266A0C038E1
     */
    private String id;//id
    private String org_Id;//机构Id
    private String loginName;//登录名称
    private String email;//邮箱
    private String phone;//手机号
    private String nickName;//昵称

    public LoginUserData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrg_Id() {
        return org_Id;
    }

    public void setOrg_Id(String org_Id) {
        this.org_Id = org_Id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "LoginUserData{" +
                "id='" + id + '\'' +
                ", org_Id='" + org_Id + '\'' +
                ", loginName='" + loginName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
