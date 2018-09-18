package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.IGoodsZhaYaoView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class GoodsZhaYaoPresenter extends IBasePresenterImpl<IGoodsZhaYaoView> {

    public GoodsZhaYaoPresenter(Context context, String activityName, IGoodsZhaYaoView view) {
        super(context, activityName, view);
    }
}
