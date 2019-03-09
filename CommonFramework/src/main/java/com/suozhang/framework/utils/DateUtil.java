package com.suozhang.framework.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author moodd
 * @email 420410175@qq.com
 * @date 2017/8/31 15:19
 */

public class DateUtil {
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_YYYY_MM = "yyyy-MM";
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String FORMAT_MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String FORMAT_MM_DD = "MM-dd";
    public static final String FORMAT_HH_MM = "HH:mm";

    public static final long MINUTES = 60 * 1000;
    public static final long HOURS = 60 * MINUTES;
    public static final long DAY = 24 * HOURS;

    /**
     * 获取格式化后的时间 例如：yyyy-MM-dd HH:mm:ss --> yyyy-MM-dd
     * 默认源时间格式为yyyy-MM-dd HH:mm:ss
     *
     * @param sourceTime   源时间格式类型的字符串
     * @param targetFormat 目标时间对应的格式化类型
     * @return
     */
    public static String getFormatTime(String sourceTime, String targetFormat) {

        return getFormatTime(sourceTime, FORMAT_YYYY_MM_DD_HH_MM_SS, targetFormat);
    }

    /**
     * 获取格式化后的时间 例如：yyyy-MM-dd HH:mm:ss --> yyyy-MM-dd
     *
     * @param sourceTime   源时间格式类型的字符串
     * @param sourceFormat 源时间对应的格式化类型
     * @param targetFormat 目标时间对应的格式化类型
     * @return
     */
    public static String getFormatTime(String sourceTime, String sourceFormat, String targetFormat) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(targetFormat);
            return df.format(getDateTime(sourceTime, sourceFormat));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取格式化后的时间
     *
     * @param sourceTime
     * @param targetFormat
     * @return
     */
    public static String getFormatTime(Date sourceTime, String targetFormat) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(targetFormat);
            return df.format(sourceTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取Date类型的时间
     *
     * @param sourceTime   源时间格式类型的字符串
     * @param sourceFormat 源时间对应的格式化类型
     * @return
     */
    public static Date getDateTime(String sourceTime, String sourceFormat) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(TextUtils.isEmpty(sourceFormat) ? FORMAT_YYYY_MM_DD_HH_MM_SS : sourceFormat);
            return df.parse(sourceTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Date类型的时间
     *
     * @param sourceTime
     * @param targetFormat
     * @return
     */
    public static Date getDateTime(Date sourceTime, String targetFormat) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(targetFormat);
            return df.parse(df.format(sourceTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 格式化时间到：刚刚，N小时前，昨天，Y天前
     * <p>
     * N<=24,Y<=3
     *
     * @param time
     * @return
     */
    public static String formatTimeToState(String time) {
        return formatTimeToState(getDateTime(time, FORMAT_YYYY_MM_DD_HH_MM_SS));
    }

    /**
     * 格式化时间到：刚刚，N小时前，昨天，Y天前
     * <p>
     * N<=24,Y<=3
     *
     * @param time
     * @return
     */
    public static String formatTimeToState(Date time) {
        if (time == null) {
            return "";
        }
        Date currentDate = new Date();
        long currentTime = currentDate.getTime();
        long targetTime = time.getTime();

        String defResultTime = getFormatTime(time, FORMAT_YYYY_MM_DD);

        if (currentTime < targetTime) {
            return defResultTime;
        }

        boolean isYesterday = currentDate.getDay() - time.getDay() == 1;
        long intervalTime = currentTime - targetTime;

        if (isYesterday) {
            return "昨天";
        }

        if (intervalTime <= MINUTES * 5) {
            return "刚刚";
        }

        if (intervalTime <= HOURS * 24) {
            return intervalTime / HOURS + "小时前";
        }

        if (intervalTime <= DAY * 3) {
            return intervalTime / DAY + "天前";
        }

        return defResultTime;
    }

}
