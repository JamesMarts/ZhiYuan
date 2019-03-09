/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.http.converter;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 描述
 *
 * @author Moodd(420410175@qq.com)
 * @date 2017/3/10
 */
final class FastJsonResponseBodyConvert<T> implements Converter<ResponseBody, T> {

    private Type type;

    public FastJsonResponseBodyConvert(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        return JSON.parseObject(value.string(), type);
    }
}
