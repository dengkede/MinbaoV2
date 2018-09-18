package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.IBlastDetailView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class BlastDetailPresenter extends IBasePresenterImpl<IBlastDetailView> {

    public BlastDetailPresenter(Context context, String activityName, IBlastDetailView view) {
        super(context, activityName, view);
    }
}
