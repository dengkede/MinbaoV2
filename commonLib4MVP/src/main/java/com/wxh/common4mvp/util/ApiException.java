package com.wxh.common4mvp.util;

/**
 * 服务器返回错误信息封装
 */
public class ApiException extends Exception {

    private int code;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}