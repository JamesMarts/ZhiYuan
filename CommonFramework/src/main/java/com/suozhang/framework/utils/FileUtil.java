/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.utils;

import android.os.Environment;
import android.os.StatFs;

import com.suozhang.framework.framework.AM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;

/**
 * 文件操作工具类
 * Created by Moodd on 2017/2/15.
 */
public class FileUtil {

    private FileUtil() {
    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 是否存在SD卡
     *
     * @return
     */
    public static Boolean existsSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取应用缓存文件路径
     * 优先获取外部存储路径，否则获取内部存储路径
     *
     * @param dirName
     * @return
     */
    public static File getCacheDir(String dirName) {
        File result;
        if (existsSdcard()) {
            //获取外部存储
            File cacheDir = AM.app().getExternalCacheDir();
            if (cacheDir == null) {
                result = new File(Environment.getExternalStorageDirectory(),
                        "Android/data/" + AM.app().getPackageName() + "/cache/" + dirName);
            } else {
                result = new File(cacheDir, dirName);
            }
        } else {
            //获取内部存储
            result = new File(AM.app().getCacheDir(), dirName);
        }
        if (result.exists() || result.mkdirs()) {
            return result;
        } else {
            return null;
        }
    }

    /**
     * 获取应用文件路径
     * 优先获取外部存储路径，否则获取内部存储路径
     *
     * @param dirName
     * @return
     */
    public static File getFilesDir(String dirName) {
        File result;
        if (existsSdcard()) {
            //获取外部存储
            File filesDir = AM.app().getExternalFilesDir("");
            if (filesDir == null) {
                result = new File(Environment.getExternalStorageDirectory(),
                        "Android/data/" + AM.app().getPackageName() + "/files/" + dirName);
            } else {
                result = new File(filesDir, dirName);
            }
        } else {
            //获取内部存储
            result = new File(AM.app().getFilesDir(), dirName);
        }
        if (result.exists() || result.mkdirs()) {
            return result;
        } else {
            return null;
        }
    }

    /**
     * 获取App缓存目录大小
     *
     * @return
     */
    public static String getAppCacheSize() {
        long cache = getFileOrDirSize(AM.app().getCacheDir());
        if (existsSdcard()) {
            cache += getFileOrDirSize(AM.app().getExternalCacheDir());
        }
        return getFormatSize(cache);
    }

    /**
     * 清除App缓存目录
     */
    public static void clearAppCache() {
        deleteFileOrDir(AM.app().getCacheDir());
        if (existsSdcard()) {
            deleteFileOrDir(AM.app().getExternalCacheDir());
        }
    }

    /**
     * 检查磁盘空间是否大于10mb
     *
     * @return true 大于
     */
    public static boolean isDiskAvailable() {
        long size = getDiskAvailableSize();
        return size > 10 * 1024 * 1024; // > 10bm
    }

    /**
     * 获取磁盘可用空间
     *
     * @return byte 单位 kb
     */
    public static long getDiskAvailableSize() {
        if (!existsSdcard()) return 0;
        File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        StatFs stat = new StatFs(path.getAbsolutePath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
        // (availableBlocks * blockSize)/1024 KIB 单位
        // (availableBlocks * blockSize)/1024 /1024 MIB单位
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }


    /**
     * 获取文件或文件夹大小
     *
     * @param file
     * @return
     */
    public static long getFileOrDirSize(File file) {
        if (file == null || !file.exists()) return 0;
        if (!file.isDirectory()) return file.length();

        long length = 0;
        File[] list = file.listFiles();
        if (list != null) { // 文件夹被删除时, 子文件正在被写入, 文件属性异常返回null.
            for (File item : list) {
                length += getFileOrDirSize(item);
            }
        }

        return length;
    }

    /**
     * 复制文件到指定文件
     *
     * @param fromPath 源文件
     * @param toPath   复制到的文件
     * @return true 成功，false 失败
     */
    public static boolean copy(String fromPath, String toPath) {
        boolean result = false;
        File from = new File(fromPath);
        if (!from.exists()) {
            return result;
        }

        File toFile = new File(toPath);
        deleteFileOrDir(toFile);
        File toDir = toFile.getParentFile();
        if (toDir.exists() || toDir.mkdirs()) {
            FileInputStream in = null;
            FileOutputStream out = null;
            try {
                in = new FileInputStream(from);
                out = new FileOutputStream(toFile);
                IOUtil.copy(in, out);
                result = true;
            } catch (Throwable ex) {
                //L.d(ex.getMessage(), ex);
                result = false;
            } finally {
                IOUtil.closeQuietly(in);
                IOUtil.closeQuietly(out);
            }
        }
        return result;
    }

    /**
     * 删除文件或文件夹（文件夹下所有文件均被删除），使用时，请慎重，如传入为SD卡根路径，则SD卡均会被清空
     *
     * @param path
     * @return
     */
    public static boolean deleteFileOrDir(File path) {
        if (path == null || !path.exists()) {
            return true;
        }
        if (path.isFile()) {
            return path.delete();
        }
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                deleteFileOrDir(file);
            }
        }
        return path.delete();
    }
}
