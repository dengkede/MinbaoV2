package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.IGoodsReceiveView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class GoodsReceivePresenter extends IBasePresenterImpl<IGoodsReceiveView> {

    public GoodsReceivePresenter(Context context, String activityName, IGoodsReceiveView view) {
        super(context, activityName, view);
    }
}
