package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.IGeoFenceView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class GeoFencePresenter extends IBasePresenterImpl<IGeoFenceView> {

    public GeoFencePresenter(Context context, String activityName, IGeoFenceView view) {
        super(context, activityName, view);
    }
}
