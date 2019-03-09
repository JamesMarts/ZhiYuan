/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.widget.pickerview;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.suozhang.framework.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 时间选择器
 *
 * @author Sai
 */
public class TimePopupWindow extends PopupWindow implements OnClickListener {


    public enum Type {
        ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN, YEAR_MONTH
    }// 四种选择模式，年月日时分，年月日，时分，月日时分

    public static final String FORMAT_HH_MM = "HH:mm";
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_MM_DD_HH_MM = "MM-dd HH:mm";

    private View rootView; // 总的布局
    WheelTime wheelTime;
    private Button btnSubmit, btnCancel;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private OnTimeSelectListener timeSelectListener;

    public TimePopupWindow(Context context, Type type) {
        super(context);
        this.setWidth(LayoutParams.MATCH_PARENT);
        //this.setWidth((int) (context.getResources().getDisplayMetrics().widthPixels * 0.9));
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());// 这样设置才能点击屏幕外dismiss窗口
        this.setOutsideTouchable(true);
        //this.setAnimationStyle(R.style.timepopwindow_anim_style);

        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        rootView = mLayoutInflater.inflate(R.layout.pw_time, null);
        // -----确定和取消按钮
        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        // ----时间转轮
        final View timepickerview = rootView.findViewById(R.id.timepicker);
        wheelTime = new WheelTime(timepickerview, type);
        // wheelTime.setIsHanzi(true);

        wheelTime.screenheight = context.getResources().getDisplayMetrics().heightPixels;

        //默认选中当前时间
        setTime(new Date());

        setContentView(rootView);
    }

    /**
     * 设置按钮颜色
     *
     * @param color
     */
    public void setBtnColor(@ColorInt int color) {
        if (btnSubmit != null) {
            btnSubmit.setTextColor(color);
        }
        if (btnCancel != null) {
            btnCancel.setTextColor(color);
        }
    }

    /**
     * 设置确定按钮文字及颜色
     *
     * @param text
     * @param color
     */
    public void setBtnSubmit(String text, @ColorInt int color) {
        if (btnSubmit != null) {
            btnSubmit.setText(text);
            btnSubmit.setTextColor(color);
        }
    }

    /**
     * 设置取消按钮文字及颜色
     *
     * @param text
     * @param color
     */
    public void setBtnCancel(String text, @ColorInt int color) {
        if (btnCancel != null) {
            btnCancel.setText(text);
            btnCancel.setTextColor(color);
        }
    }

    public WheelTime getWheelTime() {
        return wheelTime;
    }

    /**
     * 设置是否显示汉字
     *
     * @param isHanzi
     */
    public void setIsHanzi(boolean isHanzi) {
        if (wheelTime != null) wheelTime.setIsHanzi(isHanzi);
    }

    /**
     * 设置可以选择的时间范围
     * 设置完后需要调用setTime()重新设置时间，否则会造成年显示错误
     *
     * @param START_YEAR
     * @param END_YEAR
     */
    public void setRange(int START_YEAR, int END_YEAR) {
        wheelTime.setSTART_YEAR(START_YEAR);
        wheelTime.setEND_YEAR(END_YEAR);
    }

    /**
     * 设置选中时间
     *
     * @param date
     */
    public void setTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null)
            calendar.setTimeInMillis(System.currentTimeMillis());
        else
            calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);
    }

    /**
     * 设置最大滚动日期
     * 需要在setTime()前调用，否则无效
     *
     * @param maxScrollDate
     */
    public void setMaxScrollDate(Date maxScrollDate) {
        this.wheelTime.setMaxScrollDate(maxScrollDate);
    }

    /**
     * 设置选中值字体颜色
     *
     * @param color
     */
    public void setValueTextColor(@ColorInt int color) {
        this.wheelTime.setValueTextColor(color);
    }

    /**
     * 设置条目值字体颜色
     *
     * @param color
     */
    public void setItemTextColor(@ColorInt int color) {
        this.wheelTime.setItemTextColor(color);
    }

    /**
     * 指定选中的时间，显示选择器
     *
     * @param parent
     * @param gravity
     * @param x
     * @param y
     * @param date
     */
    public void showAtLocation(View parent, int gravity, int x, int y, Date date) {
        setTime(date);
        update();
        super.showAtLocation(parent, gravity, x, y);
    }

    /**
     * 显示在指定View的下方
     *
     * @param parent
     * @param date
     */
    public void showAsDropDown(View parent, Date date) {
        setTime(date);
        update();
        int screenW = parent.getResources().getDisplayMetrics().widthPixels;
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        int s = this.getWidth() / 2;
        int centreOffset = -(parent.getWidth() / 2 - (screenW / 2 - location[0]));
        int xoff = (-this.getWidth() / 2 + parent.getWidth() / 2 + centreOffset);
        super.showAsDropDown(parent, xoff, 0);
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wheelTime.setCyclic(cyclic);
    }

    /**
     * 设置是否循环滚动-年
     *
     * @param cyclic
     */
    public void setCyclicYear(boolean cyclic) {
        wheelTime.setCyclicYear(cyclic);
    }

    /**
     * 设置是否循环滚动-月
     *
     * @param cyclic
     */
    public void setCyclicMonth(boolean cyclic) {
        wheelTime.setCyclicMonth(cyclic);
    }

    /**
     * 设置是否循环滚动-日
     *
     * @param cyclic
     */
    public void setCyclicDay(boolean cyclic) {
        wheelTime.setCyclicDay(cyclic);
    }

    /**
     * 设置是否循环滚动-时
     *
     * @param cyclic
     */
    public void setCyclicHours(boolean cyclic) {
        wheelTime.setCyclicHours(cyclic);
    }

    /**
     * 设置是否循环滚动-分
     *
     * @param cyclic
     */
    public void setCyclicMins(boolean cyclic) {
        wheelTime.setCyclicMins(cyclic);
    }

    /**
     * 格式化日期为字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            dismiss();
            return;
        } else {
            if (timeSelectListener != null) {
                try {
                    Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                    timeSelectListener.onTimeSelect(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dismiss();
            return;
        }
    }

    public interface OnTimeSelectListener {
        public void onTimeSelect(Date date);
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.timeSelectListener = timeSelectListener;
    }

}
