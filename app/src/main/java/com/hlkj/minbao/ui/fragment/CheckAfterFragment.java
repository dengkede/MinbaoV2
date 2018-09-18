package com.hlkj.minbao.ui.fragment;


import android.os.Bundle;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.CheckAfterPresenter;
import com.hlkj.minbao.view.ICheckAfterView;
import com.wxh.common4mvp.base.baseImpl.BaseFragment;

public class CheckAfterFragment extends BaseFragment<CheckAfterPresenter> implements ICheckAfterView {

    @Override
    public CheckAfterPresenter initPresenter() {
        return new CheckAfterPresenter(mContext, mFragmentName, this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_check_after;
    }

    @Override
    public void onUserVisible() {

    }

    public CheckAfterFragment() {
        // Required empty public constructor
    }

    public static CheckAfterFragment newInstance() {

        Bundle args = new Bundle();

        CheckAfterFragment fragment = new CheckAfterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewInit() {
        super.onViewInit();

    }
}
