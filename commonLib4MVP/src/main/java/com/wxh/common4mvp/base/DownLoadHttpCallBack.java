package com.wxh.common4mvp.base;

import android.content.Context;

import java.io.File;

public abstract class DownLoadHttpCallBack {

    private String mActivityName;
    private Context mContext;
    private boolean mIsShowBaseLoadingDialog;
    private String[] mLoadingDialogMsg;

    public DownLoadHttpCallBack(String mActivityName, Context mContext, boolean mIsShowBaseLoadingDialog, String... mLoadingDialogMsg) {
        this.mActivityName = mActivityName;
        this.mContext = mContext;
        this.mIsShowBaseLoadingDialog = mIsShowBaseLoadingDialog;
        this.mLoadingDialogMsg = mLoadingDialogMsg;
    }

    public DownLoadHttpCallBack(String mActivityName, Context mContext, boolean mIsShowBaseLoadingDialog) {
        this.mActivityName = mActivityName;
        this.mContext = mContext;
        this.mIsShowBaseLoadingDialog = mIsShowBaseLoadingDialog;
        this.mLoadingDialogMsg = null;
    }

    public DownLoadHttpCallBack(String mActivityName, Context mContext) {
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

    /**
     * 下载开始回调
     */
    public abstract void onDownLoadStart();

    /**
     * 下载成功的回调
     *
     * @param file
     */
    public abstract void onDownLoadSuccess(File file);

    /**
     * 下载失败回调
     *
     * @param throwable
     */
    public abstract void onDownLoadFail(Throwable throwable);

    /**
     * 下载进度监听
     *
     * @param progress
     * @param total
     */
    public abstract void onDownLoadProgress(int progress, long total);
}
