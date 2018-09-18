package com.hlkj.minbao.ui.activity;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.TransportListPresenter;
import com.hlkj.minbao.ui.adapter.TransportListAdapter;
import com.hlkj.minbao.view.ITransportListView;
import com.wxh.common4mvp.base.baseImpl.BaseActivity;
import com.wxh.common4mvp.customView.RecycleViewDivider;
import com.wxh.common4mvp.util.NetWorkUtils;
import com.wxh.common4mvp.util.SystemUtils;
import com.wxh.common4mvp.util.ToastUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class TransportListActivity extends BaseActivity<TransportListPresenter> implements ITransportListView {
    @BindView(R.id.recycle)
    RecyclerView rvRecycle;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.layout_common_nodata)
    FrameLayout layoutCommonNodata;

    private TransportListAdapter mAdapter;
    private Handler mHandler;
    private int mListType;

    private boolean isLoading = false;
    private boolean hasMore = false;
    private int pageIndex;

    private List<JSONObject> mdataList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_transport_list;
    }

    @Override
    protected void initToolBar() {
        setToolbarBtnLeft(R.mipmap.btn_common_back);
        setToolbarCenter(R.string.transport_detail_title);
    }

    @Override
    public TransportListPresenter initPresenter() {
        return new TransportListPresenter(this, mActivityName, this);
    }

    @Override
    public void onViewInit() {
        super.onViewInit();
        try {
            mListType = getIntent().getIntExtra("status", 0);
            mdataList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                JSONObject jo = new JSONObject();
                if (i == 2)
                    jo.put("type", 0);
                else
                    jo.put("type", 1);

                jo.put("license", "");
                jo.put("count", i + 1);

                mdataList.add(jo);
            }

            pageIndex = 1;
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case TransportListAdapter.MSG_LISTITEM_FOOTVIEW_CLICK:
                            break;
                        case TransportListAdapter.MSG_LISTITEM_ITEM_CLICK:
                            break;
                        default:
                            break;
                    }
                }
            };

            mAdapter = new TransportListAdapter(this, mHandler, mListType);

            rvRecycle.setLayoutManager(new LinearLayoutManager(this));
            rvRecycle.setItemAnimator(new DefaultItemAnimator());
            rvRecycle.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL,
                    SystemUtils.dp2px(this, 6), Color.parseColor("#00000000")));
            rvRecycle.setAdapter(mAdapter);
            rvRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && !rvRecycle.canScrollVertically(1)) {
                        //当正在刷新的时候，屏蔽加载更多
                        if (layoutRefresh.isRefreshing()) {
                            return;
                        }
                        if (!isLoading && hasMore) {
                            //分页加载
                            mAdapter.updateFootView(mAdapter.FOOTVIEW_TYPE_LOADING);
//                            actionGetList(true);
                        }
                    }
                }
            });

            layoutRefresh.setColorSchemeColors(getResources().getColor(R.color.color_common_blue));
            layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //下拉刷新
                    mAdapter.updateFootView(mAdapter.FOOTVIEW_TYPE_LOADING);
                    actionGetList(false);
                }
            });

            layoutRefresh.post(new Runnable() {
                @Override
                public void run() {
                    layoutRefresh.setRefreshing(true);
                    actionGetList(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actionGetList(final boolean isLoadMore) {
        if (isLoadMore) {
            pageIndex++;
            isLoading = true;
        }

        if (!NetWorkUtils.isNetworkAvailable(this)) {
            ToastUtils.showToast(R.string.toast_network_unable);
            layoutRefresh.setRefreshing(false);
            if (isLoadMore) {
                pageIndex--;
                isLoading = false;
                mAdapter.updateFootView(mAdapter.FOOTVIEW_TYPE_ERROR);
            }
            return;
        }

        Observable
                .timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        layoutRefresh.setRefreshing(false);
                        pageIndex = 1;
                        mAdapter.updateData(mdataList);
                    }
                });
    }
}
