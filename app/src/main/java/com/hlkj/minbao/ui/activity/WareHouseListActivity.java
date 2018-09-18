package com.hlkj.minbao.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.WareHouseListPresenter;
import com.hlkj.minbao.view.IWareHouseListView;
import com.wxh.common4mvp.base.baseImpl.BaseActivity;

import butterknife.BindView;

public class WareHouseListActivity extends BaseActivity<WareHouseListPresenter> implements IWareHouseListView {
    @BindView(R.id.tv_content_zhayao)
    TextView tvContentZhayao;
    @BindView(R.id.layout_zhayao)
    LinearLayout layoutZhayao;
    @BindView(R.id.tv_content_leiguan)
    TextView tvContentLeiguan;
    @BindView(R.id.layout_leiguan)
    LinearLayout layoutLeiguan;
    @BindView(R.id.tv_content_suolei)
    TextView tvContentSuolei;
    @BindView(R.id.layout_suolei)
    LinearLayout layoutSuolei;
    @BindView(R.id.layout_total_review)
    FrameLayout layoutTotalReview;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.layout_common_nodata)
    FrameLayout layoutCommonNodata;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_warehouse_list;
    }

    @Override
    protected void initToolBar() {
        setToolbarCenter(R.string.warehouse_list_title);
        setToolbarBtnLeft(R.mipmap.btn_common_back);
    }

    @Override
    public WareHouseListPresenter initPresenter() {
        return new WareHouseListPresenter(this, mActivityName, this);
    }
}
