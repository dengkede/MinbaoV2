package com.hlkj.minbao.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.TextureMapView;
import com.google.gson.Gson;
import com.hlkj.minbao.R;
import com.hlkj.minbao.entity.HomeListDetailInfo;
import com.hlkj.minbao.entity.LoginUserInfo;
import com.hlkj.minbao.presenter.TransportDetailPresenter;
import com.hlkj.minbao.util.AppConfig;
import com.hlkj.minbao.view.ITransportDetailView;
import com.wxh.common4mvp.base.baseImpl.BaseActivity;
import com.wxh.common4mvp.customView.ExpandLayout;
import com.wxh.common4mvp.util.SPUtils;
import com.wxh.common4mvp.util.StringUtils;
import com.wxh.common4mvp.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

public class TransportDetailActivity extends BaseActivity<TransportDetailPresenter> implements ITransportDetailView {

    @BindView(R.id.tv_detail_top_title)
    TextView tvDetailTopTitle;
    @BindView(R.id.tv_detail_content_1)
    TextView tvDetailContent1;
    @BindView(R.id.layout_middle_1)
    RelativeLayout layoutMiddle1;
    @BindView(R.id.tv_detail_content_2)
    TextView tvDetailContent2;
    @BindView(R.id.layout_middle_2)
    RelativeLayout layoutMiddle2;
    @BindView(R.id.tv_detail_content_3)
    TextView tvDetailContent3;
    @BindView(R.id.layout_middle_3)
    RelativeLayout layoutMiddle3;
    @BindView(R.id.tv_detail_content_4)
    TextView tvDetailContent4;
    @BindView(R.id.layout_middle_4)
    RelativeLayout layoutMiddle4;
    @BindView(R.id.tv_detail_content_5)
    TextView tvDetailContent5;
    @BindView(R.id.layout_middle_5)
    RelativeLayout layoutMiddle5;
    @BindView(R.id.tv_detail_content_6)
    TextView tvDetailContent6;
    @BindView(R.id.layout_middle_6)
    RelativeLayout layoutMiddle6;
    @BindView(R.id.layout_detail_middle)
    ExpandLayout layoutDetailMiddle;
    @BindView(R.id.map_transport_detail)
    TextureMapView mapTransportDetail;
    @BindView(R.id.fab_refresh)
    FloatingActionButton fabRefresh;
    @BindView(R.id.btn_arrive)
    Button btnArrive;

    private LoginUserInfo mLoginUserInfo;
    private HomeListDetailInfo mHomeListDetailInfo;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_transport_detail;
    }

    @Override
    protected void initToolBar() {
        setToolbarBtnLeft(R.mipmap.btn_common_back);
        setToolbarCenter(R.string.transport_detail_title);
    }

    @Override
    public TransportDetailPresenter initPresenter() {
        return new TransportDetailPresenter(this, mActivityName, this);
    }

    @Override
    public void onViewInit() {
        super.onViewInit();
        mLoginUserInfo = (LoginUserInfo) SPUtils.getInstance().readObject(AppConfig.CACHE_USERINFO);
        mHomeListDetailInfo = (HomeListDetailInfo) getIntent().getSerializableExtra("homeListDetailInfo");
        if (mLoginUserInfo != null && mHomeListDetailInfo != null) {
            int status = mHomeListDetailInfo.getStatus();
            boolean isBtnUse = mHomeListDetailInfo.isUse();
            //0:出库配送
            //1:退库配送
            //2:入库配送
            //3:变更配送
            int type = mHomeListDetailInfo.getType();
            switch (type) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    if (status == 0)
                        btnArrive.setText(R.string.transport_detail_btn_arrive_warehouse);
                    else if (status == 2)
                        btnArrive.setText(R.string.transport_detail_btn_record);

                    btnArrive.setEnabled(isBtnUse);
                    btnArrive.setClickable(isBtnUse);
                    HomeListDetailInfo.TransportDetailInfo transportDetailInfo = (HomeListDetailInfo.TransportDetailInfo) mHomeListDetailInfo.getData();
                    initHeadView(transportDetailInfo);
                    break;
                case 3:
                    break;
                default:
                    break;
            }

            btnArrive.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_arrive:
                if (mHomeListDetailInfo != null) {
                    int status = mHomeListDetailInfo.getStatus();
                    if (status == 0) {
                        mPresenter.actionGetTransportArrive(
                                TransportDetailPresenter.REQUEST_GET_TRANSPORT_ARRIVE,
                                mLoginUserInfo,
                                mHomeListDetailInfo.getId(),
                                1);
                    } else if (status == 2) {
                        intent = new Intent(TransportDetailActivity.this, TransportListActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapTransportDetail.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapTransportDetail.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapTransportDetail.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapTransportDetail.onPause();
    }

    @Override
    protected void onDestroy() {
        mapTransportDetail.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onServerSuccess(int whichRequest, String result) {
        super.onServerSuccess(whichRequest, result);
        switch (whichRequest) {
            case TransportDetailPresenter.REQUEST_GET_TRANSPORT_ARRIVE:
                mPresenter.actionGetUserIdentityAndBaseData(
                        TransportDetailPresenter.REQUEST_GET_USER_IDENTITY_AND_BASE_DATA,
                        mLoginUserInfo,
                        mHomeListDetailInfo);
                break;
            case TransportDetailPresenter.REQUEST_GET_USER_IDENTITY_AND_BASE_DATA:
                if (!StringUtils.isEmpty(result)) {
                    HomeListDetailInfo detailInfo = new Gson().fromJson(result, HomeListDetailInfo.class);
                    if (detailInfo == null) {
                        ToastUtils.showToast(R.string.home_list_error_get_mission_status);
                        return;
                    }

                    //1:显示入库列表界面
                    int status = detailInfo.getStatus();
                    if (status == 1) {
                        Intent intent = new Intent(TransportDetailActivity.this, TransportListActivity.class);
                        intent.putExtra("homeListDetailInfo", detailInfo);
                        startActivity(intent);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void initHeadView(HomeListDetailInfo.TransportDetailInfo data) {
        int type = mHomeListDetailInfo.getType();
        if (data != null) {
            int order = data.getOrder();
            String projectName = data.getProjectName();
            String companyName = data.getCompanyName();
            String address = data.getAddress();
            String car = data.getCar();
            long time = data.getTransportTime();

            String titleStr = String.format(getResources().getString(R.string.home_list_item_title), order);
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            String timeStr = format.format(new Date(time));

            tvDetailTopTitle.setText(titleStr);
            tvDetailContent1.setText(projectName);
            tvDetailContent2.setText(companyName);
            if (type == 2) {
                layoutMiddle3.setVisibility(View.GONE);
            }
            tvDetailContent4.setText(address);
            tvDetailContent5.setText(car);
            tvDetailContent6.setText(timeStr);
        }

    }
}
