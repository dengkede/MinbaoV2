package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.IGoodsLeiGuanView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class GoodsLeiGuanPresenter extends IBasePresenterImpl<IGoodsLeiGuanView> {

    public GoodsLeiGuanPresenter(Context context, String activityName, IGoodsLeiGuanView view) {
        super(context, activityName, view);
    }
}
