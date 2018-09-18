package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.IGoodsReceiveListView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class GoodsReceiveListPresenter extends IBasePresenterImpl<IGoodsReceiveListView> {

    public GoodsReceiveListPresenter(Context context, String activityName, IGoodsReceiveListView view) {
        super(context, activityName, view);
    }
}
