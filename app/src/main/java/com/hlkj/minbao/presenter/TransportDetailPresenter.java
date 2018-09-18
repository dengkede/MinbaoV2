package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.entity.HomeListDetailInfo;
import com.hlkj.minbao.entity.LoginUserInfo;
import com.hlkj.minbao.util.AppConfig;
import com.hlkj.minbao.util.AppServerManager;
import com.hlkj.minbao.view.ITransportDetailView;
import com.wxh.common4mvp.base.NormalHttpCallback;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;
import com.wxh.common4mvp.util.LogUtils;
import com.wxh.common4mvp.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;

public class TransportDetailPresenter extends IBasePresenterImpl<ITransportDetailView> {

    public static final int REQUEST_GET_TRANSPORT_ARRIVE = 1;
    public static final int REQUEST_GET_USER_IDENTITY_AND_BASE_DATA = 2;

    public TransportDetailPresenter(Context context, String activityName, ITransportDetailView view) {
        super(context, activityName, view);
    }

    public void actionGetTransportArrive(final int whichRequest, LoginUserInfo userInfo, String id, int type) {
        if (userInfo == null)
            return;

        if (!isNetworkAvailable())
            return;

        Map<String, String> paths = new HashMap<>();
        paths.put("userId", userInfo.getUserId());
        paths.put("id", id);
        paths.put("type", String.valueOf(type));

        AppServerManager.HttpGet(
                AppConfig.URL_GET_TRANSPORT_ARRIVE,
                paths,
                null,
                new NormalHttpCallback(mActivityName, mContext) {
                    @Override
                    public void onHttpStart() {
                        mViewRef.get().onServerStart(whichRequest);
                    }

                    @Override
                    public void onHttpSuccess(String resultStr) {
                        LogUtils.v("actionGetTransportArrive responseDataStr：" + resultStr);
                        mViewRef.get().onServerSuccess(whichRequest, resultStr);
                    }

                    @Override
                    public void onHttpFail(int errorCode, String errorMsg) {
                        LogUtils.v("actionGetTransportArrive onHttpFail:" + errorMsg);
                        ToastUtils.showToast(errorMsg);
                        mViewRef.get().onServerError(whichRequest, errorCode, errorMsg);
                    }

                    @Override
                    public void onHttpProgress(int progress, long total) {
                        mViewRef.get().onServerProcess(whichRequest, progress, total);
                    }
                });
    }

    public void actionGetUserIdentityAndBaseData(final int whichRequest, LoginUserInfo userInfo, HomeListDetailInfo homeListDetailInfo) {

        if (userInfo == null || homeListDetailInfo == null)
            return;

        if (!isNetworkAvailable())
            return;

        Map<String, String> paths = new HashMap<>();
        paths.put("deptType", String.valueOf(userInfo.getCompanyType()));
        paths.put("userId", userInfo.getUserId());
        paths.put("type", String.valueOf(homeListDetailInfo.getType()));
        paths.put("id", homeListDetailInfo.getId());

        AppServerManager.HttpGet(
                AppConfig.URL_GET_USER_IDENTITY_AND_BASE_DATA,
                paths,
                null,
                new NormalHttpCallback(mActivityName, mContext) {
                    @Override
                    public void onHttpStart() {
                        mViewRef.get().onServerStart(whichRequest);
                    }

                    @Override
                    public void onHttpSuccess(String resultStr) {
                        LogUtils.v("actionGetUserIdentityAndBaseData responseDataStr：" + resultStr);
                        if (resultStr != null) {
                            mViewRef.get().onServerSuccess(whichRequest, resultStr);
                        }
                    }

                    @Override
                    public void onHttpFail(int errorCode, String errorMsg) {
                        LogUtils.v("actionGetUserIdentityAndBaseData onHttpFail:" + errorMsg);
                        ToastUtils.showToast(errorMsg);
                        mViewRef.get().onServerError(whichRequest, errorCode, errorMsg);
                    }

                    @Override
                    public void onHttpProgress(int progress, long total) {
                        mViewRef.get().onServerProcess(whichRequest, progress, total);
                    }
                });
    }
}
