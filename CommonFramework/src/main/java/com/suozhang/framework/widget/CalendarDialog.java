package com.suozhang.framework.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.suozhang.framework.R;
import com.suozhang.framework.utils.DateUtil;
import com.suozhang.framework.utils.DensityUtil;
import com.suozhang.framework.utils.T;

import java.util.Date;


/**
 * @author moodd
 * @email 420410175@qq.com
 * @date 2017/11/2 15:15
 */

public class CalendarDialog extends Dialog implements CalendarView.OnDateChangeListener, CalendarView.OnDateSelectedListener {
    public interface OnDateSelectedListener {
        void onDateSelected(String startDate, String endDate);
    }

    private TextView mTextMonthDay;

    private TextView mTextYear;

    private TextView mTextLunar;

    private TextView mTextCurrentDay;

    private CalendarView mCalendarView;

    private RelativeLayout mRelativeTool;

    private int mYear;

    /**
     * 是否选择整月，结束日期必须比开始日期大一整月，单选模式下无效
     */
    private boolean isSelectWholeMonth;
    /**
     * 是否单选模式 true：只选择一个
     */
    private boolean isSingleSelection;
    /**
     * 日期格式
     */
    private String dateFormat;
    /**
     * 是否为选择开始时间
     */
    private boolean isSelectStart;
    /**
     * 开始时间
     */
    private Date mStartDate;
    /**
     * 结束时间
     */
    private Date mEndDate;
    /**
     * 提示消息
     */
    private String msg;

    private OnDateSelectedListener mOnDateSelectedListener;


    public CalendarDialog(@NonNull Context context, boolean isSingleSelection) {
        this(context, isSingleSelection, DateUtil.FORMAT_YYYY_MM_DD);
    }

    public CalendarDialog(@NonNull Context context, boolean isSingleSelection, String dateFormat) {
        this(context, R.style.DialogSemitransparent);
        this.isSingleSelection = isSingleSelection;
        this.dateFormat = TextUtils.isEmpty(dateFormat) ? DateUtil.FORMAT_YYYY_MM_DD : dateFormat;
    }

    public CalendarDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_calendar_custom);
        // getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        mRelativeTool = (RelativeLayout) findViewById(R.id.rl_tool);

        mTextMonthDay = (TextView) findViewById(R.id.tv_month_day);
        mTextYear = (TextView) findViewById(R.id.tv_year);
        mTextLunar = (TextView) findViewById(R.id.tv_lunar);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mTextCurrentDay = (TextView) findViewById(R.id.tv_current_day);

        int radius = DensityUtil.dp2px(3);
        GradientDrawable topColor = new GradientDrawable();
        topColor.setColor(getContext().getResources().getColor(R.color.primary_highlight));
        //分别表示 左上 右上 右下 左下
        topColor.setCornerRadii(new float[]{radius, radius, radius, radius, 0, 0, 0, 0});
        mRelativeTool.setBackground(topColor);

        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.showSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });

        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnDateChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

    }


    @Override
    public void onDateChange(Calendar calendar) {
        updateUI(calendar);
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    private void updateUI(Calendar calendar) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        int year = calendar.getYear();
        int month = calendar.getMonth();
        int day = calendar.getDay();

        mTextMonthDay.setText(month + "月" + day + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = year;
    }


    @Override
    public void onDateSelected(Calendar calendar) {
        updateUI(calendar);

        int year = calendar.getYear();
        int month = calendar.getMonth();
        int day = calendar.getDay();

        if (mOnDateSelectedListener != null) {
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.set(year, month - 1, day, 0, 0, 0);
            Date selectDate = c.getTime();

            String startDateResult = null;
            String endDateResult = null;

            if (isSingleSelection) {
                startDateResult = DateUtil.getFormatTime(selectDate, dateFormat);
            } else {
                msg = isSelectStart ? "开始时间不能大于结束时间" : "结束时间不能小于开始时间";
                if (isSelectStart) {
                    if (mEndDate != null) {
                        if (selectDate.after(mEndDate)) {
                            T.showShort(msg);
                            return;
                        }
                        if (isSelectWholeMonth && !isWholeMonth(true, selectDate, mEndDate)) {
                            T.showShort("开始-结束日期必须间隔整月");
                            return;
                        }
                    }
                    startDateResult = DateUtil.getFormatTime(selectDate, dateFormat);
                    endDateResult = DateUtil.getFormatTime(mEndDate, dateFormat);
                } else {
                    if (mStartDate != null) {
                        if (selectDate.before(mStartDate)) {
                            T.showShort(msg);
                            return;
                        }
                        if (isSelectWholeMonth && !isWholeMonth(false, mStartDate, selectDate)) {
                            T.showShort("开始-结束日期必须间隔整月");
                            return;
                        }
                    }
                    startDateResult = DateUtil.getFormatTime(mStartDate, dateFormat);
                    endDateResult = DateUtil.getFormatTime(selectDate, dateFormat);
                }
            }
            mStartDate = null;
            mEndDate = null;

            dismiss();
            mOnDateSelectedListener.onDateSelected(startDateResult, endDateResult);
        }

    }

    /**
     * 是否整月，必须先选开始时间
     *
     * @param isSelectStart
     * @param startDate1
     * @param endDate1
     * @return
     */
    private boolean isWholeMonth(boolean isSelectStart, Date startDate1, Date endDate1) {
        Date startDate = (Date) startDate1.clone();
        Date endDate = (Date) endDate1.clone();

        if (endDate.before(startDate)) {
            return false;
        }

        java.util.Calendar start = java.util.Calendar.getInstance();
        start.setTime(startDate);

        java.util.Calendar end = java.util.Calendar.getInstance();
        end.setTime(endDate);

        int month = getMonth(startDate, endDate);
        if (isSelectStart) {
            start.add(java.util.Calendar.MONTH, month);
            end.add(java.util.Calendar.DAY_OF_MONTH, 1);
        } else {
            start.add(java.util.Calendar.MONTH, month);
            start.add(java.util.Calendar.DAY_OF_MONTH, -1);
        }
        start.set(java.util.Calendar.HOUR_OF_DAY, 0);
        start.set(java.util.Calendar.MINUTE, 0);
        start.set(java.util.Calendar.SECOND, 0);
        start.set(java.util.Calendar.MILLISECOND, 0);

        end.set(java.util.Calendar.HOUR_OF_DAY, 0);
        end.set(java.util.Calendar.MINUTE, 0);
        end.set(java.util.Calendar.SECOND, 0);
        end.set(java.util.Calendar.MILLISECOND, 0);

        return start.equals(end);
    /*
        int startYear = start.get(java.util.Calendar.YEAR);
        int startMonth = start.get(java.util.Calendar.MONTH) + 1;
        int startDay = start.get(java.util.Calendar.DAY_OF_MONTH);
        int maxStartDay = start.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);


        int endYear = end.get(java.util.Calendar.YEAR);
        int endMonth = end.get(java.util.Calendar.MONTH) + 1;
        int endDay = end.get(java.util.Calendar.DAY_OF_MONTH);
        int maxEndDay = end.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

        long startTime = start.getTimeInMillis();
        long endTime = end.getTimeInMillis();
        */


    }

    /**
     * 计算2个日期相差的月数
     *
     * @param start
     * @param end
     * @return
     */
    public int getMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        java.util.Calendar startCalendar = java.util.Calendar.getInstance();
        startCalendar.setTime(start);
        java.util.Calendar endCalendar = java.util.Calendar.getInstance();
        endCalendar.setTime(end);
        java.util.Calendar temp = java.util.Calendar.getInstance();
        temp.setTime(end);
        temp.add(java.util.Calendar.DATE, 1);

        int year = endCalendar.get(java.util.Calendar.YEAR) - startCalendar.get(java.util.Calendar.YEAR);
        int month = endCalendar.get(java.util.Calendar.MONTH) - startCalendar.get(java.util.Calendar.MONTH);

        if ((startCalendar.get(java.util.Calendar.DATE) == 1) && (temp.get(java.util.Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(java.util.Calendar.DATE) != 1) && (temp.get(java.util.Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(java.util.Calendar.DATE) == 1) && (temp.get(java.util.Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }

    /**
     * 显示日期选择
     * <p>
     * 单选模式下不可调用此方法
     *
     * @param isSelectStart 是否选择开始时间 true:选择开始时间 false：选择结束时间
     * @param startDate     已选择的开始时间
     * @param endDate       已选择结束时间
     * @throws RuntimeException
     */
    public void show(boolean isSelectStart, String startDate, String endDate) throws RuntimeException {
        this.show(false, isSelectStart, startDate, endDate);
    }

    /**
     * 显示日期选择
     * <p>
     * 单选模式下不可调用此方法
     *
     * @param isSelectStart 是否选择开始时间 true:选择开始时间 false：选择结束时间
     * @param startDate     已选择的开始时间
     * @param endDate       已选择结束时间
     * @throws RuntimeException
     */
    public void show(boolean isSelectStart, Date startDate, Date endDate) throws RuntimeException {
        this.show(false, isSelectStart, startDate, endDate);
    }

    /**
     * 显示日期选择
     * <p>
     * 单选模式下不可调用此方法
     *
     * @param isSelectWholeMonth 是否选择整月，结束日期必须比开始日期大一整月，单选模式下无效
     * @param isSelectStart      是否选择开始时间 true:选择开始时间 false：选择结束时间
     * @param startDate          已选择的开始时间
     * @param endDate            已选择结束时间
     * @throws RuntimeException
     */
    public void show(boolean isSelectWholeMonth, boolean isSelectStart, String startDate, String endDate) throws RuntimeException {
        this.show(isSelectWholeMonth, isSelectStart, DateUtil.getDateTime(startDate, dateFormat), DateUtil.getDateTime(endDate, dateFormat));
    }

    /**
     * 显示日期选择
     * <p>
     * 单选模式下不可调用此方法
     *
     * @param isSelectWholeMonth 是否选择整月，结束日期必须比开始日期大一整月，单选模式下无效
     * @param isSelectStart      是否选择开始时间 true:选择开始时间 false：选择结束时间
     * @param startDate          已选择的开始时间
     * @param endDate            已选择结束时间
     * @throws RuntimeException
     */
    public void show(boolean isSelectWholeMonth, boolean isSelectStart, Date startDate, Date endDate) throws RuntimeException {
        if (isSingleSelection) {
            throw new RuntimeException("单选模式下不可调用此方法");
        }
        this.isSelectWholeMonth = isSelectWholeMonth;
        this.isSelectStart = isSelectStart;
        this.mStartDate = DateUtil.getDateTime(startDate, dateFormat);
        this.mEndDate = DateUtil.getDateTime(endDate, dateFormat);
        show();
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.mOnDateSelectedListener = listener;
    }
}
