package com.wxh.common4mvp.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.wxh.common4mvp.util.SPUtils;

public class BaseApplication extends Application {
    private static BaseApplication mApplication = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo info : manager.getRunningAppProcesses()) {
            if (info.pid == pid && info.processName.equals(getPackageName())) {
                mApplication = this;
                init();
            }
        }
    }

    public void init() {
        SPUtils.init(this);
    }

    public static BaseApplication getInstance() {
        return mApplication;
    }
}
