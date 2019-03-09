/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.component.uesr;

import com.suozhang.framework.entity.bo.LoginData;
import com.suozhang.framework.entity.bo.LoginOrgData;
import com.suozhang.framework.entity.bo.UserAccount;
import com.suozhang.framework.entity.bo.TokenInfo;
import com.suozhang.framework.entity.bo.LoginUserData;
import com.suozhang.framework.framework.AM;
import com.suozhang.framework.utils.SPUtil;

import io.reactivex.Observable;
import io.reactivex.functions.Function;


/**
 * Created by Moodd on 2017/2/15.
 */
public class UserController {
    /**
     * 保存用户信息时使用的SharedPreferences文件名
     */
    private static final String FILE_NAME = "szuserController";

    // 登录结果信息,用于在SharedPreferences中保存或获取用户信息的key
    private static final String LOGIN_INFO = "LOGIN_INFO";

    // 用户登录的用户名等信息,用于在SharedPreferences中保存或获取用户信息的key
    private static final String LOGIN_USER = "LOGIN_USER";

    private static final String IS_REMEMBER_PASSWORD = "IS_REMEMBER_PASSWORD";

    private static final Object lock = new Object();


    /**
     * 用户配置是否允许锁屏显示的key
     */
    public static final String CONFIG_KEY_ALLOW_LOCK_SCREEN = "CONFIG_ALLOW_LOCK_SCREEN";
    private TokenInfo mTokenInfo;

    private static UserController instance;

    private UserController() {
    }

    public static UserController getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new UserController();
                }
            }
        }
        return instance;
    }

    /**
     * 从本地获取保存的“记住密码”状态
     *
     * @return
     */
    public boolean isRememberPassword() {
        boolean isRememberPassword = false;
        try {
            isRememberPassword = (boolean) SPUtil.get(AM.app(), FILE_NAME, IS_REMEMBER_PASSWORD, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isRememberPassword;
    }

    /**
     * 设置记住密码的状态-保存到本地
     *
     * @param isRememberPassword
     */
    public void setRememberPassword(boolean isRememberPassword) {
        SPUtil.put(AM.app(), FILE_NAME, IS_REMEMBER_PASSWORD, isRememberPassword);
    }

    /**
     * 保存用户帐号信息
     *
     * @param info
     */
    public void saveUserAccountCache(UserAccount info) {

        if (info != null) {
            SPUtil.saveObj(AM.app(), FILE_NAME, LOGIN_USER, info);
        } else {
            SPUtil.clearByKey(AM.app(), FILE_NAME, LOGIN_USER);
        }
    }

    /**
     * 获取保存的用户帐号信息
     *
     * @return
     */
    public UserAccount getUserAccountCache() {
        UserAccount user = null;
        try {
            user = (UserAccount) SPUtil.readObj(AM.app(), FILE_NAME, LOGIN_USER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 保存登录结果
     *
     * @param info
     */
    public void saveLoginResultCache(TokenInfo info) {
        //保存到内存
        mTokenInfo = info;
        //保存到本地
        if (info != null) {
            SPUtil.saveObj(AM.app(), FILE_NAME, LOGIN_INFO, info);
        } else {
            SPUtil.clearByKey(AM.app(), FILE_NAME, LOGIN_INFO);
        }
    }

    /**
     * 获取登录结果，首先从内存中获取，内存中没有从磁盘缓存中取
     *
     * @return
     */
    public TokenInfo getLoginResultCache() {

        if (mTokenInfo == null) {
            try {
                mTokenInfo = (TokenInfo) SPUtil.readObj(AM.app(), FILE_NAME, LOGIN_INFO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mTokenInfo;
    }

    /**
     * 从登录结果中获取用户信息
     *
     * @return
     */
    public LoginUserData getLoginUserData() {
        TokenInfo tokenInfo = getLoginResultCache();
        if (tokenInfo == null) {
            return null;
        }

        try {
            return Observable.just(tokenInfo).map(new Function<TokenInfo, LoginData>() {
                @Override
                public LoginData apply(TokenInfo tokenInfo) throws Exception {
                    return tokenInfo.getData();
                }
            }).map(new Function<LoginData, LoginUserData>() {
                @Override
                public LoginUserData apply(LoginData loginData) throws Exception {
                    return loginData.getUser();
                }
            }).blockingSingle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从登录结果中获取机构信息-酒店、经销商
     *
     * @return
     */
    public LoginOrgData getLoginOrgData() {
        TokenInfo tokenInfo = getLoginResultCache();
        if (tokenInfo == null) {
            return null;
        }
        try {
            return Observable.just(tokenInfo).map(new Function<TokenInfo, LoginData>() {
                @Override
                public LoginData apply(TokenInfo tokenInfo) throws Exception {
                    return tokenInfo.getData();
                }
            }).map(new Function<LoginData, LoginOrgData>() {
                @Override
                public LoginOrgData apply(LoginData loginData) throws Exception {
                    return loginData.getOrg();
                }
            }).blockingSingle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从登录结果中获取其他信息--经销商APP模块列表
     *
     * @return
     */
    public String getLoginOtherData() {
        TokenInfo tokenInfo = getLoginResultCache();
        if (tokenInfo == null) {
            return null;
        }
        try {
            return Observable.just(tokenInfo).map(new Function<TokenInfo, LoginData>() {
                @Override
                public LoginData apply(TokenInfo tokenInfo) throws Exception {
                    return tokenInfo.getData();
                }
            }).map(new Function<LoginData, String>() {
                @Override
                public String apply(LoginData loginData) throws Exception {
                    return (loginData.getData());
                }
            }).blockingSingle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取Token
     *
     * @return
     */
    public String getToken() {
        TokenInfo info = getLoginResultCache();
        if (info != null) {
            return info.getAccessToken();
        }
        return null;
    }

    /**
     * 获取刷新Token
     *
     * @return
     */
    public String getRefreshToken() {
        TokenInfo info = getLoginResultCache();
        if (info != null) {
            return info.getRefreshToken();
        }
        return null;
    }

    /**
     * 刷新Token后保存新登录信息
     *
     * @param newInfo
     */
    public void refreshToken(TokenInfo newInfo) {
        saveLoginResultCache(newInfo);
    }


    /**
     * 是否登录
     *
     * @return true:已登录 false:未登录
     */
    public boolean isLogin() {
        return getLoginResultCache() != null;
    }

    /**
     * 退出登录 ，清空登录结果，及登录保存的用户名密码等
     */
    public void loginOut() {
        //清空登录结果，token信息等
        saveLoginResultCache(null);

        //如果不是记住密码-就清空用户的登录信息
        if (!isRememberPassword()) {
            saveUserAccountCache(null);
        }
    }
    /**
     * 是否允许锁屏显示
     *
     * @return
     */
    public  boolean isAllowLockScreen() {
        try {
            return (boolean) SPUtil.get(AM.app(), CONFIG_KEY_ALLOW_LOCK_SCREEN, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public  void setAllowLockScreen(boolean isAllowLockScreen) {
        SPUtil.put(AM.app(), CONFIG_KEY_ALLOW_LOCK_SCREEN, isAllowLockScreen);
    }
}
