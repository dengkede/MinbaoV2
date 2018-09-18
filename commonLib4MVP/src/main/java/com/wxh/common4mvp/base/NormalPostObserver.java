package com.wxh.common4mvp.base;

import android.content.Context;

import com.wxh.common4mvp.base.baseImpl.BaseObserverImpl;
import com.wxh.common4mvp.util.ApiException;
import com.wxh.common4mvp.util.ExceptionUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import retrofit2.Response;

public abstract class NormalPostObserver<T> extends BaseObserverImpl<T> {
    public NormalPostObserver(Context mContext, String mActivityName, boolean showBaseLoadingDialog, String... msg) {
        super(mContext, mActivityName, showBaseLoadingDialog, msg);
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        onPostStart();
    }

    @Override
    public void onNext(T t) {
        super.onNext(t);
        if (t instanceof Response) {
            try {
                Response response = (Response) t;
                int code = response.code();
                String message = response.message();
                ResponseBody successBody = (ResponseBody) response.body();
                ResponseBody errorBody = response.errorBody();
                if (response.isSuccessful()) {
                    String resultStr = successBody != null ? successBody.string() : "";
                    onPostSuccess(resultStr);
                } else {
                    String errorStr = errorBody != null ? errorBody.string() : "";
                    JSONObject joErrorBody = new JSONObject(errorStr);
                    if (joErrorBody.has("message")) {
                        message = joErrorBody.getString("message");
                    }

                    ApiException exception = new ApiException(code, message);
                    onError(exception);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            onPostFail(-2, "请求返回格式错误");
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        String errorMsg = ExceptionUtils.handleException(e);
        onPostFail(-1, errorMsg);
    }

    @Override
    public void onComplete() {
        super.onComplete();
    }

    //请求开始回调
    public abstract void onPostStart();

    //请求成功的回调
    public abstract void onPostSuccess(String resultStr);

    //请求你失败回调
    public abstract void onPostFail(int errorCode, String errorMsg);

    //请求进度回调
    public abstract void onPostProgress(int progress, long total);


    public static class UploadFileRequestBody<Q> extends RequestBody {

        private RequestBody mRequestBody;
        private NormalPostObserver<Q> normalPostObserver;

        public UploadFileRequestBody(File file, NormalPostObserver<Q> normalPostObserver) {
            this.mRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            this.normalPostObserver = normalPostObserver;
        }

        @Override
        public MediaType contentType() {
            return mRequestBody.contentType();
        }

        @Override
        public long contentLength() throws IOException {
            return mRequestBody.contentLength();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            CountingSink countingSink = new CountingSink(sink);
            BufferedSink bufferedSink = Okio.buffer(countingSink);
            //写入
            mRequestBody.writeTo(bufferedSink);
            //刷新
            //必须调用flush，否则最后一部分数据可能不会被写入
            bufferedSink.flush();
        }


        protected final class CountingSink extends ForwardingSink {

            private long bytesWritten = 0;

            public CountingSink(Sink delegate) {
                super(delegate);
            }

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);

                bytesWritten += byteCount;
                if (normalPostObserver != null) {
                    normalPostObserver.onPostProgress((int) (bytesWritten * 100 / contentLength()), contentLength());
                }

            }

        }
    }
}
