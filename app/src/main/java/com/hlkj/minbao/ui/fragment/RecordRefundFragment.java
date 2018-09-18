package com.hlkj.minbao.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.RecordRefundPresenter;
import com.hlkj.minbao.ui.adapter.ReviewLeiGuanListAdapter;
import com.hlkj.minbao.ui.adapter.ReviewSuoLeiListAdapter;
import com.hlkj.minbao.ui.adapter.ReviewZhaYaoListAdapter;
import com.hlkj.minbao.view.IRecordRefundView;
import com.wxh.common4mvp.base.baseImpl.BaseFragment;
import com.wxh.common4mvp.customView.RecycleViewDivider;
import com.wxh.common4mvp.util.SystemUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecordRefundFragment extends BaseFragment<RecordRefundPresenter> implements IRecordRefundView {

    @BindView(R.id.rv_list_zhayao)
    RecyclerView rvListZhayao;
    @BindView(R.id.rv_list_leiguan)
    RecyclerView rvListLeiguan;
    @BindView(R.id.rv_list_suolei)
    RecyclerView rvListSuolei;
    @BindView(R.id.fab_upload_video)
    FloatingActionButton fabUploadVideo;
    @BindView(R.id.fab_upload_taizhang)
    FloatingActionButton fabUploadTaizhang;

    private ReviewZhaYaoListAdapter mZhaYaoListAdapter;
    private ReviewLeiGuanListAdapter mLeiGuanListAdapter;
    private ReviewSuoLeiListAdapter mSuoLeiListAdapter;

    private List<JSONObject> mZhaYaoDataList;
    private List<JSONObject> mLeiGuanDataList;
    private List<JSONObject> mSuoLeiDataList;

    @Override
    public RecordRefundPresenter initPresenter() {
        return new RecordRefundPresenter(mContext, mFragmentName, this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_record_refund;
    }

    @Override
    public void onUserVisible() {
        try {
            mZhaYaoDataList = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                JSONObject jo = new JSONObject();
                jo.put("name", "乳化炸药");
                jo.put("type", "50毫米");
                jo.put("count", 20);

                mZhaYaoDataList.add(jo);
            }

            mLeiGuanDataList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                JSONObject jo = new JSONObject();
                jo.put("name", "火雷管");
                jo.put("type", "1-1");
                jo.put("count", 200);

                mLeiGuanDataList.add(jo);
            }

            mSuoLeiDataList = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                JSONObject jo = new JSONObject();
                jo.put("name", "导爆索");
                jo.put("count", 200);

                mSuoLeiDataList.add(jo);
            }

            mZhaYaoListAdapter = new ReviewZhaYaoListAdapter(getActivity(), null);
            rvListZhayao.setLayoutManager(new LinearLayoutManager(mContext));
            rvListZhayao.setItemAnimator(new DefaultItemAnimator());
            rvListZhayao.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL,
                    SystemUtils.dp2px(mContext, 10), Color.parseColor("#00000000")));
            rvListZhayao.setAdapter(mZhaYaoListAdapter);
            mZhaYaoListAdapter.updateData(mZhaYaoDataList);

            mLeiGuanListAdapter = new ReviewLeiGuanListAdapter(getActivity(), null);
            rvListLeiguan.setLayoutManager(new LinearLayoutManager(mContext));
            rvListLeiguan.setItemAnimator(new DefaultItemAnimator());
            rvListLeiguan.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL,
                    SystemUtils.dp2px(mContext, 10), Color.parseColor("#00000000")));
            rvListLeiguan.setAdapter(mLeiGuanListAdapter);
            mLeiGuanListAdapter.updateData(mLeiGuanDataList);

            mSuoLeiListAdapter = new ReviewSuoLeiListAdapter(getActivity(), null);
            rvListSuolei.setLayoutManager(new LinearLayoutManager(mContext));
            rvListSuolei.setItemAnimator(new DefaultItemAnimator());
            rvListSuolei.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL,
                    SystemUtils.dp2px(mContext, 10), Color.parseColor("#00000000")));
            rvListSuolei.setAdapter(mSuoLeiListAdapter);
            mSuoLeiListAdapter.updateData(mSuoLeiDataList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RecordRefundFragment() {
        // Required empty public constructor
    }

    public static RecordRefundFragment newInstance() {

        Bundle args = new Bundle();

        RecordRefundFragment fragment = new RecordRefundFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewInit() {
        super.onViewInit();

    }
}
