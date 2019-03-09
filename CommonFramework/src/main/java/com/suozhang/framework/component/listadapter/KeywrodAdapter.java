/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.listadapter;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.suozhang.framework.framework.annotation.Keyword;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 关键字搜索功能的Adapter
 * 参数T 需要实现Comparable接口，关键字Field 需要添加注解：@SortKeyword
 *
 * @param <T>
 */
public abstract class KeywrodAdapter<T> extends CommonAdapter<T> {
    protected String mKeyword = "";

    private Comparator<T> mDefaultComparator = new Comparator<T>() {
        @Override
        public int compare(T o1, T o2) {
            return 0;
        }
    };
    private Comparator<T> mKeywordComparator = new Comparator<T>() {

        @Override
        public int compare(T lhs, T rhs) {

            int indexL = getFieldValue(lhs).indexOf(mKeyword == null ? "" : mKeyword);
            int indexR = getFieldValue(rhs).indexOf(mKeyword == null ? "" : mKeyword);

            if (indexL != -1 && indexR != -1) {
                return indexL - indexR;
            }
            return indexR - indexL;
        }
    };

    public KeywrodAdapter(Context context, List<T> datas, int layoutId) {
        super(context, datas, layoutId);
        if (super.mDatas != null) Collections.sort(super.mDatas, mDefaultComparator);//默认排序
    }

    /**
     * 设置关键字
     *
     * @param keyword
     */
    public void setKeyword(String keyword) {
        this.mKeyword = keyword;
        sort(mKeyword);
        notifyDataSetChanged();
    }

    public abstract void convert(ViewHolder holder, T t);

    /**
     * 获取高亮关键字后的字符串
     *
     * @param def
     * @return
     */
    protected CharSequence getKeyword(String def) {

        return getKeyword(mKeyword, def, 0xfffbd88c);
    }

    /**
     * 获取高亮关键字后的字符串
     *
     * @param def
     * @param keywordColor 关键字颜色
     * @return
     */
    protected CharSequence getKeyword(String def, @ColorInt int keywordColor) {

        return getKeyword(mKeyword, def, keywordColor);
    }

    /**
     * 获取高亮关键字后的字符串
     *
     * @param keyword
     * @param def
     * @return
     */
    protected CharSequence getKeyword(String keyword, String def, @ColorInt int keywordColor) {
        if (TextUtils.isEmpty(keyword)) return def;
        if (!TextUtils.isEmpty(def) && def.contains(keyword)) {
            int index = def.indexOf(keyword);
            int len = index + keyword.length();

            SpannableString temp = new SpannableString(def);//super.mContext.getColor(R.color.colorKeyword)
            temp.setSpan(new ForegroundColorSpan(keywordColor),
                    index, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return temp;
        }
        return def;
    }

    /**
     * 排序-按关键字出现的位置进行排序，为null,默认排序
     */
    private void sort(final String keyword) {
        if (TextUtils.isEmpty(keyword)) {

            Collections.sort(super.mDatas, mDefaultComparator);//默认排序
            return;
        }
        Collections.sort(super.mDatas, mKeywordComparator);//按关键字出现的位置排序
    }

    private String getFieldValue(T t) {
        String name = null;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(Keyword.class) != null) {
                field.setAccessible(true);
                try {
                    name = (String) field.get(t);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return name;
    }

}