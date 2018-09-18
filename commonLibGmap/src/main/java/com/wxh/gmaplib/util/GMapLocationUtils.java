package com.wxh.gmaplib.util;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.HashMap;
import java.util.Map;

public class GMapLocationUtils {

    private static final String TAG = "GMapLocationUtils";

    private static Context mContext;
    private static Map<String, AMapLocationClient> mLocationClientMap;

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        mContext = context.getApplicationContext();
        mLocationClientMap = new HashMap<>();
    }

    /**
     * 启动定位
     *
     * @param key                标记位
     * @param isOnceLocation     是否启动单次定位
     * @param onLocationListener 定位结果监听
     */
    public static void startLocation(String key, final boolean isOnceLocation, final OnLocationListener onLocationListener) {
        final AMapLocationClient locationClient = initLocationClient(isOnceLocation);
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (isOnceLocation)
                        locationClient.stopLocation();

                    //经度
                    double longitude = aMapLocation.getLongitude();
                    //纬度
                    double latitude = aMapLocation.getLatitude();
                    //地址信息
                    String address = aMapLocation.getAddress();

                    if (onLocationListener != null)
                        onLocationListener.onLocationChanged(longitude, latitude, address);
                }
            }
        };

        locationClient.setLocationListener(mLocationListener);
        mLocationClientMap.put(key, locationClient);

        locationClient.stopLocation();
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @param key 标志位
     */
    public static AMapLocationClient stopLocation(String key) {
        if (key == null || key.isEmpty())
            return null;

        AMapLocationClient client = null;
        if (mLocationClientMap.containsKey(key)) {
            client = mLocationClientMap.get(key);
            if (client.isStarted())
                client.stopLocation();
        }

        return client;
    }

    /**
     * 销毁定位
     *
     * @param key 标志位
     */
    public static void destoryLocation(String key) {
        AMapLocationClient client = stopLocation(key);
        if (client != null) {
            client.onDestroy();
            mLocationClientMap.remove(key);
        }
    }

    private static AMapLocationClient initLocationClient(boolean isOnceLocation) {
        AMapLocationClient mLocationClient = new AMapLocationClient(mContext);

        AMapLocationClientOption mLocationOpt = new AMapLocationClientOption();
        mLocationOpt.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//高精度模式
        mLocationOpt.setOnceLocation(isOnceLocation);//设置单次定位/连续定位
        mLocationOpt.setOnceLocationLatest(isOnceLocation);//设置开启单次定位后返回3s内精度最高的一次结果
        mLocationOpt.setNeedAddress(true);//设置需要返回地址描述

        mLocationClient.setLocationOption(mLocationOpt);

        return mLocationClient;
    }

    public interface OnLocationListener {
        /**
         * 定位回调
         *
         * @param longitude
         * @param latitude
         * @param address
         */
        void onLocationChanged(double longitude, double latitude, String address);
    }
}
