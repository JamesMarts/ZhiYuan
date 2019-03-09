/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.utils;

import android.graphics.Paint;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 *
 * Created by Moodd on 2017/3/3.
 */

public class ToolbarUtil {

    /**
     * toolbar标题居中
     *
     * @param toolbar
     */
    public static void setTitleCenter(Toolbar toolbar) {
        int childCount = toolbar.getChildCount();
        int left = toolbar.getContentInsetStartWithNavigation();
        //int left1 = toolbar.getContentInsetLeft();
        int deviceWidth = toolbar.getContext().getResources().getDisplayMetrics().widthPixels;
        String toolbarText = toolbar.getTitle().toString();

        for (int i = 0; i < childCount; i++) {
            View child = toolbar.getChildAt(i);
            if (child instanceof TextView) {
                TextView childTitle = (TextView) child;
                String childText = childTitle.getText().toString();
                if (TextUtils.equals(childText, toolbarText)) {
                    Paint p = childTitle.getPaint();
                    float textWidth = p.measureText(childText);
                    float tx = (deviceWidth - textWidth) / 2.0f - left;
                    childTitle.setTranslationX(tx);
                    break;
                }
            }
        }
    }
}
