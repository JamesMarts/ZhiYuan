package com.suozhang.framework.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.DividerItemDecoration;

import com.suozhang.framework.R;


/**
 * RecyclerView 分隔线助手，获取分隔线，可指定颜色，宽度/高度
 *
 * @author moodd
 * @email 420410175@qq.com
 * @date 2018/1/26 14:03
 */

public class DividerHelper {
    /**
     * 默认高度/宽度1px
     */
    private static final int DEFAULT_SIZE = 1;

    /**
     * 默认分隔线颜色
     */
    private static int DEFAULT_COLOR = -1;

    /**
     * 获取分隔线
     * <p>
     * 默认：垂直方向，高度1px,颜色主要分隔线颜色（R.color.div_primary）
     *
     * @param context
     * @return
     */
    public static DividerItemDecoration get(Context context) {
        return get(context, DividerItemDecoration.VERTICAL, getDefaultColor(context), DEFAULT_SIZE);
    }

    /**
     * 获取分隔线，可指定颜色
     * <p>
     * 默认：垂直方向，高度1px
     *
     * @param context
     * @param color   颜色
     * @return
     */
    public static DividerItemDecoration get(Context context, @ColorInt int color) {
        return get(context, DividerItemDecoration.VERTICAL, color, DEFAULT_SIZE);
    }

    /**
     * 获取分隔线，可指定颜色，高度/宽度
     * <p>
     * 默认：垂直方向
     *
     * @param context
     * @param color   颜色
     * @param size    高度/宽度
     * @return
     */
    public static DividerItemDecoration get(Context context, @ColorInt int color, int size) {
        return get(context, DividerItemDecoration.VERTICAL, color, size);
    }

    /**
     * 获取分隔线
     *
     * @param context
     * @param orientation 方向
     * @param color       颜色
     * @param size        高度/宽度
     * @return
     */
    public static DividerItemDecoration get(Context context, int orientation, @ColorInt int color, int size) {
        DividerItemDecoration decoration = new DividerItemDecoration(context, orientation);

        GradientDrawable divider = new GradientDrawable();
        divider.setColor(color);
        if (orientation == DividerItemDecoration.VERTICAL) {
            divider.setSize(-1, size);
        } else {
            divider.setSize(size, -1);
        }

        decoration.setDrawable(divider);
        return decoration;
    }

    /**
     * 获取默认分隔线颜色并缓存
     *
     * @param context
     * @return
     */
    @ColorInt
    private static int getDefaultColor(Context context) {
        if (DEFAULT_COLOR == -1) {
            DEFAULT_COLOR = context.getResources().getColor(R.color.div_primary);
        }
        return DEFAULT_COLOR;
    }
}
