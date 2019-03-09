/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.utils.logger;

public interface LogAdapter {
  void d(String tag, String message);

  void e(String tag, String message);

  void w(String tag, String message);

  void i(String tag, String message);

  void v(String tag, String message);

  void wtf(String tag, String message);
}