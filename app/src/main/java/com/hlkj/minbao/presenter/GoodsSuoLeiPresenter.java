package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.IGoodsSuoLeiView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class GoodsSuoLeiPresenter extends IBasePresenterImpl<IGoodsSuoLeiView> {

    public GoodsSuoLeiPresenter(Context context, String activityName, IGoodsSuoLeiView view) {
        super(context, activityName, view);
    }
}
