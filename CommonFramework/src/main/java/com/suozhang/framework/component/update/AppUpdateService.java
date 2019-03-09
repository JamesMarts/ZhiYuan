/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.update;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.suozhang.framework.utils.AppUtil;
import com.suozhang.framework.utils.SPUtil;
import com.suozhang.framework.utils.T;
import com.suozhang.framework.utils.logger.Logger;

import java.io.File;


/**
 * 项目名称：NewSuoZhang
 * 类描述：
 * 创建人：Moodd
 * 创建时间：2016/9/22 11:04
 * 修改人：Moodd
 * 修改时间：2016/12/30 10:06
 * 修改备注：
 * <p>
 * 调用应用更新有2种方式：1.服务启动后，调用AppUpdateManager.checkUpdate(),2.启动服务时，带入参数
 * 请使用 AppUpdateManager中的方法调用更新，服务启动方式只在初始化时使用
 * <p>
 * <p>
 * 启动服务时更新方式：
 * <p>
 * Intent intent = new Intent(context, AppUpdateService.class);
 * intent.putExtra(AppUpdateService.IS_CHECK_UPDATE,true);
 * intent.putExtra(AppUpdateService.IS_AUTO_UPDATE,false);
 * startService(intent);
 */
public class AppUpdateService extends Service {

    //下载
    public static final String ACTION_DOWNLOAD = "com.suozhang.ACTION_DOWNLOAD";

    //第一次启动，是否检查更新
    public static final String IS_CHECK_UPDATE = "com.suozhang.IS_CHECK_UPDATE";
    //第一次启动，WIFI网络下是否自动更新
    public static final String IS_AUTO_UPDATE = "com.suozhang.IS_AUTO_UPDATE";

    //升级文件保存子文件夹
    private static final String DOWNLOAD_SUB_PATH = "/apk";
    //升级文件保存文件夹及文件名称
    private static final String DOWNLOAD_FILE = DOWNLOAD_SUB_PATH + "/suozhang.apk";

    //下载管理器
    private DownloadManager mDownloadManager;
    //下载ID
    private long mDownloadId;

    /**
     * 广播接受器，下载完成或点击通知
     */
    public BroadcastReceiver mDownloadBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.i("接受到下载广播--------------->>>" + intent);
            if (TextUtils.equals(action, DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                //下载完成时，发送的广播
                if (mDownloadId == downloadId) {
                    try {
                        installDownload(downloadId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (TextUtils.equals(action, DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
                //Notification被点击时发送的广播
                long[] downloadIds = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
                if (downloadIds == null) return;
                for (long downloadId : downloadIds) {
                    if (mDownloadId == downloadId) {
                        try {
                            installDownload(downloadId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            }
        }
    };

    /**
     * 安装下载的文件
     *
     * @param downloadId
     * @throws Exception
     */
    private void installDownload(long downloadId) throws Exception {
        int status = getDownloadStatus(downloadId);
        if (status == DownloadManager.STATUS_SUCCESSFUL) {
            try {
                Uri uri = mDownloadManager.getUriForDownloadedFile(downloadId);
                install(uri);
            } catch (Exception e) {
                Logger.e(e, e.getMessage());
            }
        }
    }

    /**
     * 广播接受器，接受检查更新，文件下载等Action
     */
    public BroadcastReceiver mUpdateBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.i("接受到应用更新广播--------------->>>" + action);
            if (ACTION_DOWNLOAD.equals(action)) {
                //下载
                String url = intent.getStringExtra(ACTION_DOWNLOAD);
                if (TextUtils.isEmpty(url)) return;

                //检查缓存，取消之前的下载
                checkDownloadCache();

                //开始新的下载
                try {
                    download(url);
                } catch (Exception e) {
                    T.showShortAloneToast("文件下载出错");
                    Logger.i("文件下载出错--->", e);
                }
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i("应用更新服务启动--------onCreate------->>>");
        //初始化下载管理
        initDownloadManager();

        //注册应用更新广播，在本应用中有效
        LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateBroadcast, new IntentFilter(ACTION_DOWNLOAD));

        //注册下载广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);//下载完成时，发送的广播
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);//Notification被点击时发送的广播
        registerReceiver(mDownloadBroadcast, filter);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //第一次启动，初始化检查更新
        checkUpdateWithFirst(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 第一次启动，初始化检查更新
     *
     * @param intent
     */
    private void checkUpdateWithFirst(Intent intent) {
        boolean isCheckUpdate = false;
        boolean isAutoUpdate = false;

        if (intent != null) {
            isCheckUpdate = intent.getBooleanExtra(IS_CHECK_UPDATE, false);
            isAutoUpdate = intent.getBooleanExtra(IS_AUTO_UPDATE, false);
        }
        Logger.i("以启动服务的方式，检查更新------->isCheckUpdate = " + isCheckUpdate + " isAutoUpdate = " + isAutoUpdate);
        //第一次启动，自动检查更新
        if (isCheckUpdate) {
            AppUpdateManager.getInstance().checkUpdateFromServer(getApplicationContext(), isAutoUpdate);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销应用更新广播
        if (mUpdateBroadcast != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpdateBroadcast);
        }
        //注销取消下载广播
        if (mDownloadBroadcast != null) {
            unregisterReceiver(mDownloadBroadcast);
        }

        //检查缓存，取消之前的下载
        checkDownloadCache();
    }

    /**
     * 初始化下载管理
     */
    private void initDownloadManager() {
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
    }

    /**
     * 检查缓存，取消之前的下载
     */
    private void checkDownloadCache() {
        try {
            long cacheId = getDownloadIdCache();
            if (cacheId == mDownloadId) {
                cancel(mDownloadId);
            } else {
                cancel(cacheId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //删除缓存文件和文件夹
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), DOWNLOAD_SUB_PATH);
            deleteFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 取消下载，当有正在下载或已存在下载文件均会被删除
     *
     * @param downloadId
     * @return
     * @throws Exception
     */
    private boolean cancel(long downloadId) throws Exception {

        return mDownloadManager.remove(downloadId) > 0;
    }

    /**
     * 下载
     *
     * @param apkUrl
     * @throws Exception
     */
    private void download(String apkUrl) throws Exception {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, DOWNLOAD_FILE);
        //Notification标题
        String appName = AppUtil.getAppName(this);
        appName = TextUtils.isEmpty(appName) ? "文件下载" : appName;
        request.setTitle(appName);
        //Notification描述
        request.setDescription("正在下载升级文件...");
        //在通知栏中显示
        request.setVisibleInDownloadsUi(true);
        //Notification显示，下载进行时，和完成之后都会显示
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //设置下载文件类型 设置MimeType，会覆盖服务器返回的已声明的content type
        //这是安卓.apk文件的类型。有些机型必须设置此方法，才能在下载完成后，点击通知栏的Notification时，才能正确的打开安装界面。
        // 不然会弹出一个Toast（can not open file）.
        request.setMimeType("application/vnd.android.package-archive");

        mDownloadId = mDownloadManager.enqueue(request);
        //保存id到本地，下载之前删除旧文件及下载数据
        saveDownloadIdCache(mDownloadId);

    }

    /**
     * 保存下载Id缓存
     *
     * @param downloadId
     */
    private void saveDownloadIdCache(long downloadId) {
        try {
            SPUtil.put(this, "downloadCache", "downloadId", downloadId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取下载Id缓存
     *
     * @return
     */
    private long getDownloadIdCache() {
        long downloadId = 0;
        try {
            downloadId = (long) SPUtil.get(this, "downloadCache", "downloadId", 0L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return downloadId;
    }

    /**
     * 安装
     * NOTE: 2017/7/18 如果uri不是.apk结尾，则使用指定文件安装
     *
     * @param uri
     * @throws Exception
     */
    private void install(Uri uri) throws Exception {
        String path = uri.getPath();

        // NOTE: 2017/7/18 如果uri不是.apk结尾，则使用指定文件安装
        if (!path.endsWith(".apk")) {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), DOWNLOAD_FILE);
            uri = Uri.fromFile(file);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // NOTE: 2017/7/19 解决7.0以上系统权限错误android.os.FileUriExposedException
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".basefileprovider", new File(uri.getPath()));
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /**
     * 获取下载状态
     *
     * @param downloadId
     * @return
     */
    public int getDownloadStatus(long downloadId) {
        int status = -1;

        Cursor c = null;
        try {
            DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
            c = mDownloadManager.query(query);
            if (c.moveToFirst()) {
                status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (c != null) c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return status;
    }


    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    private void deleteFile(File file) throws Exception {
        if (!file.exists()) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }


}
