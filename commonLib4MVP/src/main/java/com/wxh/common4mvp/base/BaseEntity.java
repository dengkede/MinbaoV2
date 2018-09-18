package com.wxh.common4mvp.base;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 实体类基类，接收code、message以及泛型data
 */
public class BaseEntity<T> implements Serializable {

    @SerializedName("statusCode")
    private int resultCode;
    @SerializedName("resultMsg")
    private String resultMsg;
    @SerializedName("resultData")
    private T resultData;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public T getResultData() {
        return resultData;
    }

    public void setResultData(T resultData) {
        this.resultData = resultData;
    }
}
