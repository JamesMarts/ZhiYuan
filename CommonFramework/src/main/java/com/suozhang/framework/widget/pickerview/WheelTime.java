/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.widget.pickerview;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.View;

import com.suozhang.framework.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class WheelTime {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_mins;
    public int screenheight;
    public boolean isShowHanzi = false;

    private TimePopupWindow.Type type;
    //开始时间和结束时间
    private int START_YEAR = 1990, END_YEAR = 2100;

    //最大滚动时间
    private Date maxScrollDate;

    public Date getMaxScrollDate() {
        return maxScrollDate;
    }

    public void setMaxScrollDate(Date maxScrollDate) {
        this.maxScrollDate = maxScrollDate;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getSTART_YEAR() {
        return START_YEAR;
    }

    public void setSTART_YEAR(int sTART_YEAR) {
        START_YEAR = sTART_YEAR;
    }

    public int getEND_YEAR() {
        return END_YEAR;
    }

    public void setEND_YEAR(int eND_YEAR) {
        END_YEAR = eND_YEAR;
    }

    public WheelTime(View view) {
        super();
        this.view = view;
        type = TimePopupWindow.Type.ALL;
        setView(view);
    }

    public WheelTime(View view, TimePopupWindow.Type type) {
        super();
        this.view = view;
        this.type = type;
        setView(view);
    }

    public void setPicker(int year, int month, int day) {
        this.setPicker(year, month, day, 0, 0);
    }

    public void setIsHanzi(boolean flag) {
        isShowHanzi = flag;
    }

    /**
     * @Description: TODO 弹出日期时间选择器
     */
    public void setPicker(int year, int month, int day, int h, int m) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        Context context = view.getContext();
        // 年
        wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据

        if (isShowHanzi) {
            wv_year.setLabel(context.getString(R.string.pickerview_year));// 添加文字
        }

        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

        // 月
        wv_month = (WheelView) view.findViewById(R.id.month);
        wv_month.setAdapter(new NumericWheelAdapter(1, 12));

        if (isShowHanzi) {
            wv_month.setLabel(context.getString(R.string.pickerview_month));
        }
        wv_month.setCurrentItem(month);

        // 日
        wv_day = (WheelView) view.findViewById(R.id.day);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 31));
        } else if (list_little.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 30));
        } else {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                wv_day.setAdapter(new NumericWheelAdapter(1, 29));
            else
                wv_day.setAdapter(new NumericWheelAdapter(1, 28));
        }

        if (isShowHanzi) {
            wv_day.setLabel(context.getString(R.string.pickerview_day));
        }
        wv_day.setCurrentItem(day - 1);


        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_hours.setAdapter(new NumericWheelAdapter(0, 23));

        if (isShowHanzi) {
            wv_hours.setLabel(context.getString(R.string.pickerview_hours));// 添加文字
        }
        wv_hours.setCurrentItem(h);

        wv_mins = (WheelView) view.findViewById(R.id.min);
        wv_mins.setAdapter(new NumericWheelAdapter(0, 59));

        if (isShowHanzi) {
            wv_mins.setLabel(context.getString(R.string.pickerview_minutes));// 添加文字
        }
        wv_mins.setCurrentItem(m);

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + START_YEAR;
                // 判断大小月及是否闰年,用来确定"日"的数据
                int maxItem = 30;
                if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                    maxItem = 31;
                } else if (list_little.contains(String.valueOf(wv_month
                        .getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                    maxItem = 30;
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0)
                            || year_num % 400 == 0) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                        maxItem = 29;
                    } else {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                        maxItem = 28;
                    }
                }
                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }

            }
        };
        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                int maxItem = 30;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                    maxItem = 31;
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                    maxItem = 30;
                } else {
                    if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
                            .getCurrentItem() + START_YEAR) % 100 != 0)
                            || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                        maxItem = 29;
                    } else {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                        maxItem = 28;
                    }
                }
                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }
            }
        };
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 0;
        switch (type) {
            case ALL:
                float scale = isShowHanzi ? 2.5f : 3;
                textSize = (int) ((screenheight / 100) * scale);//*3
                break;
            case YEAR_MONTH_DAY:
                textSize = (screenheight / 100) * 4;
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
                break;
            case HOURS_MINS:
                textSize = (screenheight / 100) * 4;
                wv_year.setVisibility(View.GONE);
                wv_month.setVisibility(View.GONE);
                wv_day.setVisibility(View.GONE);
                break;
            case MONTH_DAY_HOUR_MIN:
                textSize = (screenheight / 100) * 3;
                wv_year.setVisibility(View.GONE);
                break;
            case YEAR_MONTH:
                textSize = (screenheight / 100) * 4;
                wv_day.setVisibility(View.GONE);
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
            default:
                break;
        }
        //textSize = 70;
        wv_day.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
        wv_hours.TEXT_SIZE = textSize;
        wv_mins.TEXT_SIZE = textSize;

        //设置最大滚动值
        setMaxScrollValue();
    }

    /**
     * 设置最大滚动值
     */
    private void setMaxScrollValue() {

        if (maxScrollDate == null) return;

        Calendar c = Calendar.getInstance();
        c.setTime(maxScrollDate);
        final int year = c.get(Calendar.YEAR) - START_YEAR;
        final int month = c.get(Calendar.MONTH) + 1;
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int hours = c.get(Calendar.HOUR_OF_DAY);
        final int mins = c.get(Calendar.MINUTE);

        wv_year.setMaxScrollValue(year);

        wv_month.setMaxScrollValue(month - 1);
        wv_day.setMaxScrollValue(day - 1);
        wv_hours.setMaxScrollValue(hours);
        wv_mins.setMaxScrollValue(mins);

        wv_year.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (newValue < year) {
                    wv_month.setMaxScrollValue(-1);
                    wv_day.setMaxScrollValue(-1);
                    wv_hours.setMaxScrollValue(-1);
                    wv_mins.setMaxScrollValue(-1);
                } else {
                    wv_month.setMaxScrollValue(month - 1);
                    wv_day.setMaxScrollValue(day - 1);
                    wv_hours.setMaxScrollValue(hours);
                    wv_mins.setMaxScrollValue(mins);
                }
            }
        });

        wv_month.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int newMonth = newValue + 1;
                int newYear = wv_year.getCurrentItem();

                if (newMonth < month) {
                    wv_day.setMaxScrollValue(-1);
                    wv_hours.setMaxScrollValue(-1);
                    wv_mins.setMaxScrollValue(-1);
                } else {
                    if (newYear >= year) {
                        wv_day.setMaxScrollValue(day - 1);
                        wv_hours.setMaxScrollValue(hours);
                        wv_mins.setMaxScrollValue(mins);
                    }
                }
            }
        });
        wv_day.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int newMonth = wv_month.getCurrentItem() + 1;
                int newYear = wv_year.getCurrentItem();
                if (newValue < day - 1) {
                    wv_hours.setMaxScrollValue(-1);
                    wv_mins.setMaxScrollValue(-1);
                } else {
                    if (newYear >= year && newMonth >= month) {
                        wv_hours.setMaxScrollValue(hours);
                        wv_mins.setMaxScrollValue(mins);
                    }
                }
            }
        });

        wv_hours.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int newMonth = wv_month.getCurrentItem() + 1;
                int newYear = wv_year.getCurrentItem();
                int newDay = wv_day.getCurrentItem();
                if (newValue < hours) {
                    wv_mins.setMaxScrollValue(-1);
                } else {
                    if (newYear >= year && newMonth >= month && newDay >= day - 1) {
                        wv_mins.setMaxScrollValue(mins);
                    }
                }
            }
        });

    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wv_year.setCyclic(cyclic);
        wv_month.setCyclic(cyclic);
        wv_day.setCyclic(cyclic);
        wv_hours.setCyclic(cyclic);
        wv_mins.setCyclic(cyclic);
    }

    /**
     * 设置选中值字体颜色
     *
     * @param color
     */
    public void setValueTextColor(@ColorInt int color) {
        wv_year.setValueTextColor(color);
        wv_month.setValueTextColor(color);
        wv_day.setValueTextColor(color);
        wv_hours.setValueTextColor(color);
        wv_mins.setValueTextColor(color);
    }

    /**
     * 设置条目值字体颜色
     *
     * @param color
     */
    public void setItemTextColor(@ColorInt int color) {
        wv_year.setItemTextColor(color);
        wv_month.setItemTextColor(color);
        wv_day.setItemTextColor(color);
        wv_hours.setItemTextColor(color);
        wv_mins.setItemTextColor(color);
    }


    /**
     * 设置是否循环滚动-年
     *
     * @param cyclic
     */
    public void setCyclicYear(boolean cyclic) {
        wv_year.setCyclic(cyclic);
    }

    /**
     * 设置是否循环滚动-月
     *
     * @param cyclic
     */
    public void setCyclicMonth(boolean cyclic) {
        wv_month.setCyclic(cyclic);
    }

    /**
     * 设置是否循环滚动-日
     *
     * @param cyclic
     */
    public void setCyclicDay(boolean cyclic) {
        wv_day.setCyclic(cyclic);
    }

    /**
     * 设置是否循环滚动-时
     *
     * @param cyclic
     */
    public void setCyclicHours(boolean cyclic) {
        wv_hours.setCyclic(cyclic);
    }

    /**
     * 设置是否循环滚动-分
     *
     * @param cyclic
     */
    public void setCyclicMins(boolean cyclic) {
        wv_mins.setCyclic(cyclic);
    }


    public String getTime() {
        StringBuffer sb = new StringBuffer();
        sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
                .append((wv_month.getCurrentItem() + 1)).append("-")
                .append((wv_day.getCurrentItem() + 1)).append(" ")
                .append(wv_hours.getCurrentItem()).append(":")
                .append(wv_mins.getCurrentItem());
        return sb.toString();
    }
}
