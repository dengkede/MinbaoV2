package com.wxh.common4mvp.util;

import android.support.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * 日志打印统一管理工具类
 */
public class LogUtils {

    // 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static boolean DEBUG = true;
    // 日志打印标记
    private static String TAG = "wxh";

    private LogUtils() {
        // 私有构造函数，防止实例化
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void init(String tag) {
        init(tag, true);
    }

    public static void init(String tag, boolean isDebug) {
        if (!StringUtils.isEmpty(tag))
            TAG = tag;
        DEBUG = isDebug;

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag(TAG)
                .showThreadInfo(true)//是否显示线程信息 默认true
                .methodCount(2)//显示方法数量，默认2
                .methodOffset(0)//隐藏内部方法调用到偏移量。 默认0
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return DEBUG;
            }
        });
    }

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        Logger.i(msg);
    }

    public static void d(String msg) {
        Logger.d(msg);
    }

    public static void e(String msg) {
        Logger.e(msg);
    }

    public static void v(String msg) {
        Logger.v(msg);
    }

    public static void xml(String xml) {
        Logger.xml(xml);
    }

    public static void json(String json) {
        Logger.json(json);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (DEBUG)
            Logger.log(Logger.INFO, tag, msg, null);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            Logger.log(Logger.DEBUG, tag, msg, null);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            Logger.log(Logger.ERROR, tag, msg, null);
    }

    public static void v(String tag, String msg) {
        if (DEBUG)
            Logger.log(Logger.VERBOSE, tag, msg, null);
    }
}
