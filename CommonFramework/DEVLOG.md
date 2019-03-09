


2018.2.26 ------------------版本1.0.0-Alpha
1.升级编译工具Gradle4.4及SDK版本到27
2.升级图片选择库到2.1.9
3.升级Glide到4.5.0
4.启用AAPT2，编译缓存目录修改为默认设置


2018.1.22 ------------------版本1.0.0-Alpha
1.升级部分三方依赖库版本，去除部分不使用的依赖；
2.修复http请求401自动刷新token的授权参数错误的问题，由Authorization变更为zkAuthorization，保存与请求时一直；


2018.1.8 ------------------版本1.0.0-Alpha
1.修改BaseActivity及BaseFragment中bindToLife()方法实现：
由this.bindUntilEvent(ActivityEvent.DESTROY)修改为this.bindToLifecycle()，
即原来由指定在onDestroy中解除订阅修改为默认自动根据生命周期处理解绑，
解决Fragment中ButterKnife Unbinder.unbind()时，空指定问题，调用者根据情况可重新相关方法



2017.12.27 ------------------版本1.0.0-Alpha
1.去掉位置服务开启校验，解决未手动开启“位置服务”或部分机型获取不到状态时，权限校验失败导致蓝牙操作直接提示失败的问题


2017.8.9 ------------------版本1.0.0-Alpha
1.修改http请求头部参数“Authorization”为“zkAuthorization”


2017.8.8 ------------------版本1.0.0-Alpha
1.App升级组件增加酒店id参数，酒店用户端app升级时需要传入此参数


2017.4.14 ------------------版本1.0.0-Alpha
1.优化蓝牙BLE组件，发送数据部分，不抛出异常终止，采用take(2),只发送2次

2017.4.19 ------------------版本1.0.0-Alpha
1.增加RecyclerView关键字搜索显示的Adapter KeywrodAdapter
2.增加RecyclerView 分割线功能 ，支持设置颜色及大小

2017.4.21 ------------------版本1.0.0-Alpha
1.修复网络请求dialog dismiss时取消所有请求的BUG
2.增加错误码列表
3.BaseView中增加显示错误消息的方法：showErrorMsg(CharSequence msg)，
由BaseActivity,BaseFragment空实现，子类可覆盖，自己实现

2017.5.9 ------------------版本1.0.0-Alpha
1.BaseView增加showErrorMsg(int resId) 方法，由BaseActivity及BaseFragment实现，子类根据需要复写即可

   