package com.wxh.common4mvp.base;

import com.wxh.common4mvp.base.baseInterface.IBaseRetrofitService;
import com.wxh.common4mvp.util.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class BaseServerManager {

    /**
     * 下载单文件，该方法不支持断点下载
     *
     * @param url                  下载请求URL
     * @param destDir              存储文件夹
     * @param fileName             存储文件名
     * @param downLoadHttpCallBack 下载监听回调
     */
    public static void downLoadFile(String url,
                                    final String destDir,
                                    final String fileName,
                                    final DownLoadHttpCallBack downLoadHttpCallBack) {
        final FileDownLoadObserver<File> downLoadObserver = new FileDownLoadObserver<File>(
                downLoadHttpCallBack.getmContext(),
                downLoadHttpCallBack.getmActivityName(),
                downLoadHttpCallBack.ismIsShowBaseLoadingDialog(),
                downLoadHttpCallBack.getmLoadingDialogMsg()) {
            @Override
            public void onDownLoadStart() {
                downLoadHttpCallBack.onDownLoadStart();
            }

            @Override
            public void onDownLoadSuccess(File file) {
                downLoadHttpCallBack.onDownLoadSuccess(file);
            }

            @Override
            public void onDownLoadFail(Throwable throwable) {
                downLoadHttpCallBack.onDownLoadFail(throwable);
            }

            @Override
            public void onDownLoadProgress(int progress, long total) {
                downLoadHttpCallBack.onDownLoadProgress(progress, total);
            }
        };
        BaseRetrofitHelper.getInstance().getRetrofitService(IBaseRetrofitService.class)
                .downLoadFile(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())//subscribeOn和ObserOn必须在io线程，如果在主线程会出错
                .observeOn(Schedulers.computation())
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        return downLoadObserver.saveFile(responseBody, destDir, fileName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(downLoadObserver);
    }

    /**
     * GET形式获取数据（常规数据）
     *
     * @param url                请求URL
     * @param paths              请求path参数集合
     * @param headers            请求头信息
     * @param params             请求query参数集合
     * @param normalHttpCallback 请求监听回调
     */
    public static void get(String url,
                           Map<String, String> paths,
                           Map<String, String> headers,
                           Map<String, String> params,
                           final NormalHttpCallback normalHttpCallback) {

        NormalGetObserver<Response<ResponseBody>> normalGetObserver = new NormalGetObserver<Response<ResponseBody>>(
                normalHttpCallback.getmContext(),
                normalHttpCallback.getmActivityName(),
                normalHttpCallback.ismIsShowBaseLoadingDialog(),
                normalHttpCallback.getmLoadingDialogMsg()) {
            @Override
            public void onGetStart() {
                normalHttpCallback.onHttpStart();
            }

            @Override
            public void onGetSuccess(String resultStr) {
                normalHttpCallback.onHttpSuccess(resultStr);
            }

            @Override
            public void onGetFail(int errorCode, String errorMsg) {
                normalHttpCallback.onHttpFail(errorCode, errorMsg);
            }
        };

        String formatUrl = getFormatURL(url, paths);
//        Map<String, RequestBody> paramMap = new HashMap<>();
//        //组装数据
//        if (params != null && params.size() > 0) {
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                String key = entry.getKey();
//                String value = entry.getValue();
//
//                RequestBody requestBody = RequestBody.create(null, value);
//                paramMap.put(key, requestBody);
//            }
//        }

        Map<String, String> paramMap = new HashMap<>();
        if (params != null && params.size() > 0)
            paramMap.putAll(params);

        BaseRetrofitHelper.getInstance().getRetrofitService(IBaseRetrofitService.class)
                .get(formatUrl, headers, paramMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(normalGetObserver);
    }

    /**
     * POST形式提交数据（包含上传文件，上传普通表单参数）
     *
     * @param url                请求URL
     * @param paths              请求path参数集合
     * @param headers            请求头信息
     * @param files              待上传文件集合
     * @param params             请求query参数集合
     * @param normalHttpCallback 请求监听回调
     */
    public static void post(String url,
                            Map<String, String> paths,
                            Map<String, String> headers,
                            Map<String, File> files,
                            Map<String, String> params,
                            final NormalHttpCallback normalHttpCallback) {
        NormalPostObserver<Response<ResponseBody>> normalPostObserver = new NormalPostObserver<Response<ResponseBody>>(
                normalHttpCallback.getmContext(),
                normalHttpCallback.getmActivityName(),
                normalHttpCallback.ismIsShowBaseLoadingDialog(),
                normalHttpCallback.getmLoadingDialogMsg()) {
            @Override
            public void onPostStart() {
                normalHttpCallback.onHttpStart();
            }

            @Override
            public void onPostSuccess(String resultStr) {
                normalHttpCallback.onHttpSuccess(resultStr);
            }

            @Override
            public void onPostFail(int errorCode, String errorMsg) {
                normalHttpCallback.onHttpFail(errorCode, errorMsg);
            }

            @Override
            public void onPostProgress(int progress, long total) {
                normalHttpCallback.onHttpProgress(progress, total);
            }
        };

        String formatUrl = getFormatURL(url, paths);
        Map<String, RequestBody> fileMap = new HashMap<>();
        Map<String, RequestBody> paramMap = new HashMap<>();
        //组装文件
        if (files != null && files.size() > 0) {
            for (Map.Entry<String, File> entry : files.entrySet()) {
                String key = entry.getKey();
                File file = entry.getValue();
                NormalPostObserver.UploadFileRequestBody<Response<ResponseBody>> uploadFileRequestBody =
                        new NormalPostObserver.UploadFileRequestBody<>(file, normalPostObserver);
                fileMap.put(key, uploadFileRequestBody);
            }
        }
        //组装query参数
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                RequestBody requestBody = RequestBody.create(null, value);
                paramMap.put(key, requestBody);
            }
        }

        BaseRetrofitHelper.getInstance().getRetrofitService(IBaseRetrofitService.class)
                .post(formatUrl, headers, fileMap, paramMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(normalPostObserver);
    }

    /**
     * POST形式提交body数据
     *
     * @param url                请求URL
     * @param paths              请求path参数集合
     * @param headers            请求头信息
     * @param bodyStr            请求body
     * @param normalHttpCallback 请求监听回调
     */
    public static void postString(String url,
                                  Map<String, String> paths,
                                  Map<String, String> headers,
                                  String bodyStr,
                                  final NormalHttpCallback normalHttpCallback) {
        NormalPostObserver<Response<ResponseBody>> normalPostObserver = new NormalPostObserver<Response<ResponseBody>>(
                normalHttpCallback.getmContext(),
                normalHttpCallback.getmActivityName(),
                normalHttpCallback.ismIsShowBaseLoadingDialog(),
                normalHttpCallback.getmLoadingDialogMsg()) {
            @Override
            public void onPostStart() {
                normalHttpCallback.onHttpStart();
            }

            @Override
            public void onPostSuccess(String resultStr) {
                normalHttpCallback.onHttpSuccess(resultStr);
            }

            @Override
            public void onPostFail(int errorCode, String errorMsg) {
                normalHttpCallback.onHttpFail(errorCode, errorMsg);
            }

            @Override
            public void onPostProgress(int progress, long total) {
                normalHttpCallback.onHttpProgress(progress, total);
            }
        };

        String formatUrl = getFormatURL(url, paths);
        RequestBody body = null;

        if (!StringUtils.isEmpty(bodyStr)) {
            body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyStr);
        }

        BaseRetrofitHelper.getInstance().getRetrofitService(IBaseRetrofitService.class)
                .postString(formatUrl, headers, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(normalPostObserver);
    }

    /**
     * 获取完整的URL地址
     *
     * @param url
     * @param pathDataMap
     * @return
     */
    private static String getFormatURL(String url, Map<String, String> pathDataMap) {
        if (StringUtils.isEmpty(url))
            return null;
        if (pathDataMap == null || pathDataMap.isEmpty())
            return url;

        String formatUrl = url;
        for (Map.Entry<String, String> entry : pathDataMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String replaceStr = "{" + key + "}";
            if (formatUrl.contains(replaceStr)) {
                formatUrl = formatUrl.replace(replaceStr, value);
            }
        }

        return formatUrl;
    }
}
