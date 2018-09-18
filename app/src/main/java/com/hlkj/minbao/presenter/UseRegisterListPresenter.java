package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.IUseRegisterListView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class UseRegisterListPresenter extends IBasePresenterImpl<IUseRegisterListView> {

    public UseRegisterListPresenter(Context context, String activityName, IUseRegisterListView view) {
        super(context, activityName, view);
    }
}
