/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.utils.logger;

import android.util.Log;

class AndroidLogAdapter implements LogAdapter {
  @Override public void d(String tag, String message) {
    Log.d(tag, message);
  }

  @Override public void e(String tag, String message) {
    Log.e(tag, message);
  }

  @Override public void w(String tag, String message) {
    Log.w(tag, message);
  }

  @Override public void i(String tag, String message) {
    Log.i(tag, message);
  }

  @Override public void v(String tag, String message) {
    Log.v(tag, message);
  }

  @Override public void wtf(String tag, String message) {
    Log.wtf(tag, message);
  }
}
