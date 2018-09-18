package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.ITransportListView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class TransportListPresenter extends IBasePresenterImpl<ITransportListView> {

    public TransportListPresenter(Context context, String activityName, ITransportListView view) {
        super(context, activityName, view);
    }
}
