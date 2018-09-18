package com.wxh.common4mvp.base;

import android.content.Context;

import com.wxh.common4mvp.base.baseImpl.BaseObserverImpl;
import com.wxh.common4mvp.util.ApiException;
import com.wxh.common4mvp.util.ExceptionUtils;

import org.json.JSONObject;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Response;

public abstract class NormalGetObserver<T> extends BaseObserverImpl<T> {

    public NormalGetObserver(Context mContext, String mActivityName, boolean showBaseLoadingDialog, String... msg) {
        super(mContext, mActivityName, showBaseLoadingDialog, msg);
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        onGetStart();
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
                    onGetSuccess(resultStr);
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
            onGetFail(-2, "请求返回格式错误");
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        String errorMsg = ExceptionUtils.handleException(e);
        onGetFail(-1, errorMsg);
    }

    @Override
    public void onComplete() {
        super.onComplete();
    }

    //请求开始回调
    public abstract void onGetStart();

    //请求成功回调
    public abstract void onGetSuccess(String resultStr);

    //请求失败回调
    public abstract void onGetFail(int errorCode, String errorMsg);
}
