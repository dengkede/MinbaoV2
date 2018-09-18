package com.wxh.common4mvp.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.UUID;

/**
 * 系统操作统一管理工具类
 */

public class SystemUtils {

    private static final String TAG = "SystemUtils";

    /**
     * 获取手机屏幕宽度
     *
     * @param context 上下文
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取手机屏幕高度
     *
     * @param context 上下文
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取手机顶部状态栏高度
     *
     * @param context 上下文
     */
    public static int getStatusbarHeight(Context context) {
        Resources resources = context.getResources();
        int sbResourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(sbResourceId);
    }

    /**
     * 获取手机底部虚拟导航栏高度
     *
     * @param context 上下文
     */
    public static int getNavigationbarHeight(Context context) {
        Resources resources = context.getResources();
        int nbResourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(nbResourceId);
    }

    /**
     * 获取缩放比例
     *
     * @param context
     * @return
     */
    public static float getSystemDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取字体缩放比例
     *
     * @param context
     * @return
     */
    public static float getSystemScaledDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * 获取屏幕密度
     *
     * @param context
     * @return
     */
    public static int getSystemDensityDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = getSystemDensity(context);
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = getSystemDensity(context);
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = getSystemScaledDensity(context);
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float scale = getSystemScaledDensity(context);
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取app缓存文件路径（临时缓存）
     *
     * @param context
     * @param path
     * @return
     */
    public static String getAppCachePath(Context context, String path) {
//		String path1 = context.getFilesDir().getAbsolutePath();
//		String path2 = context.getCacheDir().getAbsolutePath();
//		String path3 = context.getExternalCacheDir().getAbsolutePath();
//		String path4 = Environment.getExternalStorageDirectory().getAbsolutePath();
//		String path5 = Environment.getDataDirectory().getAbsolutePath();
//		String path6 = Environment.getDownloadCacheDirectory().getAbsolutePath();
//		String path7 = Environment.getRootDirectory().getAbsolutePath();
//
//		LogUtils.v("appCachePath",
//				"---------path1---------" + "\n" + path1 + "\n" +
//				"---------path2---------" + "\n" + path2 + "\n" +
//				"---------path3---------" + "\n" + path3 + "\n" +
//				"---------path4---------" + "\n" + path4 + "\n" +
//				"---------path5---------" + "\n" + path5 + "\n" +
//				"---------path6---------" + "\n" + path6 + "\n" +
//				"---------path7---------" + "\n" + path7 + "\n");
//
//		String configPath = Environment.getExternalStorageState().equals(EnvironmentInfo.MEDIA_MOUNTED) ?
//				context.getExternalCacheDir().getAbsolutePath() + File.separator + path :
//				context.getCacheDir().getAbsolutePath() + File.separator + path;

        String appCachePath = context.getCacheDir().getAbsolutePath();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) &&
                context.getExternalCacheDir() != null) {
            appCachePath = context.getExternalCacheDir().getAbsolutePath();
        }

        if (path != null && !path.isEmpty())
            appCachePath += File.separator + path;

        File dir = new File(appCachePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        LogUtils.i(TAG, "appCachePath:" + appCachePath);
        return appCachePath;
    }

    /**
     * 获取app缓存文件路径（较长时间保存）
     *
     * @param context
     * @param path
     * @return
     */
    public static String getAppFilePath(Context context, String path) {
        String appFilePath = context.getFilesDir().getAbsolutePath();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) &&
                context.getExternalFilesDir(null) != null) {
            appFilePath = context.getExternalFilesDir(null).getAbsolutePath();
        }

        if (path != null && !path.isEmpty()) {
            appFilePath += File.separator + path;
        }

        File dir = new File(appFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        LogUtils.i(TAG, "appFilePath:" + appFilePath);
        return appFilePath;
    }

    /**
     * 获取app包的信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取手机唯一id 独一无二的Psuedo ID
     *
     * @return
     */
    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 获取手机代理信息
     *
     * @param context
     * @return
     */
    public static String getHttpUserAgent(Context context) {
        String userAgentStr = "";
        try {
            JSONObject userAgent = new JSONObject();
            userAgent.put("Project", getPackageInfo(context).packageName);
            userAgent.put("OS", "Android");
            userAgent.put("HardWareInfo", Build.MANUFACTURER + "_" + Build.MODEL + Build.VERSION.RELEASE);
            userAgent.put("VersionName", getPackageInfo(context).versionName);
            userAgent.put("VersionCode", getPackageInfo(context).versionCode);
            userAgent.put("UID", getUniquePsuedoID());

            userAgentStr = userAgent.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userAgentStr;
    }

    /**
     * 改变背景透明度
     *
     * @param bgcolor
     */
    public static void darkenBackground(Activity context, Float bgcolor) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgcolor;

        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     */
    public static void dismissInput(Activity context) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        if (context.getCurrentFocus() != null)
            inputManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 批量删除路径本身及路径下的所有文件
     *
     * @param pPath
     */
    public static void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteDirWithFile(dir);
    }

    /**
     * 递归方式删除指定目录下的所有文件，并删除目录本身
     *
     * @param dir
     */
    public static void deleteDirWithFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWithFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    /**
     * 获取指定文件的大小
     *
     * @param path
     * @return
     */
    public static String getFileSize(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return "0 MB";
        } else {
            long size = f.length();
            return (size / 1024f) / 1024f + "MB";
        }
    }
}
