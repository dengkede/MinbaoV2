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
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hlkj.minbao.R;
import com.hlkj.minbao.entity.HomeListDetailInfo;
import com.hlkj.minbao.entity.HomeListInfo;
import com.hlkj.minbao.entity.LoginUserInfo;
import com.hlkj.minbao.presenter.HomeListPresenter;
import com.hlkj.minbao.ui.activity.TransportDetailActivity;
import com.hlkj.minbao.ui.activity.TransportListActivity;
import com.hlkj.minbao.ui.adapter.HomeListAdapter;
import com.hlkj.minbao.util.AppConfig;
import com.hlkj.minbao.view.IHomeListView;
import com.wxh.common4mvp.base.baseImpl.BaseFragment;
import com.wxh.common4mvp.customView.RecycleViewDivider;
import com.wxh.common4mvp.util.SPUtils;
import com.wxh.common4mvp.util.StringUtils;
import com.wxh.common4mvp.util.SystemUtils;
import com.wxh.common4mvp.util.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeListFragment extends BaseFragment<HomeListPresenter> implements IHomeListView {

    @BindView(R.id.recycle)
    RecyclerView rvRecycle;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.layout_common_nodata)
    FrameLayout layoutCommonNodata;

    private static final int LIST_STATUS_PROCESSING = 1;
    private static final int LIST_STATUS_COMPLETED = 2;
    private static final int LIST_STATUS_UNSTARTED = 3;

    private HomeListAdapter mAdapter;
    private Handler mHandler;

    private int mStatus;
    private LoginUserInfo mLoginUserInfo;
    private List<HomeListInfo> mHomeListInfoList;

    @Override
    public HomeListPresenter initPresenter() {
        return new HomeListPresenter(mContext, mFragmentName, this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home_list;
    }

    @Override
    public void onUserVisible() {
        if (layoutRefresh != null) {
            layoutRefresh.post(new Runnable() {
                @Override
                public void run() {
                    layoutRefresh.setRefreshing(true);
                    mPresenter.actionGetHomeList(
                            HomeListPresenter.REQUEST_POST_HOME_LIST,
                            mLoginUserInfo,
                            mStatus,
                            false);
                }
            });
        }
    }

    public HomeListFragment() {
        // Required empty public constructor
    }

    public static HomeListFragment newInstance(int status) {

        Bundle args = new Bundle();
        args.putInt("status", status);

        HomeListFragment fragment = new HomeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewInit() {
        super.onViewInit();
        try {
            mStatus = getArguments().getInt("status", 1);
            mLoginUserInfo = (LoginUserInfo) SPUtils.getInstance().readObject(AppConfig.CACHE_USERINFO);
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Intent intent;
                    switch (msg.what) {
                        case HomeListAdapter.MSG_LISTITEM_FOOTVIEW_CLICK:
                            mAdapter.updateFootView(mAdapter.FOOTVIEW_TYPE_LOADING);
                            mPresenter.actionGetHomeList(
                                    HomeListPresenter.REQUEST_POST_HOME_LIST,
                                    mLoginUserInfo,
                                    mStatus,
                                    true);
                            break;
                        case HomeListAdapter.MSG_LISTITEM_ITEM_CLICK:
                            HomeListInfo data = (HomeListInfo) msg.obj;
                            if (data != null) {
                                mPresenter.actionGetUserIdentityAndBaseData(
                                        HomeListPresenter.REQUEST_GET_USER_IDENTITY_AND_BASE_DATA,
                                        mLoginUserInfo,
                                        data);
                            }

//                            try {
//                                JSONObject data = (JSONObject) msg.obj;
//                                int companyType = data.getInt("companyType");
//                                int status = data.getInt("status");
//                                if (companyType == 0) {
//                                    intent = new Intent(mContext, BlastDetailActivity.class);
//                                    intent.putExtra("status", status);
//                                    startActivity(intent);
//                                } else if (companyType == 1) {
//                                    intent = new Intent(mContext, TransportDetailActivity.class);
//                                    intent.putExtra("status", status);
//                                    startActivity(intent);
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                            break;
                        default:
                            break;
                    }
                }
            };

            mAdapter = new HomeListAdapter(getActivity(), mHandler, mStatus);

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
                        if (!mPresenter.isLoading() && mPresenter.isHasMore()) {
                            //分页加载
                            mAdapter.updateFootView(mAdapter.FOOTVIEW_TYPE_LOADING);
                            mPresenter.actionGetHomeList(
                                    HomeListPresenter.REQUEST_POST_HOME_LIST,
                                    mLoginUserInfo,
                                    mStatus,
                                    true);
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
                    mPresenter.actionGetHomeList(
                            HomeListPresenter.REQUEST_POST_HOME_LIST,
                            mLoginUserInfo,
                            mStatus,
                            false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateListStatus(boolean success, boolean isLoadMore) {
        layoutRefresh.setRefreshing(false);
        mPresenter.setLoading(false);
        if (!success && isLoadMore) {
            int pageIndex = mPresenter.getPageIndex() - 1;
            mPresenter.setPageIndex(pageIndex);
            mAdapter.updateFootView(mAdapter.FOOTVIEW_TYPE_ERROR);
        } else if (success) {
            if (isLoadMore) {
                rvRecycle.setVisibility(View.VISIBLE);
                layoutCommonNodata.setVisibility(View.GONE);
                if (mHomeListInfoList == null)
                    mAdapter.updateFootView(mAdapter.FOOTVIEW_TYPE_ERROR);
                else if (mHomeListInfoList.isEmpty()) {
                    mAdapter.removeFootView();
                } else {
                    mAdapter.updateData(mHomeListInfoList, isLoadMore, mPresenter.isHasMore());
                }
            } else {
                mPresenter.setPageIndex(1);
                if (mHomeListInfoList == null || mHomeListInfoList.isEmpty()) {
                    mAdapter.updateData(new ArrayList<HomeListInfo>(), isLoadMore, mPresenter.isHasMore());
                    rvRecycle.setVisibility(View.INVISIBLE);
                    layoutCommonNodata.setVisibility(View.VISIBLE);
                } else {
                    rvRecycle.setVisibility(View.VISIBLE);
                    layoutCommonNodata.setVisibility(View.GONE);
                    mAdapter.updateData(mHomeListInfoList, isLoadMore, mPresenter.isHasMore());
                }
            }
        }
    }

    @Override
    public void onServerSuccess(int whichRequest, String result) {
        super.onServerSuccess(whichRequest, result);
        switch (whichRequest) {
            case HomeListPresenter.REQUEST_POST_HOME_LIST:
                if (!StringUtils.isEmpty(result)) {
                    try {
                        JSONObject joResult = new JSONObject(result);
                        if (joResult.has("list")) {
                            JSONArray jaList = joResult.getJSONArray("list");
                            if (jaList != null && jaList.length() > 0) {
                                mHomeListInfoList = new Gson().fromJson(jaList.toString(), new TypeToken<List<HomeListInfo>>() {
                                }.getType());
                                boolean hasMore = mHomeListInfoList != null && mHomeListInfoList.size() >= AppConfig.BASE_COMMON_LIST_PAGE_SIZE;
                                mPresenter.setHasMore(hasMore);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case HomeListPresenter.REQUEST_GET_USER_IDENTITY_AND_BASE_DATA:
                if (!StringUtils.isEmpty(result) && mLoginUserInfo != null) {
                    try {
                        int companyType = mLoginUserInfo.getCompanyType();
                        JSONObject joResult = new JSONObject(result);
                        HomeListDetailInfo detailInfo = new Gson().fromJson(result, HomeListDetailInfo.class);
                        if (detailInfo == null) {
                            ToastUtils.showToast(R.string.home_list_error_get_mission_status);
                            return;
                        }

                        //0:显示入库详情界面，按钮状态为到达仓储公司
                        //1:显示入库列表界面
                        //2.显示入库详情界面，按钮状态为入库记录
                        int status = detailInfo.getStatus();

                        Intent intent;
                        if (companyType == 0 || companyType == 1) {//爆破公司、监理公司

                        } else if (companyType == 2) {//配送公司
                            if (status == 0 || status == 2) {
                                HomeListDetailInfo.TransportDetailInfo transportDetailInfo = new Gson().fromJson(joResult.getJSONObject("data").toString(), HomeListDetailInfo.TransportDetailInfo.class);
                                detailInfo.setData(transportDetailInfo);
                                intent = new Intent(mContext, TransportDetailActivity.class);
                                intent.putExtra("homeListDetailInfo", detailInfo);
                                startActivity(intent);
                            } else if (status == 1) {
                                intent = new Intent(mContext, TransportListActivity.class);
                                intent.putExtra("homeListDetailInfo", detailInfo);
                                startActivity(intent);
                            }
                        } else if (companyType == 3) {//仓储公司

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
}
