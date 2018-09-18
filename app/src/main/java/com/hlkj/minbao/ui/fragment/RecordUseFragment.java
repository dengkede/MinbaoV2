package com.hlkj.minbao.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.RecordUsePresenter;
import com.hlkj.minbao.ui.activity.UseRegisterActivity;
import com.hlkj.minbao.ui.adapter.RecordUseListAdapter;
import com.hlkj.minbao.view.IRecordUseView;
import com.wxh.common4mvp.base.baseImpl.BaseFragment;
import com.wxh.common4mvp.customView.RecycleViewDivider;
import com.wxh.common4mvp.util.NetWorkUtils;
import com.wxh.common4mvp.util.SystemUtils;
import com.wxh.common4mvp.util.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class RecordUseFragment extends BaseFragment<RecordUsePresenter> implements IRecordUseView {

    @BindView(R.id.recycle)
    RecyclerView rvRecycle;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.layout_common_nodata)
    FrameLayout layoutCommonNodata;

    private RecordUseListAdapter mAdapter;
    private Handler mHandler;

    private boolean isLoading = false;
    private boolean hasMore = false;
    private int pageIndex;

    private List<JSONObject> mdataList;

    @Override
    public RecordUsePresenter initPresenter() {
        return new RecordUsePresenter(mContext, mFragmentName, this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_record_use;
    }

    @Override
    public void onUserVisible() {
        try {
            mdataList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                JSONObject jo = new JSONObject();
                jo.put("title", String.format(getResources().getString(R.string.useRegister_list_item_title), i));
                jo.put("geoFence", "电子围栏已解除");

                JSONArray jaZhayao = new JSONArray();
                JSONObject joZhayao1 = new JSONObject();
                joZhayao1.put("content", "共使用100公斤XXX型号老干爹炸药");
                JSONObject joZhayao2 = new JSONObject();
                joZhayao2.put("content", "共使用200公斤XXX型号老干妈炸药");
                jaZhayao.put(joZhayao1);
                jaZhayao.put(joZhayao2);

                JSONArray jaLeyguan = new JSONArray();
                JSONObject joLeiguan1 = new JSONObject();
                joLeiguan1.put("content", "共使用1段1米100发老干爹雷管");
                JSONObject joLeiguan2 = new JSONObject();
                joLeiguan2.put("content", "共使用5段5米200发老干妈雷管");
                jaLeyguan.put(joLeiguan1);
                jaLeyguan.put(joLeiguan2);

                JSONArray jaDaobaowu = new JSONArray();
                JSONObject joDaobaowu = new JSONObject();
                joDaobaowu.put("content", "共使用200米老干爹导爆管");
                jaDaobaowu.put(joDaobaowu);

                jo.put("leiguan", jaLeyguan);
                jo.put("zhayao", jaZhayao);
                jo.put("daobaowu", jaDaobaowu);

                mdataList.add(jo);
            }

            pageIndex = 1;
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case RecordUseListAdapter.MSG_LISTITEM_FOOTVIEW_CLICK:
                            break;
                        case RecordUseListAdapter.MSG_LISTITEM_ITEM_CLICK:
                            Intent intent = new Intent(mContext, UseRegisterActivity.class);
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            };

            mAdapter = new RecordUseListAdapter(getActivity(), mHandler);

            rvRecycle.setLayoutManager(new LinearLayoutManager(mContext));
            rvRecycle.setItemAnimator(new DefaultItemAnimator());
            rvRecycle.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL,
                    SystemUtils.dp2px(mContext, 6), Color.parseColor("#00000000")));
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

    public RecordUseFragment() {
        // Required empty public constructor
    }

    public static RecordUseFragment newInstance() {

        Bundle args = new Bundle();

        RecordUseFragment fragment = new RecordUseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewInit() {
        super.onViewInit();

    }

    private void actionGetList(final boolean isLoadMore) {
        if (isLoadMore) {
            pageIndex++;
            isLoading = true;
        }

        if (!NetWorkUtils.isNetworkAvailable(mContext)) {
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
                        if (isAdded()) {
                            layoutRefresh.setRefreshing(false);
                            pageIndex = 1;
                            mAdapter.updateData(mdataList);
                        }
                    }
                });
    }
}
