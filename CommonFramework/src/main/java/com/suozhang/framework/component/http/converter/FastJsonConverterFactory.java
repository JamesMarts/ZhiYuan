/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.http.converter;


import com.alibaba.fastjson.serializer.SerializeConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * 描述
 *
 * @author Moodd(420410175@qq.com)
 * @date 2017/3/10
 */
public class FastJsonConverterFactory extends Converter.Factory {

    private final SerializeConfig serializeConfig;

    private FastJsonConverterFactory(SerializeConfig serializeConfig) {
        if (serializeConfig == null)
            throw new NullPointerException("serializeConfig == null");
        this.serializeConfig = serializeConfig;
    }

    public static FastJsonConverterFactory create() {
        return create(SerializeConfig.getGlobalInstance());
    }

    public static FastJsonConverterFactory create(SerializeConfig serializeConfig) {
        return new FastJsonConverterFactory(serializeConfig);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new FastJsonRequestBodyConverter<>(serializeConfig);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new FastJsonResponseBodyConvert<>(type);
    }
}
