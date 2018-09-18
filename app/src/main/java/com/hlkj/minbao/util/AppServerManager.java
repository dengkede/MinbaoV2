package com.hlkj.minbao.util;

import com.google.gson.Gson;
import com.hlkj.minbao.entity.LoginUserInfo;
import com.wxh.common4mvp.base.BaseEntity;
import com.wxh.common4mvp.base.BaseServerManager;
import com.wxh.common4mvp.base.DownLoadHttpCallBack;
import com.wxh.common4mvp.base.NormalHttpCallback;
import com.wxh.common4mvp.util.ApiException;
import com.wxh.common4mvp.util.DesHelper;
import com.wxh.common4mvp.util.ExceptionUtils;
import com.wxh.common4mvp.util.SPUtils;
import com.wxh.common4mvp.util.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AppServerManager extends BaseServerManager {

    public static void HttpGet(String url,
                               Map<String, String> paths,
                               Map<String, String> params,
                               NormalHttpCallback normalHttpCallback) {
        HttpGet(url, paths, params, true, true, normalHttpCallback);
    }

    public static void HttpGet(String url,
                               Map<String, String> paths,
                               Map<String, String> params,
                               boolean needAddHeader,
                               final boolean needEncrypt,
                               final NormalHttpCallback normalHttpCallback) {
        Map<String, String> headers = new HashMap<>();
        LoginUserInfo userInfo = (LoginUserInfo) SPUtils.getInstance().readObject(AppConfig.CACHE_USERINFO);
        if (userInfo != null && !StringUtils.isEmpty(userInfo.getToken()) && needAddHeader) {
            headers.put("Authorization", "Bearer " + userInfo.getToken());
        }

        if (normalHttpCallback != null) {
            NormalHttpCallback callback = new NormalHttpCallback(
                    normalHttpCallback.getmActivityName(),
                    normalHttpCallback.getmContext(),
                    normalHttpCallback.ismIsShowBaseLoadingDialog(),
                    normalHttpCallback.getmLoadingDialogMsg()) {
                @Override
                public void onHttpStart() {
                    normalHttpCallback.onHttpStart();
                }

                @Override
                public void onHttpSuccess(String resultStr) {
                    if (!StringUtils.isEmpty(resultStr)) {
                        if (needEncrypt && AppConfig.IS_DES_ENABLED) {
                            resultStr = DesHelper.decrypt(resultStr);
                            BaseEntity baseResultEntity = new Gson().fromJson(resultStr, BaseEntity.class);
                            int resultCode = baseResultEntity.getResultCode();
                            String resultMsg = baseResultEntity.getResultMsg();
                            String resultDataStr = baseResultEntity.getResultData() != null ? baseResultEntity.getResultData().toString() : "";
                            if (resultCode >= 200 && resultCode < 300) {
                                normalHttpCallback.onHttpSuccess(resultDataStr);
                            } else {
                                ApiException exception = new ApiException(resultCode, resultMsg);
                                String errorMsg = ExceptionUtils.handleException(exception);
                                normalHttpCallback.onHttpFail(resultCode, errorMsg);
                            }
                        } else {
                            normalHttpCallback.onHttpSuccess(resultStr);
                        }
                    }
                }

                @Override
                public void onHttpFail(int errorCode, String errorMsg) {
                    normalHttpCallback.onHttpFail(errorCode, errorMsg);
                }

                @Override
                public void onHttpProgress(int progress, long total) {
                    normalHttpCallback.onHttpProgress(progress, total);
                }
            };

            get(url, paths, headers, params, callback);
        }
    }

    /**
     * 上传文件
     * @param url
     * @param paths
     * @param files
     * @param params
     * @param normalHttpCallback
     */
    public static void HttpPost(String url,
                                Map<String, String> paths,
                                Map<String, File> files,
                                Map<String, String> params,
                                NormalHttpCallback normalHttpCallback) {
        HttpPost(url, paths, files, params, true, normalHttpCallback);
    }

    public static void HttpPost(String url,
                                Map<String, String> paths,
                                Map<String, File> files,
                                Map<String, String> params,
                                boolean needAddHeader,
                                NormalHttpCallback normalHttpCallback) {
        Map<String, String> headers = new HashMap<>();
        LoginUserInfo userInfo = (LoginUserInfo) SPUtils.getInstance().readObject(AppConfig.CACHE_USERINFO);
        if (userInfo != null && !StringUtils.isEmpty(userInfo.getToken()) && needAddHeader) {
            headers.put("Authorization", "Bearer " + userInfo.getToken());
        }
        post(url, paths, headers, files, params, normalHttpCallback);
    }

    public static void HttpPostString(String url,
                                      Map<String, String> paths,
                                      String bodyStr,
                                      NormalHttpCallback normalHttpCallback) {
        HttpPostString(url, paths, bodyStr, true, true, normalHttpCallback);
    }

    public static void HttpPostString(String url,
                                      Map<String, String> paths,
                                      String bodyStr,
                                      boolean needAddHeader,
                                      final boolean needEncrypt,
                                      final NormalHttpCallback normalHttpCallback) {
        Map<String, String> headers = new HashMap<>();
        LoginUserInfo userInfo = (LoginUserInfo) SPUtils.getInstance().readObject(AppConfig.CACHE_USERINFO);
        if (userInfo != null && !StringUtils.isEmpty(userInfo.getToken()) && needAddHeader) {
            headers.put("Authorization", "Bearer " + userInfo.getToken());
        }

        if (normalHttpCallback != null) {
            NormalHttpCallback callback = new NormalHttpCallback(
                    normalHttpCallback.getmActivityName(),
                    normalHttpCallback.getmContext(),
                    normalHttpCallback.ismIsShowBaseLoadingDialog(),
                    normalHttpCallback.getmLoadingDialogMsg()) {
                @Override
                public void onHttpStart() {
                    normalHttpCallback.onHttpStart();
                }

                @Override
                public void onHttpSuccess(String resultStr) {
                    if (!StringUtils.isEmpty(resultStr)) {
                        if (needEncrypt && AppConfig.IS_DES_ENABLED) {
                            resultStr = DesHelper.decrypt(resultStr);
                            BaseEntity baseResultEntity = new Gson().fromJson(resultStr, BaseEntity.class);
                            int resultCode = baseResultEntity.getResultCode();
                            String resultMsg = baseResultEntity.getResultMsg();
                            String resultDataStr = baseResultEntity.getResultData() != null ? baseResultEntity.getResultData().toString() : "";
                            if (resultCode >= 200 && resultCode < 300) {
                                normalHttpCallback.onHttpSuccess(resultDataStr);
                            } else {
                                ApiException exception = new ApiException(resultCode, resultMsg);
                                String errorMsg = ExceptionUtils.handleException(exception);
                                normalHttpCallback.onHttpFail(resultCode, errorMsg);
                            }
                        } else {
                            normalHttpCallback.onHttpSuccess(resultStr);
                        }
                    }
                }

                @Override
                public void onHttpFail(int errorCode, String errorMsg) {
                    normalHttpCallback.onHttpFail(errorCode, errorMsg);
                }

                @Override
                public void onHttpProgress(int progress, long total) {
                    normalHttpCallback.onHttpProgress(progress, total);
                }
            };

            if (!StringUtils.isEmpty(bodyStr)) {
                if (needEncrypt && AppConfig.IS_DES_ENABLED)
                    bodyStr = DesHelper.encrypt(bodyStr);

                postString(url, paths, headers, bodyStr, callback);
            }
        }
    }

    public static void HttpDownLoad(String url,
                                    final String destDir,
                                    final String fileName,
                                    final DownLoadHttpCallBack downLoadHttpCallBack) {
        downLoadFile(url, destDir, fileName, downLoadHttpCallBack);
    }
}
