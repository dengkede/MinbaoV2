package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.IProductRecordView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class ProductRecordPresenter extends IBasePresenterImpl<IProductRecordView> {

    public ProductRecordPresenter(Context context, String activityName, IProductRecordView view) {
        super(context, activityName, view);
    }
}
