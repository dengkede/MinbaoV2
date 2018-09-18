package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.ISuperviseRecordView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class SuperviseRecordPresenter extends IBasePresenterImpl<ISuperviseRecordView> {

    public SuperviseRecordPresenter(Context context, String activityName, ISuperviseRecordView view) {
        super(context, activityName, view);
    }
}
