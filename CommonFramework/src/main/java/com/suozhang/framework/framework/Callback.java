/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.framework;

/**
 * 公共回调接口
 *
 * @author Moodd
 * @e-mail 420410175@qq.com
 * @date 2017/3/27 13:09
 */

public interface Callback {

    /**
     * 公共回调接口
     *
     * @param <ResultType> 返回值类型
     */
    public interface CommonCallback<ResultType> {
        /**
         * 成功
         *
         * @param result 返回值
         */
        void onSuccess(ResultType result);

        /**
         * 错误，失败
         *
         * @param ex 异常信息
         */
        void onError(Throwable ex);
    }
}
