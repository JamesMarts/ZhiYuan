/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.utils;

/**
 * @author LIJUWEN
 * @email yiyayiyayaoljw@gmail.com
 * @date 2017/4/12 16:29
 */
public class StringUtil {

    // 使用String的split 方法
    public static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(","); // 拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }
}
