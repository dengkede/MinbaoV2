package com.hlkj.minbao.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hlkj.minbao.R;
import com.hlkj.minbao.entity.LoginUserInfo;
import com.hlkj.minbao.util.AppConfig;
import com.hlkj.minbao.util.AppServerManager;
import com.hlkj.minbao.util.MyOkhttps;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.wxh.common4mvp.base.NormalHttpCallback;
import com.wxh.common4mvp.util.DesHelper;
import com.wxh.common4mvp.util.LogUtils;
import com.wxh.common4mvp.util.SPUtils;
import com.wxh.common4mvp.util.StringUtils;
import com.wxh.common4mvp.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试项目ACTIVITY
 */
public class TextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
    }

//    /**
//     * 测试获取列表
//     */
//    private void text(){
//        LoginUserInfo  userInfo = (LoginUserInfo) SPUtils.getInstance().readObject(AppConfig.CACHE_USERINFO);
//        try {
//            Map<String, String> paths = new HashMap<>();
//            paths.put("userId", userInfo.getUserId());
//
//            JSONObject params = new JSONObject();
//            params.put("page", 1);
//            params.put("size", AppConfig.BASE_COMMON_LIST_PAGE_SIZE);
//            params.put("status", 1);
//            params.put("type", userInfo.getCompanyType());
////            LogUtils.e("actionGetHomeList requestJsonStr:" + params.toString());
//
//            Map<String, String> params2 = new HashMap<String, String>();
//            //MyOkHttp.setheaders("1.2.0");
//            params2.put("page", 1+"");
//            params.put("size", AppConfig.BASE_COMMON_LIST_PAGE_SIZE+"");
//            params.put("status", 1+"");
//            params.put("type", userInfo.getCompanyType()+"");
//            MyOkhttps.get().post(this, "", params, new RawResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, String response) {
//                    if (!StringUtils.isEmpty(response)) {
//                        if (needEncrypt && AppConfig.IS_DES_ENABLED)
//                            response = DesHelper.encrypt(response);
//
//                        postString(url, paths, headers, bodyStr, callback);
//                    }
//                }
//
////                @Override
////                public void onSuccess(int statusCode, JSONObject response) {
//////                Log.d("results_code", response.toString());
////////              if (response.get("code").toString().equals("1")) {
//////                try {
//////                    if (response.get("code").toString().equals("1")) {
//////
//////                    } else {
//////                        ToastHelper.showToast(LoginActivity.this, "验证获取异常,请稍候再试");
//////                    }
//////                } catch (Exception e) {
//////
//////                }
////
////                }
//
//                @Override
//                public void onFailure(int statusCode, String error_msg) {
//
//                    //dismissLoadingView();
//                }
//            },true);
//
//
////            AppServerManager.HttpPostString(
////                    AppConfig.URL_POST_HOME_LIST,
////                    paths,
////                    params.toString(),
////                    new NormalHttpCallback(mActivityName + status, mContext, false) {
////                        @Override
////                        public void onHttpStart() {
////                            mViewRef.get().onServerStart(whichRequest);
////                        }
////
////                        @Override
////                        public void onHttpSuccess(String resultStr) {
////                            LogUtils.v("actionGetHomeList responseDataStr：" + resultStr);
////                            if (resultStr != null) {
////                                mViewRef.get().onServerSuccess(whichRequest, resultStr);
////                            }
////                            mViewRef.get().updateListStatus(true, isLoadMore);
////                        }
////
////                        @Override
////                        public void onHttpFail(int errorCode, String errorMsg) {
////                            LogUtils.v("actionGetHomeList onHttpFail:" + errorMsg);
////                            ToastUtils.showToast(errorMsg);
////                            mViewRef.get().onServerError(whichRequest, errorCode, errorMsg);
////                            mViewRef.get().updateListStatus(false, isLoadMore);
////                        }
////
////                        @Override
////                        public void onHttpProgress(int progress, long total) {
////                            mViewRef.get().onServerProcess(whichRequest, progress, total);
////                        }
////                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
