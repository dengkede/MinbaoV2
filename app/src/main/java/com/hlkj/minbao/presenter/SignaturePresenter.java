package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.view.ISignatureView;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;

public class SignaturePresenter extends IBasePresenterImpl<ISignatureView> {

    public SignaturePresenter(Context context, String activityName, ISignatureView view) {
        super(context, activityName, view);
    }


}
