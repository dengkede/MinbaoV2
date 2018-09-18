package com.wxh.common4mvp.base.baseInterface;

public interface IBaseView {

    /**
     * 初始化UI回调
     */
    void onViewInit();

    /**
     * 销毁UI回调
     */
    void onViewDestroy();

    /**
     * 请求开始回调
     *
     * @param whichRequest
     */
    void onServerStart(int whichRequest);

    /**
     * 请求过程回调
     *
     * @param whichRequest
     * @param progress
     * @param total
     */
    void onServerProcess(int whichRequest, float progress, long total);

    /**
     * 请求成功回调
     *
     * @param whichRequest
     * @param result
     */
    void onServerSuccess(int whichRequest, String result);

    /**
     * 请求失败回调
     *
     * @param whichRequest
     * @param errorCode
     * @param errorMsg
     */
    void onServerError(int whichRequest, int errorCode, String errorMsg);
}
