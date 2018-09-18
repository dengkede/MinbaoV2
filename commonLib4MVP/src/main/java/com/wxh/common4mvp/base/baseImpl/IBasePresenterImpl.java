package com.wxh.common4mvp.base.baseImpl;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.wxh.common4mvp.R;
import com.wxh.common4mvp.base.baseInterface.IBasePresenter;
import com.wxh.common4mvp.base.BaseRetrofitHelper;
import com.wxh.common4mvp.base.baseInterface.IBaseView;
import com.wxh.common4mvp.customView.LoadingDialog;
import com.wxh.common4mvp.util.LogUtils;
import com.wxh.common4mvp.util.NetWorkUtils;
import com.wxh.common4mvp.util.StringUtils;
import com.wxh.common4mvp.util.ToastUtils;

import java.lang.ref.WeakReference;

public abstract class IBasePresenterImpl<V extends IBaseView> implements IBasePresenter {

    protected WeakReference<V> mViewRef;
    protected String mActivityName;
    protected Context mContext;

    protected LoadingDialog loadingDialog;

    public IBasePresenterImpl(Context context, String activityName, V view) {
        this.mContext = context;
        this.mActivityName = activityName;
        this.mViewRef = new WeakReference<>(view);
    }

    @Override
    public void fetch() {
        if (mViewRef != null)
            mViewRef.get().onViewInit();
    }

    @Override
    public void detach() {
        if (mViewRef != null) {
            mViewRef.get().onViewDestroy();
            mViewRef.clear();
            mViewRef = null;
        }
        BaseRetrofitHelper.getInstance().clearDisposable(mActivityName);
    }

    /**
     * 判断网络是否可用
     *
     * @return
     */
    protected boolean isNetworkAvailable() {
        if (!NetWorkUtils.isNetworkAvailable(mContext)) {
            ToastUtils.showToast(R.string.toast_network_unable);
            return false;
        }

        return true;
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

        if (msg.length > 0 && !StringUtils.isEmpty(msg[0])) {
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
