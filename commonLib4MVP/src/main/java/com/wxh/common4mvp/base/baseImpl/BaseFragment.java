package com.wxh.common4mvp.base.baseImpl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxh.common4mvp.base.baseInterface.IBasePresenter;
import com.wxh.common4mvp.base.baseInterface.IBaseView;
import com.wxh.common4mvp.util.LogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<P extends IBasePresenter> extends Fragment implements IBaseView, View.OnClickListener {

    protected P mPresenter;
    public Context mContext;
    public View parentView;
    private Unbinder unbinder;

    protected String mFragmentName;//fragment名称
    private boolean isViewCreate = false;//view是否创建
    private boolean isViewVisiable = false;//view是否可见
    public boolean isViewVisiableFirst = true;//view是否第一次对用户可见

    /**
     * 在子类中初始化对应的presenter
     *
     * @return 相应的presenter
     */
    public abstract P initPresenter();

    /**
     * 返回当前布局文件的id
     *
     * @return
     */
    public abstract int getLayoutResId();

    /**
     * fragment可见（切换回来或者onResume）
     */
    public abstract void onUserVisible();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e(mFragmentName + "--onCreate--");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.e(mFragmentName + "--onActivityCreated--");
        isViewCreate = true;
        mFragmentName = getClass().getSimpleName();
        mPresenter = initPresenter();
        if (mPresenter != null)
            mPresenter.fetch();
        if (isViewVisiable)
            onUserVisible();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.e(mFragmentName + "--onAttach--");
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), null);
        parentView = view;
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e(mFragmentName + "--onResume--");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.e(mFragmentName + "--onStart--");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.e(mFragmentName + "--onStop--");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.e(mFragmentName + "--onPause--");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e(mFragmentName + "--onDestroy--");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.e(mFragmentName + "--onDestroyView--");
        isViewCreate = false;
        isViewVisiableFirst = true;
        unbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e(mFragmentName + "--setUserVisibleHint--" + isVisibleToUser);
        isViewVisiable = isVisibleToUser;
        if (isViewVisiable && isViewCreate) {
            onUserVisible();
            isViewVisiableFirst = false;
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onViewInit() {

    }

    @Override
    public void onViewDestroy() {

    }

    @Override
    public void onServerStart(int whichRequest) {

    }

    @Override
    public void onServerProcess(int whichRequest, float progress, long total) {

    }

    @Override
    public void onServerSuccess(int whichRequest, String result) {

    }

    @Override
    public void onServerError(int whichRequest, int errorCode, String errorMsg) {

    }
}
