/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.entity.bo;

import java.io.Serializable;

/**
 * 实体基类，主要实现序列化接口及克隆接口
 * 如需使用比较接口，请单独实现Comparable
 * <p>
 * Created by Moodd on 2017/2/10.
 */

public interface BaseEntity extends Serializable, Cloneable {
    /* no-op */

}
