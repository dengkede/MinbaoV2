package com.hlkj.minbao.ui.activity;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.SignaturePresenter;
import com.hlkj.minbao.view.ISignatureView;
import com.wxh.common4mvp.base.baseImpl.BaseActivity;

public class SignatureActivity extends BaseActivity<SignaturePresenter> implements ISignatureView {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_signature;
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    public SignaturePresenter initPresenter() {
        return new SignaturePresenter(this, mActivityName, this);
    }
}
