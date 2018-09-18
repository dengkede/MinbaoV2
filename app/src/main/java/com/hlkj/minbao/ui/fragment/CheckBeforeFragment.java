package com.hlkj.minbao.ui.fragment;


import android.os.Bundle;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.CheckBeforePresenter;
import com.hlkj.minbao.view.ICheckBeforeView;
import com.wxh.common4mvp.base.baseImpl.BaseFragment;

public class CheckBeforeFragment extends BaseFragment<CheckBeforePresenter> implements ICheckBeforeView {

    @Override
    public CheckBeforePresenter initPresenter() {
        return new CheckBeforePresenter(mContext, mFragmentName, this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_check_before;
    }

    @Override
    public void onUserVisible() {

    }

    public CheckBeforeFragment() {
        // Required empty public constructor
    }

    public static CheckBeforeFragment newInstance() {

        Bundle args = new Bundle();

        CheckBeforeFragment fragment = new CheckBeforeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewInit() {
        super.onViewInit();

    }
}
