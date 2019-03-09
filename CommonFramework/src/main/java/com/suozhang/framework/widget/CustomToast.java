/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Looper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 项目名称：NewSuoZhang
 * 类描述：
 * 创建人：Moodd
 * 创建时间：2016/8/11 10:17
 * 修改人：Moodd
 * 修改时间：2016/8/11 10:17
 * 修改备注：
 */
public class CustomToast extends Toast {
    private TextView mText;


    public CustomToast(Context context) {
        super(context);
    }


    /**
     * 创建一个自定义的Toast,此方法必须在主线程中调用
     *
     * @param context
     * @param message  消息内容，字符串
     * @param duration
     * @return
     */
    public static CustomToast createToast(final Context context, final CharSequence message, final int duration) {

        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("请在主线程中调用此方法");
        }

        CustomToast toast = new CustomToast(context.getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, context.getResources().getDisplayMetrics().heightPixels / 4);

        ViewGroup view = new LinearLayout(context);
        view.setBackground(getBackgroundDrawable(view.getContext(), 3, Color.parseColor("#12c7b6")));
        view.setFocusable(false);
        view.setClickable(false);

        TextView text = new TextView(context);
        text.setFocusable(false);
        text.setClickable(false);
        text.setTextSize(16);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(0xffffffff);//字体颜色---白色，@ColorInt ARGB
        text.setText(message);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params.leftMargin = 30;
        params.rightMargin = 30;
        params.bottomMargin = 10;
        params.topMargin = 10;

        view.addView(text, params);
        toast.setView(view);
        toast.setDuration(duration);
        toast.mText = text;
        return toast;
    }

    public void setMsg(CharSequence msg) {
        if (mText != null && msg != null) {
            mText.setText(msg);
        }
    }

    public static Drawable getBackgroundDrawable(Context context, int dipRadius, int color) {
        int radius = dip2Px(context, dipRadius);
        float[] radiusArray = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};

        RoundRectShape roundRect = new RoundRectShape(radiusArray, null, null);
        ShapeDrawable bgDrawable = new ShapeDrawable(roundRect);
        bgDrawable.getPaint().setColor(color);
        return bgDrawable;
    }

    private static int dip2Px(Context context, float dip) {

        return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}
