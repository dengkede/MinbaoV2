package com.hlkj.minbao.ui.fragment;


import android.os.Bundle;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.GoodInfoPresenter;
import com.hlkj.minbao.view.IGoodsInfoView;
import com.wxh.common4mvp.base.baseImpl.BaseFragment;

public class GoodsInfoFragment extends BaseFragment<GoodInfoPresenter> implements IGoodsInfoView {


    @Override
    public GoodInfoPresenter initPresenter() {
        return new GoodInfoPresenter(mContext, mFragmentName, this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_goods_info;
    }

    @Override
    public void onUserVisible() {

    }

    public GoodsInfoFragment() {
        // Required empty public constructor
    }

    public static GoodsInfoFragment newInstance() {

        Bundle args = new Bundle();

        GoodsInfoFragment fragment = new GoodsInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewInit() {
        super.onViewInit();

    }
}
