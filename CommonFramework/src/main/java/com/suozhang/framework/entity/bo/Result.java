/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.entity.bo;


import com.suozhang.framework.component.http.ApiError;

/**
 * 请求响应数据实体基类，实际数据内容为泛型数据T
 * <p>
 * 一个标准的java bean,包括一个无参构造方法，私有化字段，提供set,get访问，建议其他DTO对象都按照此规范写
 * <p>
 * Created by Moodd on 2017/2/10.
 */
public class Result<T> implements BaseEntity {
    //返回成功码
    private static final int CODE_SUCCESS = ApiError.SUCCESS.code();

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回消息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    public Result() {
    }

    /**
     * 请求是否成功
     *
     * @return true:成功，false:失败
     */
    public boolean isOk() {
        return CODE_SUCCESS == this.code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
