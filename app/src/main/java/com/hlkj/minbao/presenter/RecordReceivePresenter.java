package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.IRecordReceiveView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class RecordReceivePresenter extends IBasePresenterImpl<IRecordReceiveView> {

    public RecordReceivePresenter(Context context, String activityName, IRecordReceiveView view) {
        super(context, activityName, view);
    }
}
