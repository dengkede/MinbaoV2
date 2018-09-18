package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.IGoodsInfoView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class GoodInfoPresenter extends IBasePresenterImpl<IGoodsInfoView> {

    public GoodInfoPresenter(Context context, String activityName, IGoodsInfoView view) {
        super(context, activityName, view);
    }
}
