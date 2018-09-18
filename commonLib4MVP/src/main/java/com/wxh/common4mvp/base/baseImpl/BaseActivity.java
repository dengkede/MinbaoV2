package com.wxh.common4mvp.base.baseImpl;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wxh.common4mvp.R;
import com.wxh.common4mvp.base.baseInterface.IBasePresenter;
import com.wxh.common4mvp.base.baseInterface.IBaseView;
import com.wxh.common4mvp.customView.ClickableImageView;
import com.wxh.common4mvp.util.ActivityManager;
import com.wxh.common4mvp.util.LogUtils;
import com.wxh.common4mvp.util.SystemUtils;

import butterknife.ButterKnife;

public abstract class BaseActivity<P extends IBasePresenter> extends AppCompatActivity implements IBaseView, View.OnClickListener {

    protected P mPresenter;
    private Context mContext;

    protected String mActivityName;

    protected Toolbar mToolbar;
    protected TextView mToolbarTextCenter;
    protected TextView mToolbarTextCenter2;
    protected ClickableImageView mToolbarBtnLeft;
    protected TextView mToolbarTextLeft;
    protected ClickableImageView mToolbarBtnRight;
    protected TextView mToolbarTextRight;

    public View parentView;

//    protected LoadingDialog loadingDialog;

    /**
     * 返回当前布局文件的id
     *
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化标题栏
     */
    protected abstract void initToolBar();

    /**
     * 在子类中初始化对应的presenter
     *
     * @return 相应的presenter
     */
    public abstract P initPresenter();

    protected void onToolBarLeftTextClick() {

    }

    protected void onToolBarLeftBtnClick() {
        SystemUtils.dismissInput(this);
        onBackPressed();
    }

    protected void onToolBarRightTextClick() {

    }

    protected void onToolBarRightBtnClick() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LogUtils.e(mActivityName + "--onBackPressed--");
        ActivityManager.getAppInstance().finishActivity(this);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.btn_toolbar_left) {
            onToolBarLeftBtnClick();
        }
        if (vId == R.id.btn_toolbar_left2) {
            onToolBarLeftTextClick();
        }
        if (vId == R.id.btn_toolbar_right) {
            onToolBarRightBtnClick();
        }
        if (vId == R.id.btn_toolbar_right2) {
            onToolBarRightTextClick();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.e(mActivityName + "--onSaveInstanceState--");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityName = getClass().getSimpleName();
        LogUtils.e(mActivityName + "--onCreate--");

        mContext = this;

        ActivityManager.getAppInstance().addActivity(this);//将当前activity添加进入管理栈

        //全局界面设置
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        parentView = contentFrameLayout.getChildAt(0);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_common);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mToolbarTextCenter = (TextView) findViewById(R.id.txt_toolbar_center);
        mToolbarTextCenter2 = (TextView) findViewById(R.id.txt_toolbar_center2);
        mToolbarBtnRight = (ClickableImageView) findViewById(R.id.btn_toolbar_right);
        mToolbarTextRight = (TextView) findViewById(R.id.btn_toolbar_right2);
        mToolbarBtnLeft = (ClickableImageView) findViewById(R.id.btn_toolbar_left);
        mToolbarTextLeft = (TextView) findViewById(R.id.btn_toolbar_left2);

        setToolbarDefault();
        initToolBar();

        mPresenter = initPresenter();
        if (mPresenter != null)
            mPresenter.fetch();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.e(mActivityName + "--onStart--");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e(mActivityName + "--onStop--");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e(mActivityName + "--onDestroy--");

        if (mPresenter != null) {
            mPresenter.detach();//在presenter中解绑释放view
            mPresenter = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e(mActivityName + "--onPause--");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e(mActivityName + "--onResume--");
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

    @Override
    public void onViewInit() {

    }

    @Override
    public void onViewDestroy() {

    }

    protected void setToolbarDefault() {
        if (mToolbarBtnLeft != null)
            mToolbarBtnLeft.setVisibility(View.GONE);
        if (mToolbarTextLeft != null)
            mToolbarTextLeft.setVisibility(View.GONE);
        if (mToolbarTextCenter != null)
            mToolbarTextCenter.setVisibility(View.VISIBLE);
        if (mToolbarTextCenter2 != null)
            mToolbarTextCenter2.setVisibility(View.GONE);
        if (mToolbarBtnRight != null)
            mToolbarBtnRight.setVisibility(View.GONE);
        if (mToolbarTextRight != null)
            mToolbarTextRight.setVisibility(View.GONE);
    }

    protected void setToolbarCenter(int resId) {
        if (mToolbarTextCenter != null) {
            mToolbarTextCenter.setVisibility(View.VISIBLE);
            mToolbarTextCenter.setText(resId);
        }
    }

    protected void setToolbarCenter(String title) {
        if (mToolbarTextCenter != null) {
            mToolbarTextCenter.setVisibility(View.VISIBLE);
            mToolbarTextCenter.setText(title);
        }
    }

    protected void setToolbarSubCenter(int resId) {
        if (mToolbarTextCenter2 != null) {
            mToolbarTextCenter2.setVisibility(View.VISIBLE);
            mToolbarTextCenter2.setText(resId);
        }
    }

    protected void setToolbarSubCenter(String subtitle) {
        if (mToolbarTextCenter2 != null) {
            mToolbarTextCenter2.setVisibility(View.VISIBLE);
            mToolbarTextCenter2.setText(subtitle);
        }
    }

    protected void setToolbarBtnLeft(int resId) {
        if (mToolbarBtnLeft != null) {
            mToolbarBtnLeft.setVisibility(View.VISIBLE);
            mToolbarBtnLeft.setImageResource(resId);
            mToolbarBtnLeft.setOnClickListener(this);
        }
    }

    protected void setToolbarTextLeft(int resId) {
        if (mToolbarTextLeft != null) {
            mToolbarTextLeft.setVisibility(View.VISIBLE);
            mToolbarTextLeft.setText(resId);
            mToolbarTextLeft.setOnClickListener(this);
        }
    }

    protected void setToolbarTextLeft(String text) {
        if (mToolbarTextLeft != null) {
            mToolbarTextLeft.setVisibility(View.VISIBLE);
            mToolbarTextLeft.setText(text);
            mToolbarTextLeft.setOnClickListener(this);
        }
    }

    protected void setToolbarBtnRight(int resId) {
        if (mToolbarBtnRight != null) {
            mToolbarBtnRight.setVisibility(View.VISIBLE);
            mToolbarBtnRight.setImageResource(resId);
            mToolbarBtnRight.setOnClickListener(this);
        }
    }

    protected void setToolbarTextRight(int resId) {
        if (mToolbarTextRight != null) {
            mToolbarTextRight.setVisibility(View.VISIBLE);
            mToolbarTextRight.setText(resId);
            mToolbarTextRight.setOnClickListener(this);
        }
    }

    protected void setToolbarTextRight(String text) {
        if (mToolbarTextRight != null) {
            mToolbarTextRight.setVisibility(View.VISIBLE);
            mToolbarTextRight.setText(text);
            mToolbarTextRight.setOnClickListener(this);
        }
    }
}
