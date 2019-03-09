/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.component.ble;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * @author Moodd
 * @email 420410175@qq.com
 * @date 2017/4/11 9:28
 */

public class BleCommand {
    /**
     * 未安装
     * ----FFFFFFFFN对应的十六进制ACSII码
     */
    public static final String STATE_FFFFFFFFN = "46464646464646464E";//FFFFFFFFN
    /**
     * 可安装
     * ----FFFFFFFFI对应的十六进制ACSII码
     */
    public static final String STATE_FFFFFFFFI = "464646464646464649";//FFFFFFFFI
    /**
     * 已安装
     * ----FFFFFFFFY对应的十六进制ACSII码
     */
    public static final String STATE_FFFFFFFFY = "464646464646464659";//FFFFFFFFY

    /**
     * 返回数据的状态值-成功
     */
    public static final String RESULT_SUCCESS = "53554343455353";//SUCCESS

    /**
     * 返回数据的状态值-错误
     */
    public static final String RESULT_ERROR = "4552524F52";//ERROR
    /**
     * 返回数据的状态值-未授权
     */
    public static final String RESULT_UNAUTHOR = "554E415554484F5200";//UNAUTHOR
    /**
     * 返回数据的状态值-未安装
     */
    public static final String RESULT_UNINSTALL = "554E494E5354414C4C00";//UNINSTALL

    /**
     * 字节数组转十六进制
     *
     * @param bytes
     * @return
     */
    public static String byte2Hex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            String sub = Integer.toHexString(b & 0xFF);
            if (sub.length() < 2) {
                hex.append(0);
            }
            hex.append(sub);
        }
        return hex.toString().toUpperCase();
    }

    /**
     * 十六进制字符串转字节数组
     *
     * @param hexStr
     * @return
     */
    public static byte[] hex2Byte(String hexStr) {
        byte[] arrB = hexStr.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 转换数据包
     * <p>
     * 若原数据包大小超过指定的最小数据包大小，则最终数据包大小为原始数据包大小
     *
     * @param hexRawData    十六进制原始数据
     * @param minPacketSize 转换后最小数据包大小（单位字节byte）
     * @return
     */
    public static byte[] getCommand(String hexRawData, int minPacketSize) {
        byte[] command = null;
        try {
            //补足到最小字节数
            String newData = complement(hexRawData, minPacketSize * 2);
            //转换为字节数组
            command = hex2Byte(newData);
        } catch (Throwable e) {
        }
        return command;
    }

    /**
     * 补码
     *
     * @param data      原数据
     * @param newLength 新数据长度，当新数据长度大于原数据长度时（newLength>oldLength），后面补0，
     *                  当新长度小于原数据长度时（newLength<=oldLength），不做任何处理
     * @return 补码后的新数据
     */
    public static String complement(String data, int newLength) {

        if (data == null || data.length() == 0) {
            return data;
        }

        StringBuilder newData = new StringBuilder(data);
        int len = newData.length();
        if (len < newLength) {
            int num = newLength - len;
            for (int i = 0; i < num; i++) {
                newData.append(0);
            }
            return newData.toString();
        }
        return data;
    }


    /**
     * 解析蓝牙返回数据
     *
     * @param characteristic 特征
     * @return 16进制 字符串
     */
    public static String parseResultDataToHex(BluetoothGattCharacteristic characteristic) {
        String data = null;
        try {
            data = byte2Hex(characteristic.getValue());
        } catch (Exception e) {
        }
        return data;
    }

}
