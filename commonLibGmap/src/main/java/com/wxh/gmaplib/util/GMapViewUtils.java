package com.wxh.gmaplib.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.util.Pair;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceStatusListener;
import com.wxh.gmaplib.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GMapViewUtils {

    private static final String TAG = "GMapViewUtils";

    public static final String DEFAULT_LOCATION_STYLE = "default_location_style";
    public static final String CUSTOM_LOCATION_STYLE_1 = "custom_location_style_1";

    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static final int MAP_TYPE_NIGHT = 3;
    public static final int MAP_TYPE_NAVI = 4;
    public static final int MAP_TYPE_BUS = 5;

    public static final int LOCATION_TYPE_SHOW = 0;
    public static final int LOCATION_TYPE_LOCATE = 1;
    public static final int LOCATION_TYPE_FOLLOW = 2;
    public static final int LOCATION_TYPE_MAP_ROTATE = 3;
    public static final int LOCATION_TYPE_LOCATION_ROTATE = 4;
    public static final int LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER = 5;
    public static final int LOCATION_TYPE_FOLLOW_NO_CENTER = 6;
    public static final int LOCATION_TYPE_MAP_ROTATE_NO_CENTER = 7;

    public static final int LOGO_POSITION_BOTTOM_LEFT = 0;
    public static final int LOGO_POSITION_BOTTOM_CENTER = 1;
    public static final int LOGO_POSITION_BOTTOM_RIGHT = 2;


    /**
     * 对指定经纬度进行逆地理编码
     *
     * @param context               上下文
     * @param longitude             经度
     * @param latitude              纬度
     * @param onReGeoSearchListener 逆地理编码监听
     */
    public static void getReGeoSearchAddress(final Context context,
                                             double longitude,
                                             double latitude,
                                             final OnReGeoSearchListener onReGeoSearchListener) {
        GeocodeSearch geocodeSearch = new GeocodeSearch(context.getApplicationContext());
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (i == AMapException.CODE_AMAP_SUCCESS) {
                    if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null
                            && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                        String addressName = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                        if (onReGeoSearchListener != null)
                            onReGeoSearchListener.onRegeocodeSearched(addressName, i, "");
                    } else {
                        if (onReGeoSearchListener != null)
                            onReGeoSearchListener.onRegeocodeSearched(null, i, context.getResources().getString(R.string.no_result));
                    }
                } else {
                    if (onReGeoSearchListener != null)
                        onReGeoSearchListener.onRegeocodeSearched(null, i, context.getResources().getString(R.string.no_result));
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });

        LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(
                latLonPoint,
                50,
                GeocodeSearch.AMAP);
        // 设置异步逆地理编码请求
        geocodeSearch.getFromLocationAsyn(query);
    }

    /**
     * 设置默认地图UI显示以及手势交互
     *
     * @param aMap
     */
    public static void setDefaultAmapSettings(AMap aMap, int mapType) {
        if (aMap == null)
            return;

        aMap.setMapType(mapType);
        setCustomUISettings(aMap, LOGO_POSITION_BOTTOM_LEFT, false, false, true, false);
        setCustomGestureSettings(aMap, false, false, false, false);
    }

    /**
     * 自定义地图UI显示
     *
     * @param aMap
     * @param logoPosition
     * @param showScaleControl
     * @param showZoomControl
     * @param showCompassControl
     * @param showLocationButtonControl
     */
    public static void setCustomUISettings(AMap aMap,
                                           int logoPosition,
                                           boolean showScaleControl,
                                           boolean showZoomControl,
                                           boolean showCompassControl,
                                           boolean showLocationButtonControl) {
        if (aMap == null)
            return;

        UiSettings uiSettings = aMap.getUiSettings();

        //设置logo位置
        uiSettings.setLogoPosition(logoPosition);
        //设置比例尺是否显示
        uiSettings.setScaleControlsEnabled(showScaleControl);
        //设置默认缩放按钮是否显示
        uiSettings.setZoomControlsEnabled(showZoomControl);
        //设置默认指南针是否显示
        uiSettings.setCompassEnabled(showCompassControl);
        //设置默认的定位按钮是否显示
        uiSettings.setMyLocationButtonEnabled(showLocationButtonControl);
    }

    /**
     * 自定义地图手势交互
     *
     * @param aMap
     * @param scrollEnabled
     * @param zoomEnabled
     * @param tiltEnabled
     * @param rotateEnabled
     */
    public static void setCustomGestureSettings(AMap aMap,
                                                boolean scrollEnabled,
                                                boolean zoomEnabled,
                                                boolean tiltEnabled,
                                                boolean rotateEnabled) {
        if (aMap == null)
            return;

        UiSettings uiSettings = aMap.getUiSettings();

        //设置是否开启滑动手势
        uiSettings.setScrollGesturesEnabled(scrollEnabled);
        //设置是否开启缩放手势
        uiSettings.setZoomGesturesEnabled(zoomEnabled);
        //设置是否开启倾斜手势
        uiSettings.setTiltGesturesEnabled(tiltEnabled);
        //设置是否开启旋转手势
        uiSettings.setRotateGesturesEnabled(rotateEnabled);
    }

    /**
     * 自定义定位蓝点
     *
     * @param aMap
     * @param resId
     * @param strokeColor
     * @param fillColor
     */
    public static void setCustomLocationStyle(AMap aMap,
                                              boolean showLocationIcon,
                                              int locationType,
                                              int resId,
                                              int strokeColor,
                                              int fillColor,
                                              final OnMyLocationChangedListener onLocationChangedListener) {
        if (aMap == null)
            return;

        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //设置定位模式
        myLocationStyle.myLocationType(locationType);
        //设置是否显示定位蓝点
        myLocationStyle.showMyLocation(showLocationIcon);
        // 自定义定位蓝点图标
        if (resId != -1)
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(resId));
        // 自定义精度范围的圆形边框颜色
        if (strokeColor != -1)
            myLocationStyle.strokeColor(strokeColor);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(3);
        // 设置圆形的填充颜色
        if (fillColor != -1)
            myLocationStyle.radiusFillColor(fillColor);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                if (onLocationChangedListener != null)
                    onLocationChangedListener.onLocationChanged(longitude, latitude);
            }
        });
    }

    /**
     * 获取地图截屏
     *
     * @param aMap
     * @param onMyScreenShotListener
     */
    public static void getMapScreenShot(AMap aMap, final OnMyScreenShotListener onMyScreenShotListener) {
        if (aMap == null)
            return;

        aMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
            @Override
            public void onMapScreenShot(Bitmap bitmap) {

            }

            @Override
            public void onMapScreenShot(Bitmap bitmap, int i) {
                if (onMyScreenShotListener != null)
                    onMyScreenShotListener.onMapScreenShot(bitmap, i);
            }
        });
    }

    /**
     * 添加自定义Marker
     *
     * @param aMap
     * @param longitude
     * @param latitude
     * @param describe
     * @param draggable
     * @param iconRes
     * @param anchorX
     * @param anchorY
     * @return
     */
    public static Map<String, Marker> addMarkerToMap(AMap aMap,
                                                     double longitude,
                                                     double latitude,
                                                     String describe,
                                                     boolean draggable,
                                                     int iconRes,
                                                     float anchorX,
                                                     float anchorY) {
        if (aMap == null || describe == null || describe.isEmpty())
            return null;

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)//设置marker位置
                .anchor(anchorX, anchorY)
                .draggable(draggable)//设置marker是否可拖拽
                .visible(true)//设置marker是否可见
                .icon(BitmapDescriptorFactory.fromResource(iconRes))//设置自定义marker图标
                .setFlat(true);//marker平贴地图效果

        Marker marker = aMap.addMarker(markerOptions);
        Map<String, Marker> markerMap = new HashMap<>();
        markerMap.put(describe, marker);

        return markerMap;
    }

    /**
     * 添加自定义大头针Marker
     *
     * @param aMap
     * @param longitude
     * @param latitude
     * @param describe
     * @param draggable
     * @param iconRes
     * @return
     */
    public static Map<String, Marker> addPinMarkerToMap(AMap aMap,
                                                        double longitude,
                                                        double latitude,
                                                        String describe,
                                                        boolean draggable,
                                                        int iconRes) {
        return addMarkerToMap(aMap,
                longitude,
                latitude,
                describe,
                draggable,
                iconRes,
                0.5f,
                0.5f);
    }

    /**
     * 添加自定义默认Marker
     *
     * @param aMap
     * @param longitude
     * @param latitude
     * @param describe
     * @param draggable
     * @param iconRes
     * @return
     */
    public static Map<String, Marker> addDefaultMarkerToMap(AMap aMap,
                                                            double longitude,
                                                            double latitude,
                                                            String describe,
                                                            boolean draggable,
                                                            int iconRes) {
        return addMarkerToMap(aMap,
                longitude,
                latitude,
                describe,
                draggable,
                iconRes,
                0f,
                0f);
    }

    /**
     * 添加自定义Marker列表
     *
     * @param aMap
     * @param resList
     * @param draggable
     * @param iconRes
     * @param anchorX
     * @param anchorY
     * @return
     */
    public static Map<String, Marker> addMarkersToMap(AMap aMap,
                                                      List<Map<String, Object>> resList,
                                                      boolean draggable,
                                                      int iconRes,
                                                      float anchorX,
                                                      float anchorY) {
        if (aMap == null)
            return null;

        if (resList == null || resList.size() <= 0)
            return null;

        Map<String, Marker> markerMap = new HashMap<>();

        for (int i = 0; i < resList.size(); i++) {
            Map<String, Object> resMap = resList.get(i);
            String describe = (String) resMap.get("describe");
            double longitude = (double) resMap.get("longitude");
            double latitude = (double) resMap.get("latitude");

            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)//设置marker位置
                    .anchor(anchorX, anchorY)
                    .draggable(draggable)//设置marker是否可拖拽
                    .visible(true)//设置marker是否可见
                    .icon(BitmapDescriptorFactory.fromResource(iconRes))//设置自定义marker图标
                    .setFlat(true);//marker平贴地图效果

            Marker marker = aMap.addMarker(markerOptions);
            markerMap.put(describe, marker);
        }

        return markerMap;
    }

    /**
     * 添加自定义默认Marker列表
     *
     * @param aMap
     * @param resList
     * @param draggable
     * @param iconRes
     * @return
     */
    public static Map<String, Marker> addDefaultMarkersToMap(AMap aMap,
                                                             List<Map<String, Object>> resList,
                                                             boolean draggable,
                                                             int iconRes) {
        return addMarkersToMap(aMap,
                resList,
                draggable,
                iconRes,
                0f,
                0f);
    }

    /**
     * 添加自定义大头针Marker列表
     *
     * @param aMap
     * @param resList
     * @param draggable
     * @param iconRes
     * @return
     */
    public static Map<String, Marker> addPinMarkersToMap(AMap aMap,
                                                         List<Map<String, Object>> resList,
                                                         boolean draggable,
                                                         int iconRes) {
        return addMarkersToMap(aMap,
                resList,
                draggable,
                iconRes,
                0.5f,
                0.5f);
    }

    /**
     * 设置Marker在屏幕上某个坐标点，不跟随地图移动
     *
     * @param aMap   AMap对象
     * @param marker marker标记
     * @return
     */
    public static Point setMarkerFixedToMap(AMap aMap,
                                            Marker marker) {
        if (aMap == null)
            return null;

        if (marker == null)
            return null;

        LatLng latLng = marker.getPosition();
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        //设置Marker在屏幕上,不跟随地图移动
        marker.setPositionByPixels(screenPosition.x, screenPosition.y);

        return screenPosition;
    }

    /**
     * 移动地图到距离地图中心点指定偏移量的位置
     *
     * @param aMap        AMap对象
     * @param offsetXByPx X方向偏移量
     * @param offsetYByPx Y方向偏移量
     * @param animated    是否需要动画
     * @param millisecond 移动时间，单位毫秒
     */
    public static void autoChangeMapCamera(AMap aMap,
                                           float offsetXByPx,
                                           float offsetYByPx,
                                           boolean animated,
                                           long millisecond) {
        if (aMap == null)
            return;

        if (animated) {
            if (millisecond != -1)
                aMap.animateCamera(CameraUpdateFactory.scrollBy(offsetXByPx, offsetYByPx), millisecond, null);
            else
                aMap.animateCamera(CameraUpdateFactory.scrollBy(offsetXByPx, offsetYByPx));
        } else {
            aMap.moveCamera(CameraUpdateFactory.scrollBy(offsetXByPx, offsetYByPx));
        }
    }

    /**
     * marker跳动的动画
     *
     * @param context      上下文
     * @param aMap         AMap对象
     * @param screenMarker marker对象
     */
    public static void startMarkerJumpAnimation(Context context,
                                                AMap aMap,
                                                Marker screenMarker,
                                                Point screenPositionPoint) {
        if (aMap == null)
            return;

        if (screenMarker == null)
            return;
        if (screenPositionPoint == null)
            return;

        //根据屏幕距离计算需要移动的目标点
//        final LatLng latLng = screenMarker.getPosition();
        Point point = new Point(screenPositionPoint.x, screenPositionPoint.y);
        point.y -= dip2px(context.getApplicationContext(), 100);
        LatLng target = aMap.getProjection().fromScreenLocation(point);
        //使用TranslateAnimation,填写一个需要移动的目标点
        Animation animation = new TranslateAnimation(target);
        animation.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                // 模拟重加速度的interpolator
                if (input <= 0.5) {
                    return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                } else {
                    return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                }
            }
        });
        //整个移动所需要的时间
        animation.setDuration(1000);
        //设置动画
        screenMarker.setAnimation(animation);
        //开始动画
        screenMarker.startAnimation();
    }

    /**
     * marker地上生长的动画
     *
     * @param growMarker marker对象
     */
    private static void startGrowAnimation(Marker growMarker) {
        if (growMarker != null) {
            Animation animation = new ScaleAnimation(0, 1, 0, 1);
            animation.setInterpolator(new LinearInterpolator());
            //整个移动所需要的时间
            animation.setDuration(1000);
            //设置动画
            growMarker.setAnimation(animation);
            //开始动画
            growMarker.startAnimation();
        }
    }

    /**
     * 添加自定义圆形区域
     *
     * @param aMap
     * @param longitude
     * @param latitude
     * @param radius
     * @param strokeColor
     * @param fillColor
     * @param storkeWidth
     * @return
     */
    public static Circle setCircleToMap(AMap aMap,
                                        double longitude,
                                        double latitude,
                                        double radius,
                                        int strokeColor,
                                        int fillColor,
                                        float storkeWidth) {
        if (aMap == null)
            return null;

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(new LatLng(latitude, longitude))
                .radius(radius)
                .fillColor(fillColor)
                .strokeColor(strokeColor)
                .strokeWidth(storkeWidth);

        Circle circle = aMap.addCircle(circleOptions);

        return circle;
    }

    /**
     * 控制指定marker是否显示信息弹窗
     *
     * @param marker   marker对象
     * @param describe 文字信息
     * @param isShow   是否显示
     */
    public static void controlMarkerInfoWindow(Marker marker,
                                               String describe,
                                               boolean isShow) {
        if (marker == null)
            return;

        if (isShow) {
            marker.setSnippet(describe);
            marker.showInfoWindow();
        } else {
            marker.hideInfoWindow();
        }
    }

    /**
     * 开始进行运行轨迹记录（带轨迹纠偏）
     *
     * @param context           上下文
     * @param aMap              AMap对象
     * @param color             轨迹线颜色
     * @param textureResId      轨迹纹理，-1表示不设置纹理
     * @param markerResId       移动图标
     * @param moveSeconds       图标移动时间
     * @param onMyTraceListener 运行轨迹回调
     */
    public static void startTrace(Context context,
                                  final AMap aMap,
                                  final int color,
                                  final int textureResId,
                                  final int markerResId,
                                  final float moveSeconds,
                                  final OnMyTraceListener onMyTraceListener) {
        final SmoothMoveMarker moveMarker = setSmoothMoveMarkerToMap(aMap, markerResId, moveSeconds);
        LBSTraceClient traceClient = LBSTraceClient.getInstance(context);
        traceClient.startTrace(new TraceStatusListener() {
            @Override
            public void onTraceStatus(List<TraceLocation> locations, List<LatLng> rectifications, String errorInfo) {
                if (LBSTraceClient.TRACE_SUCCESS.equals(errorInfo)) {
                    //在地图上绘制轨迹纠偏后的行驶路径
                    Polyline polyline = setTracedLocationsToMap(aMap, rectifications, color, textureResId);
                    if (moveMarker != null) {
                        //开始移动图标
                        startMarkerMove(moveMarker, rectifications);
                    }

                    if (onMyTraceListener != null)
                        onMyTraceListener.onTraceStatus(polyline, rectifications, errorInfo);
                }
            }
        });
    }

    /**
     * 开始进行运行轨迹记录（带轨迹纠偏）
     *
     * @param context           上下文
     * @param aMap              AMap对象
     * @param color             轨迹线颜色
     * @param textureResId      轨迹纹理，-1表示不设置纹理
     * @param onMyTraceListener 运行轨迹回调
     */
    public static void startTrace(Context context,
                                  final AMap aMap,
                                  final int color,
                                  final int textureResId,
                                  final OnMyTraceListener onMyTraceListener) {
        startTrace(context, aMap, color, textureResId, -1, -1, onMyTraceListener);
    }

    /**
     * 开始进行运行轨迹记录（带轨迹纠偏）
     *
     * @param context           上下文
     * @param aMap              AMap对象
     * @param markerResId       移动图标
     * @param moveSeconds       图标移动时间
     * @param onMyTraceListener 运行轨迹回调
     */
    public static void startTrace(Context context,
                                  final AMap aMap,
                                  final int markerResId,
                                  final float moveSeconds,
                                  final OnMyTraceListener onMyTraceListener) {
        startTrace(context, aMap, -1, -1, markerResId, moveSeconds, onMyTraceListener);
    }

    /**
     * 开始进行运行轨迹记录（带轨迹纠偏）
     *
     * @param context           上下文
     * @param aMap              AMap对象
     * @param onMyTraceListener 运行轨迹回调
     */
    public static void startTrace(Context context,
                                  final AMap aMap,
                                  final OnMyTraceListener onMyTraceListener) {
        startTrace(context, aMap, -1, -1, -1, -1, onMyTraceListener);
    }

    /**
     * 添加运动轨迹到地图
     *
     * @param aMap         AMap对象
     * @param locList      轨迹点集合
     * @param color        轨迹颜色，-1表示不设置颜色
     * @param textureResId 轨迹纹理，-1表示不设置纹理
     * @return polyline对象
     */
    public static Polyline setTracedLocationsToMap(AMap aMap,
                                                   List<LatLng> locList,
                                                   int color,
                                                   int textureResId) {
        if (aMap == null)
            return null;
        if (locList == null || locList.size() <= 0)
            return null;

        PolylineOptions options = new PolylineOptions()
                .addAll(locList)
                .width(10)//设置线段宽度，默认10
                .zIndex(0);//设置Z轴值

        int defaultColor = Color.parseColor("#88FF0000");
        if (textureResId != -1) {//设置线段纹理
            options.setCustomTexture(BitmapDescriptorFactory.fromResource(textureResId));
            options.setUseTexture(true);
        } else if (color != -1) {
            options.color(color);
            options.setUseTexture(false);
        } else {
            options.color(defaultColor);
            options.setUseTexture(false);
        }

        Polyline polyline = aMap.addPolyline(options);
        polyline.setPoints(locList);

        return polyline;
    }

    /**
     * 添加平滑移动图标到地图
     *
     * @param aMap        AMap对象
     * @param markerResId 移动图标
     * @param moveSeconds 移动时间
     * @return SmoothMoveMarker对象
     */
    public static SmoothMoveMarker setSmoothMoveMarkerToMap(AMap aMap,
                                                            int markerResId,
                                                            float moveSeconds) {
        if (aMap == null)
            return null;

        int defaultMarkerResId = R.drawable.icon_car;
        int defaultMoveSeconds = 5;

        SmoothMoveMarker smoothMoveMarker = new SmoothMoveMarker(aMap);
        // 设置 平滑移动的 图标
        if (markerResId != -1)
            smoothMoveMarker.setDescriptor(BitmapDescriptorFactory.fromResource(markerResId));
        else
            smoothMoveMarker.setDescriptor(BitmapDescriptorFactory.fromResource(defaultMarkerResId));

        // 设置平滑移动的总时间  单位  秒
        if (moveSeconds != -1) {
            smoothMoveMarker.setTotalDuration((int) moveSeconds);
        } else {
            smoothMoveMarker.setTotalDuration(defaultMoveSeconds);
        }

        return smoothMoveMarker;
    }

    /**
     * 开始在地图上移动图标指定图标
     *
     * @param moveMarker SmoothMoveMarker对象
     * @param locList    轨迹点集合
     */
    public static void startMarkerMove(SmoothMoveMarker moveMarker,
                                       List<LatLng> locList) {
        if (moveMarker == null)
            return;

        // 取轨迹点的第一个点 作为 平滑移动的启动
        LatLng drivePoint = locList.get(0);
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(locList, drivePoint);
        locList.set(pair.first, drivePoint);
        List<LatLng> subList = locList.subList(pair.first, locList.size());

        // 设置轨迹点
        moveMarker.setPoints(subList);

        moveMarker.startSmoothMove();
    }

    //----------------------------------------------------------------------------------------------

    //dip和px转换
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface OnMyLocationChangedListener {
        /**
         * 定位图层定位回调
         *
         * @param longitude
         * @param latitude
         */
        void onLocationChanged(double longitude, double latitude);
    }

    public interface OnMyScreenShotListener {
        /**
         * 截屏回调
         *
         * @param bitmap
         * @param status
         */
        void onMapScreenShot(Bitmap bitmap, int status);
    }

    public interface OnReGeoSearchListener {
        /**
         * 逆地理编码回调
         *
         * @param reGeoAddress
         * @param errorCode
         * @param errorMsg
         */
        void onRegeocodeSearched(String reGeoAddress, int errorCode, String errorMsg);
    }

    public interface OnMyTraceListener {
        /**
         * 轨迹纠偏回调
         *
         * @param polyline       线段对象
         * @param rectifications 轨迹点集合
         * @param errorInfo      轨迹纠偏错误信息
         */
        void onTraceStatus(Polyline polyline, List<LatLng> rectifications, String errorInfo);
    }
}
