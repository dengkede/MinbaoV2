package com.wxh.common4mvp.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.wxh.common4mvp.base.BaseApplication;

/**
 * 消息提示统一管理辅助类
 * Created by wxh on 2017/3/8.
 */
public class ToastUtils {
    private static Context mContext = BaseApplication.getInstance();
    private static final int DEFAULT_TIME_DELAY = 50;// 单位：毫秒
    private static Toast mToast;//toast提示类
    private static Handler mToastHandler;

    /**
     * 显示Toast
     *
     * @param msgResId
     */
    public static void showToast(int msgResId) {
        if (mContext != null)
            showToast(mContext.getString(msgResId));
    }

    /**
     * 显示Toast
     *
     * @param msg
     */
    public static void showToast(final String msg) {
        if (mContext != null && msg != null) {

            if (mToastHandler == null) {
                mToastHandler = new Handler();
            }

            mToastHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (mToast == null) {
                        mToast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
                        mToast.setDuration(Toast.LENGTH_SHORT);
                    }
                    mToast.setText(msg);
                    mToast.show();
                }
            }, DEFAULT_TIME_DELAY);
        }
    }
}
