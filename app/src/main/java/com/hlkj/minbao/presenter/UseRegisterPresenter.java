package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.IUseRegisterView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class UseRegisterPresenter extends IBasePresenterImpl<IUseRegisterView> {

    public UseRegisterPresenter(Context context, String activityName, IUseRegisterView view) {
        super(context, activityName, view);
    }
}
