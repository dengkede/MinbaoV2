package com.hlkj.minbao.presenter;


import android.content.Context;

import com.google.gson.Gson;
import com.hlkj.minbao.entity.LoginUserInfo;
import com.hlkj.minbao.util.AppConfig;
import com.hlkj.minbao.util.AppServerManager;
import com.hlkj.minbao.view.ITestView;
import com.wxh.common4mvp.base.DownLoadHttpCallBack;
import com.wxh.common4mvp.base.NormalHttpCallback;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;
import com.wxh.common4mvp.util.LogUtils;
import com.wxh.common4mvp.util.SPUtils;
import com.wxh.common4mvp.util.StringUtils;
import com.wxh.common4mvp.util.SystemUtils;
import com.wxh.common4mvp.util.ToastUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试Presenter，负责测试界面的业务逻辑
 * 测试了普通get请求，下载文件请求，postString请求
 * 通过持有view的弱引用，在业务逻辑处理过程的适当地方调用view的渲染UI的方法
 * 具体的view渲染UI方法的实现在view层实现（activity或者fragment）
 */
public class TestPresenter extends IBasePresenterImpl<ITestView> {

    public static final int REQUEST_POST_LOGIN = 1;
    public static final int REQUEST_GET_INFO = 2;
    public static final int REQUEST_DOWNLOAD_USER_PHOTO = 3;

    public TestPresenter(Context context, String activityName, ITestView view) {
        super(context, activityName, view);
    }

    /**
     * 账户登录
     *
     * @param whichRequest
     */
    public void actionLogin(final int whichRequest) {
        if (isNetworkAvailable()) {
            try {
                JSONObject params = new JSONObject();
//                params.put("username", "11111111111");
//                params.put("password", "123456");
                params.put("username", "15325051111");
                params.put("password", "123456");
                AppServerManager.HttpPostString(
                        AppConfig.URL_POST_USER_LOGIN,
                        null,
                        params.toString(),
                        new NormalHttpCallback(mActivityName, mContext) {
                            @Override
                            public void onHttpStart() {
                                mViewRef.get().onServerStart(whichRequest);
                            }

                            @Override
                            public void onHttpSuccess(String resultStr) {
                                ToastUtils.showToast(resultStr);
                                LogUtils.v("111111：" + resultStr);
                                if (resultStr != null) {
                                    LoginUserInfo userInfo = new Gson().fromJson(resultStr, LoginUserInfo.class);
                                    if (userInfo != null) {
                                        SPUtils.getInstance().saveObject(userInfo, AppConfig.CACHE_USERINFO);
                                    }
                                }

                                mViewRef.get().onServerSuccess(whichRequest, resultStr);
                            }

                            @Override
                            public void onHttpFail(int errorCode, String errorMsg) {
                                ToastUtils.showToast(errorMsg);
                                LogUtils.v("222222:" + errorMsg);
                                mViewRef.get().onServerError(whichRequest, errorCode, errorMsg);
                            }

                            @Override
                            public void onHttpProgress(int progress, long total) {
                                mViewRef.get().onServerProcess(whichRequest, progress, total);
                            }
                        }

                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取用户信息
     *
     * @param whichRequest
     */
    public void actionGetInfo(final int whichRequest) {
        if (isNetworkAvailable()) {
            LoginUserInfo userInfo = (LoginUserInfo) SPUtils.getInstance().readObject(AppConfig.CACHE_USERINFO);
            if (userInfo == null)
                return;

            String userId = userInfo.getUserId();
            Map<String, String> paths = new HashMap<>();
            paths.put("userId", userId);

            AppServerManager.HttpGet(
                    AppConfig.URL_GET_USER_PHOTO,
                    paths,
                    null,
                    new NormalHttpCallback(mActivityName, mContext) {
                        @Override
                        public void onHttpStart() {
                            mViewRef.get().onServerStart(whichRequest);
                        }

                        @Override
                        public void onHttpSuccess(String resultStr) {
                            ToastUtils.showToast(resultStr);
                            LogUtils.v("111111：" + resultStr);
                            mViewRef.get().onServerSuccess(whichRequest, resultStr);
                            //下载用户照片
                            actionDownUserPhoto(REQUEST_DOWNLOAD_USER_PHOTO, resultStr);
                        }

                        @Override
                        public void onHttpFail(int errorCode, String errorMsg) {
                            ToastUtils.showToast(errorMsg);
                            LogUtils.v("222222:" + errorMsg);
                            mViewRef.get().onServerError(whichRequest, errorCode, errorMsg);
                        }

                        @Override
                        public void onHttpProgress(int progress, long total) {
                            mViewRef.get().onServerProcess(whichRequest, progress, total);
                        }
                    });
        }
    }

    /**
     * 下载用户照片
     *
     * @param whichRequest
     * @param resultStr
     */
    private void actionDownUserPhoto(final int whichRequest, String resultStr) {
        if (isNetworkAvailable()) {
            if (StringUtils.isEmpty(resultStr))
                return;

            String filePath = SystemUtils.getAppFilePath(mContext, AppConfig.FOLDER_IMAGE);
            String fileName = "test.jpg";

            AppServerManager.HttpDownLoad(
                    resultStr,
                    filePath,
                    fileName,
                    new DownLoadHttpCallBack(mActivityName, mContext, false) {
                        @Override
                        public void onDownLoadStart() {
                            mViewRef.get().onServerStart(whichRequest);
                        }

                        @Override
                        public void onDownLoadSuccess(File file) {
                            ToastUtils.showToast("下载成功");
                            LogUtils.v("333333:" + file.getAbsolutePath());
                            mViewRef.get().onServerSuccess(whichRequest, file.getAbsolutePath());
                        }

                        @Override
                        public void onDownLoadFail(Throwable throwable) {
                            ToastUtils.showToast("下载失败");
                            LogUtils.v("444444:" + throwable.getMessage());
                            mViewRef.get().onServerError(whichRequest, -1, throwable.getMessage());
                        }

                        @Override
                        public void onDownLoadProgress(int progress, long total) {
                            LogUtils.v("555555——progress:" + progress + " total:" + total);
                            mViewRef.get().onServerProcess(whichRequest, progress, total);
                        }
                    }
            );
        }
    }
}
