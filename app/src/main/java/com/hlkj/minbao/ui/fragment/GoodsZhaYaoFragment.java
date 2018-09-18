package com.hlkj.minbao.ui.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.GoodsZhaYaoPresenter;
import com.hlkj.minbao.ui.adapter.GoodsZhaYaoListAdapter;
import com.hlkj.minbao.view.IGoodsZhaYaoView;
import com.wxh.common4mvp.base.baseImpl.BaseFragment;
import com.wxh.common4mvp.customView.RecycleViewDivider;
import com.wxh.common4mvp.util.SystemUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GoodsZhaYaoFragment extends BaseFragment<GoodsZhaYaoPresenter> implements IGoodsZhaYaoView {

    @BindView(R.id.rv_list_zhayao)
    RecyclerView rvListZhayao;

    private GoodsZhaYaoListAdapter mAdapter;
    private Handler mHandler;

    private List<JSONObject> mdataList;

    @Override
    public GoodsZhaYaoPresenter initPresenter() {
        return new GoodsZhaYaoPresenter(mContext, mFragmentName, this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_goods_zhayao;
    }

    @Override
    public void onUserVisible() {

    }

    @Override
    public void onViewInit() {
        super.onViewInit();
        try {
            mdataList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                JSONObject jo = new JSONObject();
                jo.put("name", "乳化炸药");
                jo.put("type", "50毫米");
                jo.put("count", 20);

                mdataList.add(jo);
            }

            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case GoodsZhaYaoListAdapter.MSG_LISTITEM_FOOTVIEW_CLICK:
                            break;
                        case GoodsZhaYaoListAdapter.MSG_LISTITEM_ITEM_CLICK:
                            break;
                        default:
                            break;
                    }
                }
            };

            mAdapter = new GoodsZhaYaoListAdapter(getActivity(), mHandler);

            rvListZhayao.setLayoutManager(new LinearLayoutManager(mContext));
            rvListZhayao.setItemAnimator(new DefaultItemAnimator());
            rvListZhayao.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL,
                    SystemUtils.dp2px(mContext, 10), Color.parseColor("#00000000")));
            rvListZhayao.setAdapter(mAdapter);

            Observable
                    .timer(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            mAdapter.updateData(mdataList);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            default:
                break;
        }
    }

    public GoodsZhaYaoFragment() {
        // Required empty public constructor
    }

    public static GoodsZhaYaoFragment newInstance() {

        Bundle args = new Bundle();

        GoodsZhaYaoFragment fragment = new GoodsZhaYaoFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
