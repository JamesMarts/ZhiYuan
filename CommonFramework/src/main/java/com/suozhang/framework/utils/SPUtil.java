/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * SharedPreferences 文件操作工具类
 * Created by Moodd on 2017/2/15.
 */
public class SPUtil {
    /**
     * 保存在手机里面的文件名
     */
    private static String FILE_NAME = "app_config";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {

        put(context, FILE_NAME, key, object);
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param fileName
     * @param key
     * @param object
     */
    public static void put(Context context, String fileName, String key, Object object) {

        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {

        return get(context, FILE_NAME, key, defaultObject);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param fileName
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String fileName, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 存储一个对象
     *
     * @param context
     * @param
     * @param key
     */
    public static <T> void saveObj(Context context, String key, T obj) {
        saveObj(context, FILE_NAME, key, obj);
    }

    /**
     * 存储一个对象
     *
     * @param context
     * @param fileName
     * @param
     * @param key
     */
    public static <T> void saveObj(Context context, String fileName, String key, T obj) {
        T _obj = obj;

        SharedPreferences prefe = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 创建对象输出流,封装字节流
        ObjectOutputStream oos = null;
        try {
            // 创建对象输出流,封装字节流
            oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(_obj);
            // 将字节流编码成base64的字符串
            String list_base64 = new String(Base64.encodeToString(baos.toByteArray(), Base64.NO_PADDING));

            SharedPreferences.Editor editor = prefe.edit();
            editor.putString(key, list_base64);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();

                if (oos != null) oos.close();
            } catch (Throwable e) {
            }
        }
    }

    /**
     * @param context
     * @return
     */
    public static Object readObj(Context context, String key) {
        return readObj(context, FILE_NAME, key);
    }

    /**
     * @param context
     * @param fileName
     * @return
     */
    public static Object readObj(Context context, String fileName, String key) {
        Object obj = null;
        SharedPreferences prefe = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        String replysBase64 = prefe.getString(key, "");
        if (TextUtils.isEmpty(replysBase64)) {
            return obj;
        }
        // 读取字节
        byte[] base64 = Base64.decode(replysBase64.getBytes(), Base64.NO_PADDING);
        // 封装到字节读取流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        // 封装到对象读取流
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);

            // 读取对象
            obj = ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bais.close();

                if (ois != null) ois.close();
            } catch (Throwable e) {
            }
        }

        return obj;
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        return getAll(context, FILE_NAME);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Map<String, ?> getAll(Context context, String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        return contains(context, FILE_NAME, key);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param fileName
     * @param key
     * @return
     */
    public static boolean contains(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        remove(context, FILE_NAME, key);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param fileName
     * @param key
     */
    public static void remove(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除某个key对应的缓存
     *
     * @param key
     * @param context
     */
    public static void clearByKey(Context context, String key) {
        clearByKey(context, FILE_NAME, key);
    }

    /**
     * 清除某个key对应的缓存
     *
     * @param key
     * @param fileName
     * @param context
     */
    public static void clearByKey(Context context, String fileName, String key) {
        SharedPreferences prefe = context.getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = prefe.edit();
        editor.putString(key, "");
        editor.commit();
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        clear(context, FILE_NAME);
    }

    /**
     * 清除所有数据
     *
     * @param context
     * @param fileName
     */
    public static void clear(Context context, String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }


    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (Exception e) {
            }
            editor.commit();
        }
    }


}