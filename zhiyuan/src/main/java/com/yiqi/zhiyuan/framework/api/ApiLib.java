package com.yiqi.zhiyuan.framework.api;


import com.suozhang.framework.component.app.AppComponent;
import com.suozhang.framework.component.app.DaggerAppComponent;
import com.suozhang.framework.component.http.Host;
import com.suozhang.framework.framework.AM;
import com.yiqi.zhiyuan.framework.api.dependencies.ApiComponent;
import com.yiqi.zhiyuan.framework.api.dependencies.DaggerApiComponent;


public class ApiLib {
    private static ApiComponent apiComponent;

    /**
     * 初始化ApiComponent
     */
    public static void initApiComponent() {
        apiComponent = DaggerApiComponent.builder().build();
    }

    /**
     * 重新设置api地址
     * 调用完AM.init()方法后，才可调用此方法
     *
     * @param host
     */
    public static void resetHost(Host host) {

        AM.buildComponent(AM.app());
        initApiComponent();
        AM.api().setHost(host);
    }

    /**
     * 向外提供apiComponent,方便其他依赖apiComponent的component构建
     *
     * @return
     */
    public static ApiComponent apiComponent() {
        return apiComponent;
    }

    /**
     * 向外提供appComponent,方便其他依赖appComponent的component构建
     *
     * @return
     */
    public static AppComponent appComponent() {
        return AM.appComponent();
    }
}
