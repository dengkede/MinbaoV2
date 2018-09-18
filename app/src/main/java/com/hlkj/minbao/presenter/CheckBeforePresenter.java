package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.ICheckBeforeView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class CheckBeforePresenter extends IBasePresenterImpl<ICheckBeforeView> {

    public CheckBeforePresenter(Context context, String activityName, ICheckBeforeView view) {
        super(context, activityName, view);
    }
}
