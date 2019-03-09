/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.suozhang.framework.utils.DensityUtil;


/**
 * @author Moodd
 * @email 420410175@qq.com
 * @date 2017/5/10 10:53
 */


public class ScanProgressBar extends View {
    //进度条颜色
    private int progressColor = 0xff12c7b6;

    //默认进度条颜色
    private int secondaryProgressColor = 0xffe3e2e2;

    //文字进度颜色
    private int textProgressColor = 0xff12c7b6;

    private int scanColor = 0x1912C7B6;


    //刻度总数
    private int mTickCount;

    //起始角
    private float mStartAngle = 135;
    //结束角
    private float mEndAngle = 45;

    //刻度间隔角度
    private float mTickSpaceAngle = 5;

    //刻度角度，刻度大小
    private float mTickAngle = 1f;

    //正常刻度的长度
    private float mNormalTickSize = 65;

    //当前刻度的长度
    private float mCurrentTickSize = 82;

    //扫描刻度的长度
    private float mScanTickSize = 60;

    //当前进度
    private int mCurrentProgress = 0;

    //是否扫描 默认是
    private boolean isScan = false;

    //扫描，每次旋转的偏移量
    private float mRotateAngleOffset = 0;


    private Paint mPaint;
    private Paint mTextPaint;
    private RectF mOval;
    private RectF mScanOvalBig;
    private RectF mScanOvalSmall;
    private int mRadius;
    private float mTextWidth;
    private float mTextOffset;

    private Thread uiThread;


    public ScanProgressBar(Context context) {
        this(context, null);
    }

    public ScanProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        uiThread = Thread.currentThread();

        mPaint = new Paint();
        //设置画笔颜色
        mPaint.setColor(Color.WHITE);
        //设置画笔抗锯齿
        mPaint.setAntiAlias(true);
        //让画出的图形是空心的(不填充)
        mPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTypeface(Typeface.MONOSPACE); //设置字体
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(textProgressColor);
        mTextPaint.setTextSize(DensityUtil.sp2px(13));

        mTextWidth = mTextPaint.measureText("100%");
        mTextOffset = getTextPaintOffset(mTextPaint);


        //计算需要绘制的总角度
        float sweepAngle = 360f - mStartAngle + mEndAngle;
        //总刻度数量
        mTickCount = (int) (sweepAngle / (mTickSpaceAngle + mTickAngle)) + 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mRadius == 0) {
            int width = measureSpec(widthMeasureSpec);
            int height = measureSpec(heightMeasureSpec);
            //以最小值为正方形的长
            int len = Math.min(width, height);
            mRadius = len / 2;

            //刻度矩形
            float padding = mCurrentTickSize / 2 + mTextWidth;
            mOval = new RectF(padding, padding, len - padding, len - padding);
            //大扫描圆环矩形
            padding = padding + mCurrentTickSize + (mScanTickSize * 0.65f);
            mScanOvalBig = new RectF(padding, padding, len - padding, len - padding);
            //小扫描圆环矩形
            padding -= (mScanTickSize * 0.65f) / 4;
            mScanOvalSmall = new RectF(padding, padding, len - padding, len - padding);

            setMeasuredDimension(len, len);
        }
    }

    private int measureSpec(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.max(result, specSize);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgress(canvas);
        drawScan(canvas);
    }

    /**
     * 画刻度
     *
     * @param canvas
     */
    private void drawProgress(Canvas canvas) {

        mPaint.setStyle(Paint.Style.STROKE);

        final int currentBlockIndex = (int) (mCurrentProgress / 100f * mTickCount);
        for (int i = 0; i < mTickCount; i++) {
            float startAngle = mStartAngle + (i * (mTickAngle + mTickSpaceAngle));
            if (i == 0 && currentBlockIndex == 0) {
                drawProgressText(canvas, startAngle);
            }
            if (i == currentBlockIndex - 1) {
                //当前刻度，刻度长度>正常刻度
                mPaint.setStrokeWidth(mCurrentTickSize);
                drawProgressText(canvas, startAngle);
                //当前刻度加粗
                canvas.drawArc(mOval, startAngle, mTickAngle + 0.25f, false, mPaint);
                continue;
            } else if (i < currentBlockIndex) {
                //已选中的刻度
                mPaint.setStrokeWidth(mNormalTickSize);
                mPaint.setColor(progressColor);
            } else {
                //未选中的刻度
                mPaint.setStrokeWidth(mNormalTickSize);
                mPaint.setColor(secondaryProgressColor);
            }

            canvas.drawArc(mOval, startAngle, mTickAngle, false, mPaint);
        }
    }

    /**
     * 画进度文字百分比
     *
     * @param canvas
     * @param angle
     */
    private void drawProgressText(Canvas canvas, float angle) {
        canvas.save();
        canvas.translate(mRadius, mRadius);
        String text = mCurrentProgress + "%";
        canvas.drawText(text, getCosX(angle), getSinY(angle), mTextPaint);
        canvas.restore();
    }

    /**
     * 画扫描背景
     *
     * @param canvas
     */
    private void drawScan(Canvas canvas) {

        if (isScan) {
            //绘制小圆-顺时针旋转
            canvas.save();
            canvas.rotate(mRotateAngleOffset, mRadius, mRadius);
            drawScanSmall(canvas);
            canvas.restore();
            //绘制大圆-逆时针旋转
            canvas.save();
            canvas.rotate(-mRotateAngleOffset, mRadius, mRadius);
            drawScanBig(canvas);
            canvas.restore();

            //下一次旋转偏移量
            mRotateAngleOffset = (mRotateAngleOffset + 3) % 360;

            //重绘，一直旋转
            invalidate();
        } else {
            drawScanComplete(canvas);
        }
    }

    /**
     * 画大圆环
     *
     * @param canvas
     */
    private void drawScanBig(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE); //设置空心
        mPaint.setColor(scanColor);
        mPaint.setStrokeWidth(mScanTickSize);
        for (int i = 0; i < 3; i++) {
            float startAngle = i * 120;
            canvas.drawArc(mScanOvalBig, startAngle, 60, false, mPaint);
        }

    }

    /**
     * 画小圆环
     *
     * @param canvas
     */
    private void drawScanSmall(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE); //设置空心
        mPaint.setColor(scanColor);
        mPaint.setStrokeWidth(mScanTickSize * 0.65f);

        for (int i = 0; i < 6; i++) {
            float startAngle = i * 120 ;
            canvas.drawArc(mScanOvalSmall, startAngle, 60, false, mPaint);
        }
    }

    /**
     * 画小圆环
     *
     * @param canvas
     */
    private void drawScanComplete(Canvas canvas) {

        mPaint.setColor(scanColor);
        mPaint.setStyle(Paint.Style.FILL); //设置实心
        float radius = mScanOvalSmall.width() / 2;

        canvas.drawCircle(mRadius, mRadius, radius, mPaint);
    }


    //根据角获得X坐标
    private float getCosX(float angle) {
        float textW = mTextWidth / 2;
        float x = (float) ((mRadius - textW) * Math.cos(angle * Math.PI / 180)) + mTextOffset;
        if (x < 0) {
            x -= textW / 2;
        }
        return x;
    }

    private float getSinY(float angle) {
        float textW = mTextWidth / 2;

        float y = (float) ((mRadius - textW) * Math.sin(angle * Math.PI / 180)) + mTextOffset;
        if (y > 0) {
            y -= textW / 2;
        }
        return y;
    }

    public float getTextPaintOffset(Paint paint) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return -fontMetrics.descent + (fontMetrics.bottom - fontMetrics.top) / 2;
    }

    /**
     * 设置进度
     *
     * @param percent 百分百
     */
    public void setProgress(int percent) {
        mCurrentProgress = percent;
        autoInvalidate();
    }

    /**
     * @param isScan
     */
    public void setScan(boolean isScan) {
        this.isScan = isScan;
        autoInvalidate();
    }

    private void autoInvalidate() {
        synchronized (this) {
            if (Thread.currentThread() != uiThread) {
                postInvalidate();
            } else {
                invalidate();
            }
        }
    }
}
