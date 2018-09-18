package com.wxh.common4mvp.base.baseInterface;

public interface IBasePresenter<T> {

    /**
     * 默认初始化(onCreate()调用)
     */
    void fetch();

    /**
     * 默认销毁(onDestroy()调用)
     */
    void detach();
}
