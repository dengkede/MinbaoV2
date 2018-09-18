package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.IRecordRefundView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class RecordRefundPresenter extends IBasePresenterImpl<IRecordRefundView> {

    public RecordRefundPresenter(Context context, String activityName, IRecordRefundView view) {
        super(context, activityName, view);
    }
}
