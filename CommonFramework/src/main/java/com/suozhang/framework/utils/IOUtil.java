/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.utils;

import android.database.Cursor;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class IOUtil {

    private IOUtil() {
    }

    /**
     * 关闭流Closeable
     *
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable ignored) {
                ignored.printStackTrace();
            }
        }
    }

    /**
     * 关闭游标Cursor
     *
     * @param cursor
     */
    public static void closeQuietly(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable ignored) {
                ignored.printStackTrace();
            }
        }
    }

    /**
     * 从输入流中读取字节
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] readBytes(InputStream in) throws IOException {
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            return out.toByteArray();
        } finally {
            closeQuietly(out);
        }
    }

    /**
     * 从输入流中读取指定大小字节，并跳过指定字节数
     *
     * @param in
     * @param skip 跳过和丢弃此输入流中数据的 n 个字节。
     * @param size 读取大小
     * @return
     * @throws IOException
     */
    public static byte[] readBytes(InputStream in, long skip, int size) throws IOException {
        byte[] result = null;
        if (skip > 0) {
            long skipped = 0;
            while (skip > 0 && (skipped = in.skip(skip)) > 0) {
                skip -= skipped;
            }
        }
        result = new byte[size];
        for (int i = 0; i < size; i++) {
            result[i] = (byte) in.read();
        }
        return result;
    }

    /**
     * 从输入流中读取文本数据，默认UTF-8编码
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String readStr(InputStream in) throws IOException {
        return readStr(in, "UTF-8");
    }

    /**
     * 从输入流中读取文本数据，指定编码集
     *
     * @param in
     * @param charset 编码集
     * @return
     * @throws IOException
     */
    public static String readStr(InputStream in, String charset) throws IOException {
        if (TextUtils.isEmpty(charset)) charset = "UTF-8";

        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        Reader reader = new InputStreamReader(in, charset);
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[1024];
        int len;
        while ((len = reader.read(buf)) >= 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /**
     * 将文本数据写入到输出流中,默认UTF-8编码
     *
     * @param out
     * @param str
     * @throws IOException
     */
    public static void writeStr(OutputStream out, String str) throws IOException {
        writeStr(out, str, "UTF-8");
    }

    /**
     * 将文本数据写入到输出流中，指定编码
     *
     * @param out
     * @param str
     * @param charset
     * @throws IOException
     */
    public static void writeStr(OutputStream out, String str, String charset) throws IOException {
        if (TextUtils.isEmpty(charset)) charset = "UTF-8";

        Writer writer = new OutputStreamWriter(out, charset);
        writer.write(str);
        writer.flush();
    }

    /**
     * 复制
     *
     * @param in
     * @param out
     * @throws IOException
     */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        if (!(in instanceof BufferedInputStream)) {
            in = new BufferedInputStream(in);
        }
        if (!(out instanceof BufferedOutputStream)) {
            out = new BufferedOutputStream(out);
        }
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
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
