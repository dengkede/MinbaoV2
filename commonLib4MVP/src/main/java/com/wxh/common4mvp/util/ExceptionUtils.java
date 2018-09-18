package com.wxh.common4mvp.util;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * 异常抛出统一管理工具类
 */
public class ExceptionUtils {

    private static String TAG = "ExceptionUtils";

    public static String handleException(Throwable e) {
        e.printStackTrace();
        String errorMsg;
        if (e instanceof SocketTimeoutException
                || e instanceof ConnectException
                || e instanceof UnknownHostException) {//均视为网络错误
            LogUtils.e(TAG, "-------网络连接异常-------" + e.getMessage());
            errorMsg = "网络连接异常";
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {//均视为解析错误
            LogUtils.e(TAG, "-------数据解析异常-------" + e.getMessage());
            errorMsg = "数据解析异常";
        } else if (e instanceof ApiException) {//服务器返回的错误信息
            int code = ((ApiException) e).getCode();
            errorMsg = "服务器错误:" + code + "|" + e.getMessage();
            LogUtils.e(TAG, "-------服务器错误信息-------code:" + code + " message:" + e.getMessage());
        } else {
            try {
                LogUtils.e(TAG, "-------错误-------" + e.getMessage());
            } catch (Exception e1) {
                LogUtils.e(TAG, "-------未知错误-------");
            }
            errorMsg = "未知错误";
        }

        return errorMsg;
    }

}