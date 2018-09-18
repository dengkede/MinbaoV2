package com.wxh.common4mvp.base;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.wxh.common4mvp.util.NetWorkUtils;
import com.wxh.common4mvp.util.StringUtils;
import com.wxh.common4mvp.util.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Retrofit网络请求统一管理帮助类
 */

public class BaseRetrofitHelper {

    //默认网络请求本地缓存空间大小，10M
    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    //默认网络请求连接超时时间，10s
    private static final long HTTP_CONNECT_TIMEOUT = 10000L;
    //默认网络请求读写超时时间，15s
    private static final long HTTP_READ_TIMEOUT = 15000L;
    private static final long HTTP_WRITE_TIMEOUT = 15000L;

    private Context mContext;
    private static BaseRetrofitHelper instance = null;
    private OkHttpClient mOkHttpClient = null;
    private Retrofit mRetrofit = null;
    private Map<String, CompositeDisposable> mDisposableMap;

    public static BaseRetrofitHelper getInstance() {
        if (instance == null) {
            instance = new BaseRetrofitHelper();
        }
        return instance;
    }

    private BaseRetrofitHelper() {
        mDisposableMap = new HashMap<>();
    }

    /**
     * 初始化网络框架，包括HttpClient，Picasso，Retrofit
     *
     * @param context
     * @param baseUrl
     */
    public void init(Context context, String baseUrl) {
        this.mContext = context.getApplicationContext();
        initHttpClient(mContext);
        initPicasso(mContext);
        initRetrofit(baseUrl);
    }

    /**
     * 初始化Retrofit
     *
     * @param baseUrl
     */
    private void initRetrofit(String baseUrl) {
        if (mRetrofit == null) {
            synchronized (BaseRetrofitHelper.class) {
                if (mRetrofit == null) {
                    if (StringUtils.isEmpty(baseUrl))
                        throw new NullPointerException("baseUrl is empty!!!");

                    Retrofit.Builder builder = new Retrofit.Builder();
                    builder.baseUrl(baseUrl);
                    builder.client(getmOkHttpClient());
                    builder.addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()));
                    builder.addConverterFactory(ScalarsConverterFactory.create());
                    builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

                    mRetrofit = builder.build();
                }
            }
        }
    }

    /**
     * 初始化HttpClient
     *
     * @param context
     */
    private void initHttpClient(Context context) {
        if (mOkHttpClient == null) {
            synchronized (BaseRetrofitHelper.class) {
                if (mOkHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();

                    //设置文件缓存
                    String httpCacheDirStr = SystemUtils.getAppCachePath(context, BaseConfig.FLODER_CACHE_HTTP_RESPONSE);
                    if (!StringUtils.isEmpty(httpCacheDirStr)) {
                        File httpCacheDir = new File(httpCacheDirStr);
                        builder.cache(new Cache(httpCacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));
                    }

                    //设置拦截器
                    builder.addInterceptor(new CommonInterceptor(context));
                    builder.addInterceptor(new LoggerInterceptor("", true));

                    //设置超时时间
                    builder.connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
                    builder.readTimeout(0, TimeUnit.MILLISECONDS);
                    builder.writeTimeout(0, TimeUnit.MILLISECONDS);

                    mOkHttpClient = builder.build();
                }
            }
        }
    }

    /**
     * 初始化Picasso图像加载工具
     * 将自定义的httpClient配置到支持OKHttp3.0+的OkHttp3DownLoader中，使得Picasso使用OKHttp3作网络请求
     *
     * @param context
     */
    private void initPicasso(Context context) {
        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3DownLoader(getmOkHttpClient()))
                .build();
        Picasso.setSingletonInstance(picasso);
    }

    public OkHttpClient getmOkHttpClient() {
        return mOkHttpClient;
    }

    public Retrofit getmRetrofit() {
        return mRetrofit;
    }

    public <T> T getRetrofitService(Class<T> cls) {
        return getmRetrofit().create(cls);
    }

    /**
     * 添加 Disposable 对象到订阅管理队列中，用于订阅管理
     *
     * @param key
     * @param subscription
     */
    public void addDisposable(String key, Disposable subscription) {
        if (mDisposableMap.containsKey(key)) {
            CompositeDisposable compositeDisposable = mDisposableMap.get(key);
            compositeDisposable.add(subscription);
        } else {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(subscription);
            mDisposableMap.put(key, compositeDisposable);
        }
    }

    /**
     * 取消订阅指定的Disposable
     *
     * @param key
     */
    public void clearDisposable(String key) {
        if (mDisposableMap.containsKey(key)) {
            CompositeDisposable compositeDisposable = mDisposableMap.get(key);
            compositeDisposable.clear();
            mDisposableMap.remove(key);
        }
    }

    /**
     * 取消订阅所有的Disposable
     */
    public void clearAllDisposable() {
        for (String key : mDisposableMap.keySet()) {
            CompositeDisposable compositeDisposable = mDisposableMap.get(key);
            compositeDisposable.clear();
        }

        mDisposableMap.clear();
    }

    //自定义下载器类
    private class OkHttp3DownLoader implements Downloader {
        OkHttpClient mClient = null;

        public OkHttp3DownLoader(OkHttpClient client) {
            mClient = client;
        }

        @Override
        public Downloader.Response load(Uri uri, int networkPolicy) throws IOException {
            CacheControl.Builder builder = new CacheControl.Builder();
            if (networkPolicy != 0) {
                if (NetworkPolicy.isOfflineOnly(networkPolicy)) {
                    builder.onlyIfCached();
                } else {
                    if (!NetworkPolicy.shouldReadFromDiskCache(networkPolicy)) {
                        builder.noCache();
                    }
                    if (!NetworkPolicy.shouldWriteToDiskCache(networkPolicy)) {
                        builder.noStore();
                    }
                }
            }
            Request request = new Request.Builder()
                    .cacheControl(builder.build())
                    .url(uri.toString())
                    .build();
            okhttp3.Response response = mClient.newCall(request).execute();
            return new Response(response.body().byteStream(), false, response.body().contentLength());
        }

        @Override
        public void shutdown() {
        }
    }

    //自定义拦截器类
    private class CommonInterceptor implements Interceptor {
        private static final String USER_AGENT_HEADER_NAME = "User-Agent";
        private final Context context;

        public CommonInterceptor(Context context) {
            this.context = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.removeHeader(USER_AGENT_HEADER_NAME);
            builder.addHeader(USER_AGENT_HEADER_NAME, SystemUtils.getHttpUserAgent(context));
            if (!NetWorkUtils.isNetworkAvailable(context))
                builder.cacheControl(CacheControl.FORCE_CACHE);
            request = builder.build();

            return chain.proceed(request);
        }
    }

    private class LoggerInterceptor implements Interceptor {

        public static final String TAG = "OkHttp";
        private String tag;
        private boolean showResponse;

        public LoggerInterceptor(String tag, boolean showResponse) {
            if (StringUtils.isEmpty(tag)) {
                tag = TAG;
            }
            this.showResponse = showResponse;
            this.tag = tag;
        }

        public LoggerInterceptor(String tag) {
            this(tag, false);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            logForRequest(request);
            Response response = chain.proceed(request);
            return logForResponse(response);
        }

        private Response logForResponse(Response response) {
            try {
                //===>response log
                Log.e(tag, "========response'log=======");
                Response.Builder builder = response.newBuilder();
                Response clone = builder.build();
                Log.e(tag, "url : " + clone.request().url());
                Log.e(tag, "code : " + clone.code());
                Log.e(tag, "protocol : " + clone.protocol());
                if (!StringUtils.isEmpty(clone.message()))
                    Log.e(tag, "message : " + clone.message());

                if (showResponse) {
                    ResponseBody body = clone.body();
                    if (body != null) {
                        MediaType mediaType = body.contentType();
                        if (mediaType != null) {
                            Log.e(tag, "responseBody's contentType : " + mediaType.toString());
                            if (isText(mediaType)) {
                                String resp = body.string();
                                Log.e(tag, "responseBody's content : " + resp);

                                body = ResponseBody.create(mediaType, resp);
                                return response.newBuilder().body(body).build();
                            } else {
                                Log.e(tag, "responseBody's content : " + " maybe [file part] , too large too print , ignored!");
                            }
                        }
                    }
                }

                Log.e(tag, "========response'log=======end");
            } catch (Exception e) {
//            e.printStackTrace();
            }

            return response;
        }

        private void logForRequest(Request request) {
            try {
                String url = request.url().toString();
                Headers headers = request.headers();

                Log.e(tag, "========request'log=======");
                Log.e(tag, "method : " + request.method());
                Log.e(tag, "url : " + url);
                if (headers != null && headers.size() > 0) {
                    Log.e(tag, "headers : " + headers.toString());
                }
                RequestBody requestBody = request.body();
                if (requestBody != null) {
                    MediaType mediaType = requestBody.contentType();
                    if (mediaType != null) {
                        Log.e(tag, "requestBody's contentType : " + mediaType.toString());
                        if (isText(mediaType)) {
                            Log.e(tag, "requestBody's content : " + bodyToString(request));
                        } else {
                            Log.e(tag, "requestBody's content : " + " maybe [file part] , too large too print , ignored!");
                        }
                    }
                }
                Log.e(tag, "========request'log=======end");
            } catch (Exception e) {
//            e.printStackTrace();
            }
        }

        private boolean isText(MediaType mediaType) {
            if (mediaType.type() != null && mediaType.type().equals("text")) {
                return true;
            }
            if (mediaType.subtype() != null) {
                if (mediaType.subtype().equals("json") ||
                        mediaType.subtype().equals("xml") ||
                        mediaType.subtype().equals("html") ||
                        mediaType.subtype().equals("webviewhtml")
                        )
                    return true;
            }
            return false;
        }

        private String bodyToString(final Request request) {
            try {
                final Request copy = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                copy.body().writeTo(buffer);
                return buffer.readUtf8();
            } catch (final IOException e) {
                return "something error when show requestBody.";
            }
        }
    }
}
