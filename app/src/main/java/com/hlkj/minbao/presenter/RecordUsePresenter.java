package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.IRecordUseView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class RecordUsePresenter extends IBasePresenterImpl<IRecordUseView> {

    public RecordUsePresenter(Context context, String activityName, IRecordUseView view) {
        super(context, activityName, view);
    }
}
