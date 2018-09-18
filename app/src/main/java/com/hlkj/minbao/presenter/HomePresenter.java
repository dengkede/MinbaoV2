package com.hlkj.minbao.presenter;

import android.app.Activity;
import android.content.Context;

import com.hlkj.minbao.R;
import com.hlkj.minbao.view.IHomeView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;
import com.wxh.common4mvp.util.ActivityManager;
import com.wxh.common4mvp.util.ToastUtils;

public class HomePresenter extends IBasePresenterImpl<IHomeView> {

    private long ExitTime = 0;

    public HomePresenter(Context context, String activityName, IHomeView view) {
        super(context, activityName, view);
    }

    public void onHomeBackPressed(Activity activity) {
        if ((System.currentTimeMillis() - ExitTime) > 2000) {
            ToastUtils.showToast(R.string.comm_exit_msg);
            ExitTime = System.currentTimeMillis();
        } else {
            ActivityManager.getAppInstance().finishActivity(activity);
        }
    }
}
