package com.hlkj.minbao.ui.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.ui.FaceLivenessActivity;
import com.hlkj.minbao.R;
import com.hlkj.minbao.entity.LoginUserInfo;
import com.hlkj.minbao.util.AppConfig;
import com.hlkj.minbao.util.AppServerManager;
import com.wxh.common4mvp.base.NormalHttpCallback;
import com.wxh.common4mvp.util.ImageUtils;
import com.wxh.common4mvp.util.LogUtils;
import com.wxh.common4mvp.util.NetWorkUtils;
import com.wxh.common4mvp.util.SPUtils;
import com.wxh.common4mvp.util.StringUtils;
import com.wxh.common4mvp.util.SystemUtils;
import com.wxh.common4mvp.util.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FaceLivenessExpActivity extends FaceLivenessActivity {

    private String mBastImageBase64;
    private String mAccessToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onLivenessCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onLivenessCompletion(status, message, base64ImageMap);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
            mBastImageBase64 = base64ImageMap.get("bestImage0");
            actionGetAccessToken();
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
            ToastUtils.showToast(R.string.faceLiveness_fail);
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    private void actionGetAccessToken() {
        try {
            if (!NetWorkUtils.isNetworkAvailable(getApplicationContext())) {
                ToastUtils.showToast(R.string.toast_network_unable);
                return;
            }

            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String faceApiKey = appInfo.metaData.getString("faceApiKey");
            String faceSecretKey = appInfo.metaData.getString("faceSecretKey");

            String url = "https://aip.baidubce.com/oauth/2.0/token";
            Map<String, String> params = new HashMap<>();
            Map<String, String> pathParams = new HashMap<>();
            params.put("grant_type", "client_credentials");
            params.put("client_id", faceApiKey);
            params.put("client_secret", faceSecretKey);

            AppServerManager.HttpGet(url, null, params, false, false, new NormalHttpCallback(TAG, this) {
                @Override
                public void onHttpStart() {

                }

                @Override
                public void onHttpSuccess(String resultStr) {
                    LogUtils.i("actionGetAccessToken onHttpSuccess resultStr:" + resultStr);
                    if (!StringUtils.isEmpty(resultStr)) {
                        try {
                            JSONObject result = new JSONObject(resultStr);
                            if (result != null && result.has("access_token")) {
                                mAccessToken = result.getString("access_token");
                                //请求人脸对比接口
                                actionFaceMatch();
                            } else {
                                ToastUtils.showToast(R.string.faceLiveness_accessToken_fail);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onHttpFail(int errorCode, String errorMsg) {
                    LogUtils.i("actionGetAccessToken onHttpFail errorCode:" + errorCode + " errorMsg:" + errorMsg);
                }

                @Override
                public void onHttpProgress(int progress, long total) {

                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void actionFaceMatch() {
        try {
            if (!NetWorkUtils.isNetworkAvailable(getApplicationContext())) {
                ToastUtils.showToast(R.string.toast_network_unable);
                return;
            }
            if (StringUtils.isEmpty(mAccessToken))
                return;
            if (StringUtils.isEmpty(mBastImageBase64))
                return;

            LoginUserInfo mUserInfo = (LoginUserInfo) SPUtils.getInstance().readObject(AppConfig.CACHE_USERINFO);
            if (mUserInfo == null || StringUtils.isEmpty(mUserInfo.getUserId()))
                return;

            String userPhotoDir = SystemUtils.getAppFilePath(getApplicationContext(), AppConfig.FOLDER_USERPHOTO);
            String userPhotoName = "user_" + mUserInfo.getUserId() + ".jpg";
            final String userPhotoPath = userPhotoDir + File.separator + userPhotoName;
            File file = new File(userPhotoPath);
            if (!file.exists())
                return;

            String userPhotoBase64 = ImageUtils.bitmapToString(userPhotoPath);

//            String userPhoto = (String) SPUtils.getInstance().get(Config.CACHE_USER_PHOTO, "");
//            if (StringUtils.isEmpty(userPhoto))
//                return;

            StringBuffer sb = new StringBuffer("https://aip.baidubce.com/rest/2.0/face/v3/match");
            sb.append("?access_token=" + mAccessToken);
            String url = sb.toString();

            JSONArray jaData = new JSONArray();

            JSONObject imageBase64 = new JSONObject();
            imageBase64.put("image", mBastImageBase64);
            imageBase64.put("image_type", "BASE64");
            imageBase64.put("face_type", "LIVE");
            imageBase64.put("quality_control", "LOW");
            imageBase64.put("liveness_control", "NORMAL");

            JSONObject imageUser = new JSONObject();
            imageUser.put("image", userPhotoBase64);
            imageUser.put("image_type", "BASE64");
            imageUser.put("face_type", "LIVE");
            imageUser.put("quality_control", "LOW");
            imageUser.put("liveness_control", "NORMAL");

            jaData.put(imageBase64);
            jaData.put(imageUser);

            AppServerManager.HttpPostString(url, null, jaData.toString(), false, false, new NormalHttpCallback(TAG, this) {
                @Override
                public void onHttpStart() {

                }

                @Override
                public void onHttpSuccess(String resultStr) {
                    LogUtils.i("actionFaceMatch onHttpSuccess resultStr:" + resultStr);
                    if (!StringUtils.isEmpty(resultStr)) {
                        try {
                            JSONObject result = new JSONObject(resultStr);
                            int error_code = result.getInt("error_code");
                            if (error_code == 0) {
                                JSONObject dataResult = result.getJSONObject("result");
                                String score = dataResult.getString("score");
                                //对比score落在什么区间判断是否匹配
                                if (!StringUtils.isEmpty(score) && Double.valueOf(score) >= 80) {//验证成功
                                    ToastUtils.showToast(R.string.faceLiveness_match_success);
                                    Intent intent = new Intent();
                                    intent.putExtra("success", true);
                                    intent.putExtra("mBastImageBase64", mBastImageBase64);
                                    setResult(RESULT_OK, intent);
                                    FaceLivenessExpActivity.this.finish();
                                } else {
                                    ToastUtils.showToast(getResources().getString(R.string.faceLiveness_match_fail) + "，匹配度：" + score);
                                    FaceLivenessExpActivity.this.finish();
                                }
                            } else {
                                ToastUtils.showToast(getResources().getString(R.string.faceLiveness_match_fail) + "，错误码：" + error_code);
                                FaceLivenessExpActivity.this.finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(R.string.faceLiveness_match_fail);
                            FaceLivenessExpActivity.this.finish();
                        }
                    }
                }

                @Override
                public void onHttpFail(int errorCode, String errorMsg) {
                    LogUtils.i("actionFaceMatch onHttpFail errorCode:" + errorCode + " errorMsg:" + errorMsg);
                }

                @Override
                public void onHttpProgress(int progress, long total) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
