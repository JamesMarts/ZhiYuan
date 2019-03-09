/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.entity.bo;

/**
 * @author Moodd
 * @e-mail 420410175@qq.com
 * @date 2017/3/28 17:08
 */

public class LoginOrgData implements BaseEntity {
    /**
     * org_Address : 深圳市南山区科苑路31号
     * name : 深圳香格里拉大酒店
     * level : 0
     * orgType : 2
     * id : c93e3021-a748-48a7-b7e8-6581aacbafb5
     */
    private String id;//Id
    private String name;//机构名称
    private int level;//机构级别
    private int orgType; //机构类型
    private String orgAddress;//机构地址

    // NOTE: 2017/9/27 新增字段
    private boolean isAllowAddOrg;//是否允许添加机构（下级经销商）
    private boolean isAdmin;//是否管理员，（管理员添加项目备案时，可指定下级经销商）


    public LoginOrgData() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getOrgType() {
        return orgType;
    }

    public void setOrgType(int orgType) {
        this.orgType = orgType;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    public boolean isAllowAddOrg() {
        return isAllowAddOrg;
    }

    public void setAllowAddOrg(boolean allowAddOrg) {
        isAllowAddOrg = allowAddOrg;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "LoginOrgData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", orgType=" + orgType +
                ", orgAddress='" + orgAddress + '\'' +
                ", isAllowAddOrg=" + isAllowAddOrg +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
