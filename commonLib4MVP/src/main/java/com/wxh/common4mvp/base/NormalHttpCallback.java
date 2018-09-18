package com.wxh.common4mvp.base;

import android.content.Context;

public abstract class NormalHttpCallback {

    private String mActivityName;
    private Context mContext;
    private boolean mIsShowBaseLoadingDialog;
    private String[] mLoadingDialogMsg;

    public NormalHttpCallback(String mActivityName, Context mContext, boolean mIsShowBaseLoadingDialog, String... mLoadingDialogMsg) {
        this.mActivityName = mActivityName;
        this.mContext = mContext;
        this.mIsShowBaseLoadingDialog = mIsShowBaseLoadingDialog;
        this.mLoadingDialogMsg = mLoadingDialogMsg;
    }

    public NormalHttpCallback(String mActivityName, Context mContext, boolean mIsShowBaseLoadingDialog) {
        this.mActivityName = mActivityName;
        this.mContext = mContext;
        this.mIsShowBaseLoadingDialog = mIsShowBaseLoadingDialog;
        this.mLoadingDialogMsg = null;
    }

    public NormalHttpCallback(String mActivityName, Context mContext) {
        this.mActivityName = mActivityName;
        this.mContext = mContext;
        this.mIsShowBaseLoadingDialog = true;
        this.mLoadingDialogMsg = null;
    }

    public String getmActivityName() {
        return mActivityName;
    }

    public Context getmContext() {
        return mContext;
    }

    public boolean ismIsShowBaseLoadingDialog() {
        return mIsShowBaseLoadingDialog;
    }

    public String[] getmLoadingDialogMsg() {
        return mLoadingDialogMsg;
    }

    //请求开始回调
    public abstract void onHttpStart();

    //请求成功的回调
    public abstract void onHttpSuccess(String resultStr);

    //请求失败回调
    public abstract void onHttpFail(int errorCode, String errorMsg);

    //请求进度回调
    public abstract void onHttpProgress(int progress, long total);
}
