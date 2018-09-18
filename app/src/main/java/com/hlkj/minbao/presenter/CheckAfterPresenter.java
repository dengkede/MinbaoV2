package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.ICheckAfterView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class CheckAfterPresenter extends IBasePresenterImpl<ICheckAfterView> {

    public CheckAfterPresenter(Context context, String activityName, ICheckAfterView view) {
        super(context, activityName, view);
    }
}
