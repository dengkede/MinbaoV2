package com.hlkj.minbao.presenter;

import android.content.Context;

import com.hlkj.minbao.entity.HomeListInfo;
import com.hlkj.minbao.entity.LoginUserInfo;
import com.hlkj.minbao.util.AppConfig;
import com.hlkj.minbao.util.AppServerManager;
import com.hlkj.minbao.view.IHomeListView;
import com.wxh.common4mvp.base.NormalHttpCallback;
import com.wxh.common4mvp.base.baseImpl.IBasePresenterImpl;
import com.wxh.common4mvp.util.LogUtils;
import com.wxh.common4mvp.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeListPresenter extends IBasePresenterImpl<IHomeListView> {

    public static final int REQUEST_POST_HOME_LIST = 1;
    public static final int REQUEST_GET_USER_IDENTITY_AND_BASE_DATA = 2;

    private boolean isLoading = false;
    private boolean hasMore = false;
    private int pageIndex = 1;

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public HomeListPresenter(Context context, String activityName, IHomeListView view) {
        super(context, activityName, view);
    }

    public void actionGetHomeList(final int whichRequest, LoginUserInfo userInfo, int status, final boolean isLoadMore) {
        if (userInfo == null)
            return;

        if (isLoadMore) {
            pageIndex++;
            isLoading = true;
        }

        if (!isNetworkAvailable()) {
            mViewRef.get().updateListStatus(false, isLoadMore);
            return;
        }

        try {
            Map<String, String> paths = new HashMap<>();
            paths.put("userId", userInfo.getUserId());

            JSONObject params = new JSONObject();
            params.put("page", pageIndex);
            params.put("size", AppConfig.BASE_COMMON_LIST_PAGE_SIZE);
            params.put("status", status);
            params.put("type", userInfo.getCompanyType());
            LogUtils.e("actionGetHomeList requestJsonStr:" + params.toString());

            AppServerManager.HttpPostString(
                    AppConfig.URL_POST_HOME_LIST,
                    paths,
                    params.toString(),
                    new NormalHttpCallback(mActivityName + status, mContext, false) {
                        @Override
                        public void onHttpStart() {
                            mViewRef.get().onServerStart(whichRequest);
                        }

                        @Override
                        public void onHttpSuccess(String resultStr) {
                            LogUtils.v("actionGetHomeList responseDataStr：" + resultStr);
                            if (resultStr != null) {
                                mViewRef.get().onServerSuccess(whichRequest, resultStr);
                            }
                            mViewRef.get().updateListStatus(true, isLoadMore);
                        }

                        @Override
                        public void onHttpFail(int errorCode, String errorMsg) {
                            LogUtils.v("actionGetHomeList onHttpFail:" + errorMsg);
                            ToastUtils.showToast(errorMsg);
                            mViewRef.get().onServerError(whichRequest, errorCode, errorMsg);
                            mViewRef.get().updateListStatus(false, isLoadMore);
                        }

                        @Override
                        public void onHttpProgress(int progress, long total) {
                            mViewRef.get().onServerProcess(whichRequest, progress, total);
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void actionGetUserIdentityAndBaseData(final int whichRequest, LoginUserInfo userInfo, HomeListInfo homeListInfo) {

        if (userInfo == null || homeListInfo == null)
            return;

        if (!isNetworkAvailable())
            return;

        Map<String, String> paths = new HashMap<>();
        paths.put("deptType", String.valueOf(userInfo.getCompanyType()));
        paths.put("userId", userInfo.getUserId());
        paths.put("type", String.valueOf(homeListInfo.getType()));
        paths.put("id", homeListInfo.getId());

        AppServerManager.HttpGet(
                AppConfig.URL_GET_USER_IDENTITY_AND_BASE_DATA,
                paths,
                null,
                new NormalHttpCallback(mActivityName, mContext) {
                    @Override
                    public void onHttpStart() {
                        mViewRef.get().onServerStart(whichRequest);
                    }

                    @Override
                    public void onHttpSuccess(String resultStr) {
                        LogUtils.v("actionGetUserIdentityAndBaseData responseDataStr：" + resultStr);
                        if (resultStr != null) {
                            mViewRef.get().onServerSuccess(whichRequest, resultStr);
                        }
                    }

                    @Override
                    public void onHttpFail(int errorCode, String errorMsg) {
                        LogUtils.v("actionGetUserIdentityAndBaseData onHttpFail:" + errorMsg);
                        ToastUtils.showToast(errorMsg);
                        mViewRef.get().onServerError(whichRequest, errorCode, errorMsg);
                    }

                    @Override
                    public void onHttpProgress(int progress, long total) {
                        mViewRef.get().onServerProcess(whichRequest, progress, total);
                    }
                });
    }
}
