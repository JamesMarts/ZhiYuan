/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.yiqi.zhiyuan.framework;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 *
 * 自定义Application.
 *
 * 注意：这个类集成TinkerApplication类，这里面不做任何操作，所有Application的代码都会放到ApplicationLike继承类当中<br/>
 * <pre>
 * 参数解析：
 * 参数1：int tinkerFlags 表示Tinker支持的类型 dex only、library only or all suuport，default: TINKER_ENABLE_ALL
 * 参数2：String delegateClassName Application代理类 这里填写你自定义的ApplicationLike
 * 参数3：String loaderClassName  Tinker的加载器，使用默认即可
 * 参数4：boolean tinkerLoadVerifyFlag  加载dex或者lib是否验证md5，默认为false
 *
 * @author Moodd(420410175@qq.com)
 * @date 2017/3/24 15:36
 */

public class ZYApplication extends TinkerApplication {

    public ZYApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.yiqi.zhiyuan.framework.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
