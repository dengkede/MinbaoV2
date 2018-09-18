package com.hlkj.minbao.ui.fragment;


import android.Manifest;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.GeoFencePresenter;
import com.hlkj.minbao.ui.adapter.GeoFenceRecordListAdapter;
import com.hlkj.minbao.view.IGeoFenceView;
import com.wxh.common4mvp.base.baseImpl.BaseFragment;
import com.wxh.common4mvp.util.LogUtils;
import com.wxh.common4mvp.util.PermissionUtils;
import com.wxh.common4mvp.util.SystemUtils;
import com.wxh.common4mvp.util.ToastUtils;
import com.wxh.gmaplib.util.GMapViewUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GeoFenceFragment extends BaseFragment<GeoFencePresenter> implements IGeoFenceView {

    @BindView(R.id.map_geofence)
    TextureMapView mapGeofence;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.rv_record)
    RecyclerView rvRecord;

    private static final String MARKER_DESCRIBE_POINT_BLAST = "point_blast";

    private GeoFenceRecordListAdapter mAdapter;
    private Handler mHandler;
    private List<JSONObject> mdataList;

    private AMap mAMap;//AMap对象全局缓存
    private LatLng mLocateLatlng = null;//定位经纬度信息全局缓存
    private LatLng mBlastLatlng = null;//爆破点实际经纬度全局缓存
    private Map<String, Marker> mMarkerMap = null;//地图上标记点集合全局管理
    private Point mScreenPositionPoint = null;//爆破点在屏幕上固定的点的坐标（单位：像素）全局缓存
    private int mCameraChangeIndex = 0;//地图状态改变次数全局缓存

    @Override
    public GeoFencePresenter initPresenter() {
        return new GeoFencePresenter(mContext, mFragmentName, this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_geofence;
    }

    @Override
    public void onUserVisible() {
        try {
            mAMap = mapGeofence.getMap();
            PermissionUtils.requestPermissionsResult(this,
                    1,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    new PermissionUtils.OnPermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            mapGeofence.setVisibility(View.VISIBLE);
                            if (mMarkerMap == null)
                                mMarkerMap = new HashMap<>();
                            if (isViewVisiableFirst || !mMarkerMap.containsKey(MARKER_DESCRIBE_POINT_BLAST)) {
                                mCameraChangeIndex = 0;
                                GMapViewUtils.setDefaultAmapSettings(mAMap, GMapViewUtils.MAP_TYPE_SATELLITE);
                                mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                                    @Override
                                    public void onCameraChange(CameraPosition cameraPosition) {
                                        LogUtils.e("---onCameraChange---");
                                    }

                                    @Override
                                    public void onCameraChangeFinish(CameraPosition cameraPosition) {
                                        LogUtils.e("---onCameraChangeFinish---");
                                        if (mCameraChangeIndex < 3) {
                                            mCameraChangeIndex++;
                                            if (mCameraChangeIndex == 1) {
                                                //地图由于定位改变状态完成，执行缩放操作
                                                if (mLocateLatlng != null) {
                                                    mAMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                                                }
                                            } else if (mCameraChangeIndex == 2) {
                                                //地图由于缩放改变状态完成，执行平移操作
                                                GMapViewUtils.autoChangeMapCamera(mAMap,
                                                        0,
                                                        SystemUtils.dp2px(mContext, 85),
                                                        true,
                                                        500);
                                            } else if (mCameraChangeIndex == 3) {
                                                //地图由于平移改变状态完成，执行添加标记的操作
                                                if (mLocateLatlng != null) {
                                                    Map<String, Marker> markerMap = GMapViewUtils.addPinMarkerToMap(mAMap,
                                                            mLocateLatlng.longitude,
                                                            mLocateLatlng.latitude,
                                                            MARKER_DESCRIBE_POINT_BLAST,
                                                            false,
                                                            R.drawable.purple_pin);
                                                    if (markerMap != null) {
                                                        mMarkerMap.putAll(markerMap);
                                                        mBlastLatlng = markerMap.get(MARKER_DESCRIBE_POINT_BLAST).getPosition();
                                                        mScreenPositionPoint = GMapViewUtils.setMarkerFixedToMap(mAMap,
                                                                mMarkerMap.get(MARKER_DESCRIBE_POINT_BLAST));
                                                        if (mScreenPositionPoint != null) {
                                                            GMapViewUtils.setCustomGestureSettings(mAMap,
                                                                    true,
                                                                    true,
                                                                    false,
                                                                    false);
                                                            GMapViewUtils.startMarkerJumpAnimation(mContext,
                                                                    mAMap,
                                                                    mMarkerMap.get(MARKER_DESCRIBE_POINT_BLAST),
                                                                    mScreenPositionPoint);

                                                            ToastUtils.showToast("初始位置设置成功，请移动地图选择爆破点");
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            mBlastLatlng = mAMap.getProjection().fromScreenLocation(mScreenPositionPoint);
                                            GMapViewUtils.startMarkerJumpAnimation(mContext,
                                                    mAMap,
                                                    mMarkerMap.get(MARKER_DESCRIBE_POINT_BLAST),
                                                    mScreenPositionPoint);
                                        }
                                    }
                                });

                                //设置定位层是否显示
                                mAMap.setMyLocationEnabled(true);
                                GMapViewUtils.setCustomLocationStyle(mAMap,
                                        true,
                                        GMapViewUtils.LOCATION_TYPE_LOCATE,
                                        -1,
                                        -1,
                                        -1,
                                        new GMapViewUtils.OnMyLocationChangedListener() {
                                            @Override
                                            public void onLocationChanged(double longitude, double latitude) {
                                                LogUtils.e("---onLocationChanged---" + longitude + " ," + latitude);
                                                mLocateLatlng = new LatLng(latitude, longitude);
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onPermissionDenied() {
                            PermissionUtils.showTipsDialog(mContext);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewInit() {
        super.onViewInit();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case GeoFenceRecordListAdapter.MSG_LISTITEM_FOOTVIEW_CLICK:
                        break;
                    case GeoFenceRecordListAdapter.MSG_LISTITEM_ITEM_CLICK:
                        break;
                    default:
                        break;
                }
            }
        };

        if (mMarkerMap == null)
            mMarkerMap = new HashMap<>();

        try {
            mAdapter = new GeoFenceRecordListAdapter(getActivity(), mHandler);
            rvRecord.setLayoutManager(new LinearLayoutManager(mContext));
            rvRecord.setItemAnimator(new DefaultItemAnimator());
//            rvRecord.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL,
//                    SystemUtils.dp2px(mContext, 6), Color.parseColor("#00000000")));
            rvRecord.setAdapter(mAdapter);

            mdataList = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                JSONObject jo = new JSONObject();
                jo.put("time", System.currentTimeMillis());
                String content = "";
                if (i % 2 == 0)
                    content = "老干妈到达警戒点";
                else
                    content = "老干妈确认警戒点周围安全";

                jo.put("content", content);

                mdataList.add(jo);
            }

            Observable
                    .timer(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            if (isAdded()) {
                                mAdapter.updateData(mdataList);
                                ViewGroup.LayoutParams lp = rvRecord.getLayoutParams();
                                if (mAdapter.getItemCount() > 3) {
                                    lp.height = SystemUtils.dp2px(mContext, 170);
                                    rvRecord.setLayoutParams(lp);
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewDestroy() {
        super.onViewDestroy();
        if (mMarkerMap != null)
            mMarkerMap.clear();

        mLocateLatlng = null;
        mScreenPositionPoint = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mapGeofence.onCreate(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapGeofence.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapGeofence.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapGeofence.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_submit:
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public GeoFenceFragment() {
        // Required empty public constructor
    }

    public static GeoFenceFragment newInstance() {

        Bundle args = new Bundle();

        GeoFenceFragment fragment = new GeoFenceFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
