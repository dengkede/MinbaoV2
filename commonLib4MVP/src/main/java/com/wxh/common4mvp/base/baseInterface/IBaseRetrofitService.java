package com.wxh.common4mvp.base.baseInterface;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface IBaseRetrofitService {

    /**
     * 下载文件
     *
     * @param fileUrl 文件下载URL
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> downLoadFile(@NonNull @Url String fileUrl);

    /**
     * GET形式获取数据（常规数据）
     *
     * @param headers 请求头信息（动态添加）
     * @param url     请求URL
     * @param params  请求参数集合
     * @return
     */
    @GET
    Observable<Response<ResponseBody>> get(@NonNull @Url String url,
                                           @HeaderMap Map<String, String> headers,
                                           @QueryMap Map<String, String> params);

    /**
     * POST形式上传（包含上传文件以及上传普通表单参数）
     *
     * @param headers 请求头信息（动态添加）
     * @param url     请求URL
     * @param files   待上传文件集合
     * @param params  请求query参数集合
     * @return
     */
    @Multipart
    @POST
    Observable<Response<ResponseBody>> post(@NonNull @Url String url,
                                            @HeaderMap Map<String, String> headers,
                                            @PartMap Map<String, RequestBody> files,
                                            @PartMap Map<String, RequestBody> params);

    /**
     * POST形式上传body
     *
     * @param url     请求URL
     * @param headers 请求头信息（动态添加）
     * @param body    请求body
     * @return
     */
    @POST
    Observable<Response<ResponseBody>> postString(@NonNull @Url String url,
                                                  @HeaderMap Map<String, String> headers,
                                                  @Body RequestBody body);
}
