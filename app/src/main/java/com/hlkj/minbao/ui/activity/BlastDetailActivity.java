package com.hlkj.minbao.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.BlastDetailPresenter;
import com.hlkj.minbao.ui.adapter.BlastDetailRecordListAdapter;
import com.hlkj.minbao.view.IBlastDetailView;
import com.wxh.common4mvp.base.baseImpl.BaseActivity;
import com.wxh.common4mvp.customView.ExpandLayout;
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

public class BlastDetailActivity extends BaseActivity<BlastDetailPresenter> implements IBlastDetailView {

    @BindView(R.id.layout_detail_top)
    RelativeLayout layoutDetailTop;
    @BindView(R.id.layout_detail_middle)
    ExpandLayout layoutDetailMiddle;
    @BindView(R.id.tv_detail_content_1)
    TextView tvDetailContent1;
    @BindView(R.id.tv_detail_content_2)
    TextView tvDetailContent2;
    @BindView(R.id.tv_detail_content_3)
    TextView tvDetailContent3;
    @BindView(R.id.tv_detail_content_4)
    TextView tvDetailContent4;
    @BindView(R.id.tv_detail_content_5)
    TextView tvDetailContent5;
    @BindView(R.id.tv_detail_content_6)
    TextView tvDetailContent6;
    @BindView(R.id.tv_detail_content_7)
    TextView tvDetailContent7;
    @BindView(R.id.tv_detail_content_8)
    TextView tvDetailContent8;
    @BindView(R.id.tv_detail_content_9)
    TextView tvDetailContent9;
    @BindView(R.id.rv_record)
    RecyclerView rvRecord;
    @BindView(R.id.fab_refresh)
    FloatingActionButton fabRefresh;
    @BindView(R.id.btn_bottom_left)
    Button btnBottomLeft;
    @BindView(R.id.btn_bottom_right)
    Button btnBottomRight;
    @BindView(R.id.btn_bottom_left2)
    Button btnBottomLeft2;
    @BindView(R.id.btn_bottom_right2)
    Button btnBottomRight2;
    @BindView(R.id.btn_bottom_left3)
    Button btnBottomLeft3;
    @BindView(R.id.btn_bottom_right3)
    Button btnBottomRight3;

    private BlastDetailRecordListAdapter mAdapter;
    private Handler mHandler;
    private List<JSONObject> mdataList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_blast_detail;
    }

    @Override
    protected void initToolBar() {
        setToolbarBtnLeft(R.mipmap.btn_common_back);
        setToolbarCenter(R.string.blast_detail_title);
    }

    @Override
    public BlastDetailPresenter initPresenter() {
        return new BlastDetailPresenter(this, mActivityName, this);
    }

    @Override
    public void onViewInit() {
        super.onViewInit();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case BlastDetailRecordListAdapter.MSG_LISTITEM_FOOTVIEW_CLICK:
                        break;
                    case BlastDetailRecordListAdapter.MSG_LISTITEM_ITEM_CLICK:
                        break;
                    default:
                        break;
                }
            }
        };

        layoutDetailMiddle.initExpand(true);
        layoutDetailTop.setOnClickListener(this);
        btnBottomLeft.setVisibility(View.VISIBLE);
        btnBottomLeft.setText(R.string.blast_detail_btn_use);
        btnBottomLeft.setOnClickListener(this);
        btnBottomRight.setText(R.string.blast_detail_btn_receive);
        btnBottomRight.setOnClickListener(this);
        fabRefresh.setOnClickListener(this);

        btnBottomLeft2.setVisibility(View.VISIBLE);
        btnBottomLeft2.setText(R.string.blast_detail_btn_refund);
        btnBottomLeft2.setOnClickListener(this);
        btnBottomRight2.setVisibility(View.VISIBLE);
        btnBottomRight2.setText(R.string.blast_detail_btn_record);
        btnBottomRight2.setOnClickListener(this);

        btnBottomRight3.setVisibility(View.VISIBLE);
        btnBottomRight3.setText(R.string.blast_detail_btn_supervise_record_supervision);
        btnBottomRight3.setOnClickListener(this);

        try {
            mdataList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                JSONObject jo = new JSONObject();
                jo.put("time", System.currentTimeMillis());
                String content = "";
                if (i % 2 == 0)
                    content = "民爆物品已出库，正在运输中…";
                else
                    content = "爆破公司四大员已在爆破现场签到";

                jo.put("content", content);
                String person = "田泽平(技术员)\t\t李成博(保管员)\t\t李国志(安全员)\t\t吴恒华(爆破员)";
                jo.put("person", person);

                mdataList.add(jo);
            }

            mAdapter = new BlastDetailRecordListAdapter(this, mHandler);
            rvRecord.setLayoutManager(new LinearLayoutManager(this));
            rvRecord.setItemAnimator(new DefaultItemAnimator());
//            rvRecord.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL,
//                    SystemUtils.dp2px(mContext, 6), Color.parseColor("#00000000")));
            rvRecord.setAdapter(mAdapter);

            Observable
                    .timer(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            mAdapter.updateData(mdataList);
                            ViewGroup.LayoutParams lp = rvRecord.getLayoutParams();
                            if (mAdapter.getItemCount() > 1) {
                                lp.height = SystemUtils.dp2px(BlastDetailActivity.this, 128);
                                rvRecord.setLayoutParams(lp);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_bottom_left:
                intent = new Intent(this, UseRegisterListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_bottom_right:
                intent = new Intent(this, GoodsReceiveListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_bottom_left2:
                break;
            case R.id.btn_bottom_right2:
                intent = new Intent(this, ProductRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_bottom_right3:
                intent = new Intent(this, SuperviseRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.fab_refresh:
                break;
            case R.id.layout_detail_top:
//                layoutDetailMiddle.toggleExpand();
                break;
            default:
                break;
        }
    }
}
