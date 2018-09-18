package com.hlkj.minbao.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.TestPresenter;
import com.hlkj.minbao.view.ITestView;
import com.wxh.common4mvp.base.baseImpl.BaseActivity;

import butterknife.BindView;

public class TestActivity extends BaseActivity<TestPresenter> implements ITestView {

    @BindView(R.id.btn_click)
    Button btnClick;
    @BindView(R.id.btn_click2)
    Button btnClick2;

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_activity_test;
    }

    @Override
    protected void initToolBar() {
        setToolbarBtnLeft(R.mipmap.btn_common_back);
        setToolbarCenter("测试界面");
    }

    @Override
    public TestPresenter initPresenter() {
        return new TestPresenter(this, mActivityName, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_click:
                mPresenter.actionLogin(TestPresenter.REQUEST_POST_LOGIN);
                break;
            case R.id.btn_click2:
                mPresenter.actionGetInfo(TestPresenter.REQUEST_GET_INFO);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnClick.setOnClickListener(this);
        btnClick2.setOnClickListener(this);
    }
}
