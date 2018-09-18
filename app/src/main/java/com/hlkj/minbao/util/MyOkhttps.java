package com.hlkj.minbao.util;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.hlkj.minbao.entity.LoginUserInfo;
import com.tsy.sdk.myokhttp.body.ProgressRequestBody;
import com.tsy.sdk.myokhttp.body.ResponseProgressBody;
import com.tsy.sdk.myokhttp.response.DownloadResponseHandler;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.tsy.sdk.myokhttp.response.IResponseHandler;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.tsy.sdk.myokhttp.util.LogUtils;
import com.wxh.common4mvp.util.DesHelper;
import com.wxh.common4mvp.util.SPUtils;
import com.wxh.common4mvp.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 封装好的MyOkhttp
 * Created by tsy on 16/8/15.
 */
public class MyOkhttps {

    private OkHttpClient client;
    private static MyOkhttps instance;
    private static String api_version = "500";

    public MyOkhttps() {
        client = new OkHttpClient();
    }

    /**
     * 获取句柄
     *
     * @return
     */
    public static MyOkhttps get() {
        if (instance == null) {
            instance = new MyOkhttps();
        }

        return instance;
    }

    public static void setheaders(String apiversion) {
        // headers.remove("api-version");
        // headers.put("api-version", apiversion);
        api_version = apiversion;
    }

    /**
     * post 请求
     *
     * @param url             url
     * @param params          参数
     * @param responseHandler 回调
     */
    public void post(final String url, final Map<String, String> params, final IResponseHandler responseHandler, boolean needAddHeader) {
        post(null, url, params, responseHandler, needAddHeader);
    }

    private static String getSign(Map<String, String> params) {


        Map<String, String> nameValues = new HashMap<String, String>();
        List<String> names = new ArrayList<String>();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            nameValues.put(entry.getKey(), entry.getValue());
            names.add(entry.getKey());

        }

        Collections.sort(names, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });

        StringBuffer sb = new StringBuffer();
        for (String key : names) {
            if (key.equalsIgnoreCase("sign")) {
                continue;
            }

            sb.append(key).append("=");

            String values = nameValues.get(key);

            sb.append(values);

            sb.append("&");
        }

        sb.append("cd6161d78d1470357052d94086b7e1cb");
        return string2MD5(sb.toString());
    }

    /***
     * MD5加码 生成32位md5码
     */
    public static String string2MD5(String inStr) {
        StringBuffer md5str = null;
        try {
            // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 2 将消息变成byte数组, *一定要设置utf-8
            byte[] input = inStr.getBytes("utf-8");
            // 3 计算后获得字节数组,这就是那128位了
            byte[] buff = md.digest(input);
            // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            md5str = bytesToHex(buff);
            return md5str.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 二进制转十六进制
     *
     * @param bytes
     * @return
     */
    private static StringBuffer bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        // 把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str;
    }

    /**
     * post 请求
     *
     * @param context         发起请求的context
     * @param url             url
     * @param params          参数
     * @param responseHandler 回调
     */
    public void post(Context context, final String url, final Map<String, String> params, final IResponseHandler responseHandler, boolean needAddHeader) {
        //post builder 参数
        FormBody.Builder builder = new FormBody.Builder();
//        String sign = getSign(params);
//
//        params.put("sign", sign);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }

        }

        Request request;

        //发起request
        Request.Builder builder_re = new Request.Builder();
        LoginUserInfo userInfo = (LoginUserInfo) SPUtils.getInstance().readObject(AppConfig.CACHE_USERINFO);
        if (userInfo != null && !StringUtils.isEmpty(userInfo.getToken()) && needAddHeader) {
            builder_re.addHeader("Authorization", "Bearer " + userInfo.getToken());
        }
        if (context == null) {
            request = builder_re
                    .url(url)
                    .post(builder.build())
                    .build();
        } else {
            request = builder_re
                    .url(url)
                    .post(builder.build())
                    .tag(context)
                    .build();
        }
        client.newCall(request).enqueue(new MyCallback(new Handler(), responseHandler));
    }

    /**
     * get 请求
     *
     * @param url             url
     * @param params          参数
     * @param responseHandler 回调
     */
    public void get(final String url, final Map<String, String> params, final IResponseHandler responseHandler, boolean needAddHeader) {
        get(null, url, params, responseHandler, needAddHeader);
    }

    /**
     * get 请求
     *
     * @param context         发起请求的context
     * @param url             url
     * @param params          参数
     * @param responseHandler 回调
     */
    public void get(Context context, final String url, final Map<String, String> params, final IResponseHandler responseHandler, boolean needAddHeader) {
        //拼接url
        String get_url = url;
//        String sign = getSign(params);
//        params.put("sign", sign);
        if (params != null && params.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (i++ == 0) {
                    get_url = get_url + "?" + entry.getKey() + "=" + entry.getValue();
                } else {
                    get_url = get_url + "&" + entry.getKey() + "=" + entry.getValue();
                }
            }
        }

        Request request;

        //发起request
        Request.Builder builder = new Request.Builder().url(get_url);
        LoginUserInfo userInfo = (LoginUserInfo) SPUtils.getInstance().readObject(AppConfig.CACHE_USERINFO);
        if (userInfo != null && !StringUtils.isEmpty(userInfo.getToken()) && needAddHeader) {
            builder.addHeader("Authorization", "Bearer " + userInfo.getToken());
        }
        if (context == null) {
            request = builder.build();
        } else {
            request = builder.build();
        }

        client.newCall(request).enqueue(new MyCallback(new Handler(), responseHandler));
    }

    /**
     * 上传文件
     *
     * @param url             url
     * @param files           上传的文件files
     * @param responseHandler 回调
     */
    public void upload(String url, Map<String, File> files, final IResponseHandler responseHandler, boolean needAddHeader) {
        upload(null, url, null, files, responseHandler, needAddHeader);
    }

    /**
     * 上传文件
     *
     * @param url             url
     * @param params          参数
     * @param files           上传的文件files
     * @param responseHandler 回调
     */
    public void upload(String url, Map<String, String> params, Map<String, File> files, final IResponseHandler responseHandler, boolean needAddHeader) {
        upload(null, url, params, files, responseHandler, needAddHeader);
    }

    /**
     * 上传文件
     *
     * @param context         发起请求的context
     * @param url             url
     * @param files           上传的文件files
     * @param responseHandler 回调
     */
    public void upload(Context context, String url, Map<String, File> files, final IResponseHandler responseHandler, boolean needAddHeader) {
        upload(context, url, null, files, responseHandler, needAddHeader);
    }

    /**
     * 上传文件
     *
     * @param context         发起请求的context
     * @param url             url
     * @param params          参数
     * @param files           上传的文件files
     * @param responseHandler 回调
     */
    public void upload(Context context, String url, Map<String, String> params, Map<String, File> files, final IResponseHandler responseHandler, boolean needAddHeader) {
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //添加参数
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }

        //添加上传文件
        if (files != null && !files.isEmpty()) {
            RequestBody fileBody;
            for (String key : files.keySet()) {
                File file = files.get(key);
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                multipartBuilder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        Request request;
        Request.Builder builder = new Request.Builder();
        LoginUserInfo userInfo = (LoginUserInfo) SPUtils.getInstance().readObject(AppConfig.CACHE_USERINFO);
        if (userInfo != null && !StringUtils.isEmpty(userInfo.getToken()) && needAddHeader) {
            builder.addHeader("Authorization", "Bearer " + userInfo.getToken());
        }
        if (context == null) {
            request = builder
                    .url(url)
                    .post(new ProgressRequestBody(multipartBuilder.build(), responseHandler))
                    .build();
        } else {
            request = builder
                    .url(url)
                    .post(new ProgressRequestBody(multipartBuilder.build(), responseHandler))
                    .tag(context)
                    .build();
        }

        client.newCall(request).enqueue(new MyCallback(new Handler(), responseHandler));
    }

    /**
     * 下载文件
     *
     * @param url                     下载地址
     * @param filedir                 下载目的目录
     * @param filename                下载目的文件名
     * @param downloadResponseHandler 下载回调
     */
    public void download(String url, String filedir, String filename, final DownloadResponseHandler downloadResponseHandler,boolean needAddHeader) {
        download(null, url, filedir, filename, downloadResponseHandler,needAddHeader);
    }

    /**
     * 下载文件
     *
     * @param context                 发起请求的context
     * @param url                     下载地址
     * @param filedir                 下载目的目录
     * @param filename                下载目的文件名
     * @param downloadResponseHandler 下载回调
     */
    public void download(Context context, String url, String filedir, String filename, final DownloadResponseHandler downloadResponseHandler,boolean needAddHeader) {

        Request request;
        Request.Builder builder = new Request.Builder();
        LoginUserInfo userInfo = (LoginUserInfo) SPUtils.getInstance().readObject(AppConfig.CACHE_USERINFO);
        if (userInfo != null && !StringUtils.isEmpty(userInfo.getToken()) && needAddHeader) {
            builder.addHeader("Authorization", "Bearer " + userInfo.getToken());
        }
        if (context == null) {
            request = builder
                    .url(url)
                    .build();
        } else {
            request =builder
                    .url(url)
                    .tag(context)
                    .build();
        }

        client.newBuilder()
                .addNetworkInterceptor(new Interceptor() {      //设置拦截器
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ResponseProgressBody(originalResponse.body(), downloadResponseHandler))
                                .build();
                    }
                })
                .build()
                .newCall(request)
                .enqueue(new MyDownloadCallback(new Handler(), downloadResponseHandler, filedir, filename));
    }

    /**
     * 取消当前context的所有请求
     *
     * @param context
     */
    public void cancel(Context context) {
        if (client != null) {
            for (Call call : client.dispatcher().queuedCalls()) {
                if (call.request().tag().equals(context))
                    call.cancel();
            }
            for (Call call : client.dispatcher().runningCalls()) {
                if (call.request().tag().equals(context))
                    call.cancel();
            }
        }
    }

    //下载回调
    private class MyDownloadCallback implements Callback {

        private Handler mHandler;
        private DownloadResponseHandler mDownloadResponseHandler;
        private String mFileDir;
        private String mFilename;

        public MyDownloadCallback(Handler handler, DownloadResponseHandler downloadResponseHandler,
                                  String filedir, String filename) {
            mHandler = handler;
            mDownloadResponseHandler = downloadResponseHandler;
            mFileDir = filedir;
            mFilename = filename;
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            LogUtils.e("onFailure", e);

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDownloadResponseHandler.onFailure(e.toString());
                }
            });
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            if (response.isSuccessful()) {
                File file = null;
                try {
                    file = saveFile(response, mFileDir, mFilename);
                } catch (final IOException e) {
                    LogUtils.e("onResponse saveFile fail", e);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mDownloadResponseHandler.onFailure("onResponse saveFile fail." + e.toString());
                        }
                    });
                }

                final File newFile = file;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadResponseHandler.onFinish(newFile);
                    }
                });
            } else {
                LogUtils.e("onResponse fail status=" + response.code());

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadResponseHandler.onFailure("fail status=" + response.code());
                    }
                });
            }
        }
    }

    //callback
    private class MyCallback implements Callback {

        private Handler mHandler;
        private IResponseHandler mResponseHandler;

        public MyCallback(Handler handler, IResponseHandler responseHandler) {
            mHandler = handler;
            mResponseHandler = responseHandler;
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            LogUtils.e("onFailure", e);

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mResponseHandler.onFailure(0, e.toString());
                }
            });
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            if (response.isSuccessful()) {
                final String response_body = response.body().string();

                if (mResponseHandler instanceof JsonResponseHandler) {       //json回调
                    try {
                        final JSONObject jsonBody = new JSONObject(response_body);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((JsonResponseHandler) mResponseHandler).onSuccess(response.code(), jsonBody);
//                                try {
//                                    if(jsonBody.getString("status").equals("2")){
//
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
                            }
                        });
                    } catch (JSONException e) {
                        LogUtils.e("onResponse fail parse jsonobject, body=" + response_body);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mResponseHandler.onFailure(response.code(), "fail parse jsonobject, body=" + response_body);
                            }
                        });
                    }
                } else if (mResponseHandler instanceof GsonResponseHandler) {    //gson回调
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Gson gson = new Gson();
                                ((GsonResponseHandler) mResponseHandler).onSuccess(response.code(),
                                        gson.fromJson(response_body, ((GsonResponseHandler) mResponseHandler).getType()));
                            } catch (Exception e) {
                                LogUtils.e("onResponse fail parse gson, body=" + response_body, e);
                                mResponseHandler.onFailure(response.code(), "fail parse gson, body=" + response_body);
                            }

                        }
                    });
                } else if (mResponseHandler instanceof RawResponseHandler) {     //raw字符串回调
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ((RawResponseHandler) mResponseHandler).onSuccess(response.code(), response_body);
                        }
                    });
                }
            } else {
                LogUtils.e("onResponse fail status=" + response.code());

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mResponseHandler.onFailure(0, "fail status=" + response.code());
                    }
                });
            }
        }
    }

    //保存文件
    private File saveFile(Response response, String filedir, String filename) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            File dir = new File(filedir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, filename);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return file;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }

    //获取mime type
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
