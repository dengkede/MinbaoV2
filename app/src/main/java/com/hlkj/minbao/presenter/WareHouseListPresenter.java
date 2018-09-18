package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.IWareHouseListView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class WareHouseListPresenter extends IBasePresenterImpl<IWareHouseListView> {

    public WareHouseListPresenter(Context context, String activityName, IWareHouseListView view) {
        super(context, activityName, view);
    }
}
