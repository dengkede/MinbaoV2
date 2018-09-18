package com.hlkj.minbao.presenter;

import android.app.Activity;
import android.content.Context;

import com.hlkj.minbao.R;
import com.hlkj.minbao.util.AppConfig;
import com.hlkj.minbao.util.AppServerManager;
import com.hlkj.minbao.view.ILoginView;
import com.wxh.common4mvp.base.DownLoadHttpCallBack;
import com.wxh.common4mvp.base.NormalHttpCallback;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;
import com.wxh.common4mvp.util.ActivityManager;
import com.wxh.common4mvp.util.LogUtils;
import com.wxh.common4mvp.util.StringUtils;
import com.wxh.common4mvp.util.SystemUtils;
import com.wxh.common4mvp.util.ToastUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LoginPresenter extends IBasePresenterImpl<ILoginView> {

    public static final int REQUEST_POST_USER_LOGIN = 1;
    public static final int REQUEST_GET_USER_PHOTO = 2;
    public static final int REQUEST_DOWNLOAD_USER_PHOTO = 3;

    private long ExitTime = 0;

    public LoginPresenter(Context context, String activityName, ILoginView view) {
        super(context, activityName, view);
    }

    public void onLoginBackPressed(Activity activity) {
        if ((System.currentTimeMillis() - ExitTime) > 2000) {
            ToastUtils.showToast(R.string.comm_exit_msg);
            ExitTime = System.currentTimeMillis();
        } else {
            ActivityManager.getAppInstance().finishActivity(activity);
        }
    }

    public boolean checkInput(String username, String password, boolean isReadDoc) {
        if (StringUtils.isEmpty(username)) {
            ToastUtils.showToast(R.string.login_error_username_empty);
            return false;
        }
        if (StringUtils.isEmpty(password)) {
            ToastUtils.showToast(R.string.login_error_password_empty);
            return false;
        }
        if (!isReadDoc) {
            ToastUtils.showToast(R.string.login_error_no_read_doc);
            return false;
        }

        return true;
    }

    public void actionLogin(final int whichRequest, String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
            return;
        if (!isNetworkAvailable())
            return;

        try {
            JSONObject params = new JSONObject();
            params.put("username", username);
            params.put("password", password);
            LogUtils.e("actionLogin requestJsonStr:" + params.toString());

            AppServerManager.HttpPostString(
                    AppConfig.URL_POST_USER_LOGIN,
                    null,
                    params.toString(),
                    false,
                    true,
                    new NormalHttpCallback(mActivityName, mContext) {
                        @Override
                        public void onHttpStart() {
                            mViewRef.get().onServerStart(whichRequest);
                        }

                        @Override
                        public void onHttpSuccess(String resultStr) {
                            LogUtils.v("actionLogin responseDataStr：" + resultStr);
                            if (resultStr != null) {
                                mViewRef.get().onServerSuccess(whichRequest, resultStr);
                            }
                        }

                        @Override
                        public void onHttpFail(int errorCode, String errorMsg) {
                            LogUtils.v("actionLogin onHttpFail:" + errorMsg);
                            ToastUtils.showToast(errorMsg);
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

    public void actionGetUserPhoto(final int whichRequest, String userId) {
        if (StringUtils.isEmpty(userId))
            return;
        if (!isNetworkAvailable())
            return;

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
                        LogUtils.v("actionGetUserPhoto responseDataStr：" + resultStr);
                        if (resultStr != null) {
                            mViewRef.get().onServerSuccess(whichRequest, resultStr);
                        }
                    }

                    @Override
                    public void onHttpFail(int errorCode, String errorMsg) {
                        LogUtils.v("actionGetUserPhoto onHttpFail:" + errorMsg);
                        ToastUtils.showToast(errorMsg);
                        mViewRef.get().onServerError(whichRequest, errorCode, errorMsg);
                    }

                    @Override
                    public void onHttpProgress(int progress, long total) {
                        mViewRef.get().onServerProcess(whichRequest, progress, total);
                    }
                });
    }

    public void actionDownLoadUserPhoto(final int whichRequest, String userId, String imgUrl) {
        if (StringUtils.isEmpty(imgUrl) || StringUtils.isEmpty(userId))
            return;
        if (!isNetworkAvailable())
            return;

        String filePath = SystemUtils.getAppFilePath(mContext, AppConfig.FOLDER_USERPHOTO);
        String fileName = "user_" + userId + ".jpg";

        AppServerManager.HttpDownLoad(imgUrl,
                filePath,
                fileName,
                new DownLoadHttpCallBack(mActivityName, mContext) {
                    @Override
                    public void onDownLoadStart() {
                        mViewRef.get().onServerStart(whichRequest);
                    }

                    @Override
                    public void onDownLoadSuccess(File file) {
                        ToastUtils.showToast("登录人员照片下载成功，开始进行人员身份验证");
                        LogUtils.v("actionDownLoadUserPhoto onDownLoadSuccess:" + file.getAbsolutePath());
                        mViewRef.get().onServerSuccess(whichRequest, file.getAbsolutePath());
                    }

                    @Override
                    public void onDownLoadFail(Throwable throwable) {
                        ToastUtils.showToast("登录人员照片下载失败");
                        LogUtils.v("actionDownLoadUserPhoto onDownLoadFail:" + throwable.getMessage());
                        mViewRef.get().onServerError(whichRequest, -1, throwable.getMessage());
                    }

                    @Override
                    public void onDownLoadProgress(int progress, long total) {
                        LogUtils.v("actionDownLoadUserPhoto onDownLoadProgress:" + progress + " total:" + total);
                        mViewRef.get().onServerProcess(whichRequest, progress, total);
                    }
                });
    }
}
