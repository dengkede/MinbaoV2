package com.hlkj.minbao.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.UseRegisterListPresenter;
import com.hlkj.minbao.ui.adapter.UseRegisterListAdapter;
import com.hlkj.minbao.view.IUseRegisterListView;
import com.wxh.common4mvp.base.baseImpl.BaseActivity;
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

public class UseRegisterListActivity extends BaseActivity<UseRegisterListPresenter> implements IUseRegisterListView {

    @BindView(R.id.recycle)
    RecyclerView rvRecycle;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.layout_common_nodata)
    FrameLayout layoutCommonNodata;
    @BindView(R.id.btn_consume)
    Button btnConsume;

    private UseRegisterListAdapter mAdapter;
    private Handler mHandler;

    private boolean isLoading = false;
    private boolean hasMore = false;
    private int pageIndex;

    private List<JSONObject> mdataList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_use_register_list;
    }

    @Override
    protected void initToolBar() {
        setToolbarCenter(R.string.useRegister_title);
        setToolbarBtnLeft(R.mipmap.btn_common_back);
        setToolbarTextRight(R.string.useRegister_list_btn_add);
    }

    @Override
    public UseRegisterListPresenter initPresenter() {
        return new UseRegisterListPresenter(this, mActivityName, this);
    }

    @Override
    protected void onToolBarRightTextClick() {
        super.onToolBarRightTextClick();

    }

    @Override
    public void onViewInit() {
        super.onViewInit();
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
                        case UseRegisterListAdapter.MSG_LISTITEM_FOOTVIEW_CLICK:
                            break;
                        case UseRegisterListAdapter.MSG_LISTITEM_ITEM_CLICK:
                            Intent intent = new Intent(UseRegisterListActivity.this, UseRegisterActivity.class);
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            };

            mAdapter = new UseRegisterListAdapter(this, mHandler);

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

            btnConsume.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_consume:
                break;
            default:
                break;
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
