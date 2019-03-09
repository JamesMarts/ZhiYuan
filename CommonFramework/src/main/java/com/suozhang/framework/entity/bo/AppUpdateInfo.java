/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.entity.bo;

import com.suozhang.framework.entity.enums.AppType;

/**
 * 项目名称：
 * 类描述： APP更新信息
 * 创建人：Moodd
 * 创建时间：2016/6/12 10:04
 * 修改人：Moodd
 * 修改时间：2016/6/12 10:04
 * 修改备注：
 */

public class AppUpdateInfo implements BaseEntity {
    private int versionCode;    //版本号
    private String versionName; //版本名称
    private String url;         //下载地址
    private String msg;         //更新内容
    private float size;         //下载文件大小
    private String channelId;   //渠道ID
    /**
     * App类型见枚举定义
     *
     * @see AppType
     */
    private int appType;     //APP类型，锁掌云管理、酒店
    private String osType;      //系统类型 android / ios

    private String hotelId;//酒店Id 用于用户端App升级

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    @Override
    public String toString() {
        return "AppUpdateInfo{" +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", url='" + url + '\'' +
                ", msg='" + msg + '\'' +
                ", size=" + size +
                ", channelId='" + channelId + '\'' +
                ", appType='" + appType + '\'' +
                ", osType='" + osType + '\'' +
                ", hotelId='" + hotelId + '\'' +
                "} ";
    }
}
