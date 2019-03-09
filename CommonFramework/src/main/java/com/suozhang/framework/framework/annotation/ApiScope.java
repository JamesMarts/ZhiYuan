/*
 * Copyright (c) 2017. 深圳掌控网络有限公司. All rights reserved.
 */

package com.suozhang.framework.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *  Dagger2 作用域注解 Api
 * @author Moodd(420410175[at]qq.com)
 * @date 2017/3/15 17:58
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface ApiScope {

}
