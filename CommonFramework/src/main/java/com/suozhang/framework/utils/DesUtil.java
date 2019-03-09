/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加密工具类
 * Created by Moodd on 2017/2/15.
 */
public class DesUtil {
    private static final Object sLock = new Object();

    private Cipher encryptCipher = null;
    private Cipher decryptCipher = null;


    public DesUtil() {
    }

    public DesUtil(String key) {
        initKey(key);
    }

    public void initKey(String strKey) {

        if (TextUtils.isEmpty(strKey)) {
            throw new NullPointerException("密钥不能为空");
        }
        try {
            Key key = getKey(strKey.getBytes());
            encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成8位钥匙-最多取8位长度
     *
     * @param arrBTmp
     * @return
     * @throws Exception
     */
    private Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // 生成密钥
        Key key = new SecretKeySpec(arrB, "DES");
        return key;
    }

    /**
     * 加密
     *
     * @param arrB
     * @return
     * @throws Exception
     */
    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密-Des加密后再Base64加密 Base64模式-NO_WRAP忽略换行符
     *
     * @param strIn
     * @return
     * @throws Exception
     */
    public String encrypt(String strIn) throws Exception {
        // return byteArr2HexStr(encrypt(strIn.getBytes()));
        byte[] bytes = encrypt(strIn.getBytes());
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    /**
     * 解密
     *
     * @param arrB
     * @return
     * @throws Exception
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密 -Base64解码后再解密 Base64模式-NO_WRAP忽略换行符
     *
     * @param strIn
     * @return
     * @throws Exception
     */
    public String decrypt(String strIn) throws Exception {
        // return new String(decrypt(hexStr2ByteArr(strIn)));
        byte[] bytes = decrypt(Base64.decode(strIn.getBytes(), Base64.NO_WRAP));
        return new String(bytes);
    }


    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

}