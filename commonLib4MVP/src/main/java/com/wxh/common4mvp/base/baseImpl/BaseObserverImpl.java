package com.wxh.common4mvp.base.baseImpl;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.wxh.common4mvp.base.BaseRetrofitHelper;
import com.wxh.common4mvp.customView.LoadingDialog;
import com.wxh.common4mvp.util.LogUtils;
import com.wxh.common4mvp.util.StringUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class BaseObserverImpl<T> implements Observer<T> {

    protected String mActivityName;
    protected Context mContext;
    protected boolean mIsShowBaseLoadingDialog;
    protected String[] mLoadingDialogMsg;

    protected LoadingDialog loadingDialog;

    public BaseObserverImpl(Context mContext, String mActivityName, boolean showBaseLoadingDialog, String... msg) {
        this.mContext = mContext;
        this.mActivityName = mActivityName;
        this.mIsShowBaseLoadingDialog = showBaseLoadingDialog;
        this.mLoadingDialogMsg = msg;
    }

    @Override
    public void onSubscribe(Disposable d) {
        BaseRetrofitHelper.getInstance().addDisposable(mActivityName, d);
        if (mIsShowBaseLoadingDialog) {
            showBaseLoadingDialog(mLoadingDialogMsg);
        }
    }

    @Override
    public void onNext(T t) {
        dissMissBaseLoadingDialog();
    }

    @Override
    public void onError(Throwable e) {
        dissMissBaseLoadingDialog();
    }

    @Override
    public void onComplete() {

    }

    /**
     * 显示进度加载框
     */
    protected void showBaseLoadingDialog(String... msg) {
        String message = "正在加载";
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.createDialog(mContext);
            loadingDialog.setCanceledOnTouchOutside(false);
        }

        loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {//返回键
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        LogUtils.v("OnKeyListener back up");
                        //取消网络请求
                        BaseRetrofitHelper.getInstance().clearDisposable(mActivityName);
                        dissMissBaseLoadingDialog();
                    }
                }
                return false;
            }
        });

        if (msg != null && msg.length > 0 && !StringUtils.isEmpty(msg[0])) {
            message = msg[0];
        }
        loadingDialog.setMessage(message);
        loadingDialog.startAnim();
        loadingDialog.show();
    }

    /**
     * 关闭进度加载框
     */
    protected void dissMissBaseLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog.stopAnim();
            loadingDialog = null;
        }
    }
}
