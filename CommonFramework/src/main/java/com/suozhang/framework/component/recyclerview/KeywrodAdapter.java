/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.component.recyclerview;

import android.support.annotation.ColorInt;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suozhang.framework.framework.annotation.Keyword;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Moodd
 * @email 420410175@qq.com
 * @date 2017/4/19 16:08
 */

public abstract class KeywrodAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    protected String mKeyword = "";

    public KeywrodAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
        defSort();
    }

    public KeywrodAdapter(List<T> data) {
        super(data);
        defSort();
    }

    public KeywrodAdapter(int layoutResId) {
        super(layoutResId);
        defSort();
    }

    /**
     * 设置默认排序规则
     *
     * @param defComparator
     */
    public void setDefComparator(Comparator<T> defComparator) {
        this.defComparator = defComparator;
    }

    /**
     * 设置关键字
     *
     * @param keyword
     */
    public void setKeyword(String keyword) {
        this.setKeyword(keyword, null);
    }

    /**
     * 设置关键字
     *
     * @param keyword
     */
    public void setKeyword(String keyword, String emptyMsg) {
        if (mData == null || mData.isEmpty()) {
            return;
        }
        if (!isContainKeyword(keyword, mData)) {
            if (!TextUtils.isEmpty(keyword) && !TextUtils.isEmpty(emptyMsg)) {
                com.suozhang.framework.utils.T.showShort(emptyMsg);
            }
            keyword = "";
        }
        this.mKeyword = keyword;
        sort(mKeyword);
        notifyDataSetChanged();
    }

    private boolean isContainKeyword(String keyword, List<T> datas) {
        boolean isContain = false;
        if (datas == null) {
            return isContain;
        }
        for (T data : datas) {
            if (data == null) continue;
            String value = getFieldValue(data);
            if (value != null && value.contains(keyword)) {
                isContain = true;
                break;
            }
        }
        return isContain;
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
     * 默认排序 名称
     */
    private void defSort() {
        if (super.mData == null) {
            return;
        }
        //默认排序 名称
        Collections.sort(super.mData, defComparator);
    }

    /**
     * 排序-按关键字出现的位置进行排序，为null,默认排序
     */
    private void sort(final String keyword) {
        if (super.mData == null) {
            return;
        }
        if (TextUtils.isEmpty(keyword)) {
            //默认排序 名称
            defSort();
            return;
        }

        Collections.sort(super.mData, keywordComparator);//按关键字出现的位置排序
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
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        name = TextUtils.isEmpty(name) ? "" : name;
        return name;
    }

    Comparator<T> defComparator = new Comparator<T>() {
        @Override
        public int compare(T o1, T o2) {

            return getFieldValue(o1).compareTo(getFieldValue(o2));
        }
    };
    Comparator<T> keywordComparator = new Comparator<T>() {
        @Override
        public int compare(T lhs, T rhs) {

            int indexL = getFieldValue(lhs).indexOf(mKeyword);
            int indexR = getFieldValue(rhs).indexOf(mKeyword);

            if (indexL != -1 && indexR != -1) return indexL - indexR;
            return indexR - indexL;
        }
    };

}
