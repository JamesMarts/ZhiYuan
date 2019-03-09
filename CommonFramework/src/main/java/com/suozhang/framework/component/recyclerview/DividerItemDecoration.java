/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.component.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView的ItemDecoration的默认实现
 * 1. 默认使用系统的分割线
 * 2. 支持自定义Drawable类型
 * 3. 支持水平和垂直方向
 * 4. 修复了官方垂直Divider显示的bug
 * 扩展自官方android sdk下的Support7Demos下的DividerItemDecoration
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL = LinearLayoutManager.VERTICAL;

    @ColorInt
    private static final int defColor = 0xffc3c3c3;//默认颜色 灰色
    private static final int defLineWidth = 1;//默认线宽 1px

    private Drawable mDivider;
    private int mWidth;
    private int mHeight;

    private int mOrientation;

    private DividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }


    /**
     * 新增：默认颜色默认线宽
     *
     * @param orientation
     */
    public DividerItemDecoration(int orientation) {

        this(orientation, defColor, defLineWidth);
    }

    /**
     * 新增：默认垂直布局，支持自定义颜色
     *
     * @param
     */
    public DividerItemDecoration(@ColorInt int color, int width) {

        this(VERTICAL, color, width);
    }

    /**
     * 新增：支持自定义颜色
     *
     * @param orientation
     * @param color
     * @param width
     */
    public DividerItemDecoration(int orientation, @ColorInt int color, int width) {
        this(orientation, new ColorDrawable(color));
        if (orientation == HORIZONTAL) {
            setWidth(width);
        } else {
            setHeight(width);
        }

    }

    /**
     * 新增：支持自定义dividerDrawable
     *
     * @param orientation
     * @param dividerDrawable
     */
    public DividerItemDecoration(int orientation, Drawable dividerDrawable) {
        mDivider = dividerDrawable;
        setOrientation(orientation);
    }

    public DividerItemDecoration setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
        return this;
    }

    /**
     * 新增：支持手动为无高宽的drawable制定宽度
     *
     * @param width
     */
    public DividerItemDecoration setWidth(int width) {
        this.mWidth = width;
        return this;
    }

    /**
     * 新增：支持手动为无高宽的drawable制定高度
     *
     * @param height
     */
    public DividerItemDecoration setHeight(int height) {
        this.mHeight = height;
        return this;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + getDividerHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin +
                    Math.round(ViewCompat.getTranslationX(child));
            final int right = left + getDividerWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (mOrientation == VERTICAL) {
            outRect.set(0, 0, 0, getDividerHeight());
        } else {
            outRect.set(0, 0, getDividerWidth(), 0);
        }
    }

    private int getDividerWidth() {
        return mWidth > 0 ? mWidth : mDivider.getIntrinsicWidth();
    }

    private int getDividerHeight() {
        return mHeight > 0 ? mHeight : mDivider.getIntrinsicHeight();
    }

}