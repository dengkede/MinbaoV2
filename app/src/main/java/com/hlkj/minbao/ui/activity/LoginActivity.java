package com.hlkj.minbao.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hlkj.minbao.R;
import com.hlkj.minbao.entity.LoginUserInfo;
import com.hlkj.minbao.presenter.LoginPresenter;
import com.hlkj.minbao.util.AppConfig;
import com.hlkj.minbao.util.BaiduFaceSDKUtil;
import com.hlkj.minbao.view.ILoginView;
import com.wxh.common4mvp.base.baseImpl.BaseActivity;
import com.wxh.common4mvp.util.ActivityManager;
import com.wxh.common4mvp.util.PermissionUtils;
import com.wxh.common4mvp.util.SPUtils;
import com.wxh.common4mvp.util.StringUtils;

import java.util.Calendar;

import butterknife.BindView;

public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginView {

    @BindView(R.id.edit_username)
    EditText editUsername;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.check_read)
    CheckBox checkRead;
    @BindView(R.id.txt_bottom_copyright)
    TextView txtBottomCopyright;

    private String mUserName;
    private String mUserPassword;
    private boolean mIsReadDocument;

    private LoginUserInfo mLoginUserInfo;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter(this, mActivityName, this);
    }

    @Override
    public void onBackPressed() {
        mPresenter.onLoginBackPressed(this);
    }

    @Override
    public void onViewInit() {
        super.onViewInit();
        String copyright = getResources().getString(R.string.login_company_copyright);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String copyrightFinal = String.format(copyright, String.valueOf(year));
        txtBottomCopyright.setText(copyrightFinal);

        mUserName = (String) SPUtils.getInstance().get(AppConfig.CACHE_USERNAME, "");
        mUserPassword = (String) SPUtils.getInstance().get(AppConfig.CACHE_USERPWD, "");
        mIsReadDocument = (boolean) SPUtils.getInstance().get(AppConfig.CACHE_READ_DOCUMENT, true);

        if (!StringUtils.isEmpty(mUserName) && !StringUtils.isEmpty(mUserPassword)) {
            editUsername.setText(mUserName);
            editPassword.setText(mUserPassword);

            editPassword.setFocusable(true);
            editPassword.setFocusableInTouchMode(true);
            editPassword.setSelection(editPassword.getText().toString().length());
            editPassword.requestFocus();
        } else {
            editUsername.setFocusable(true);
            editUsername.setFocusableInTouchMode(true);
            editUsername.setSelection(editUsername.getText().toString().length());
            editUsername.requestFocus();
        }

        if (mIsReadDocument) {
            checkRead.setChecked(true);
        }
        checkRead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsReadDocument = isChecked;
                SPUtils.getInstance().put(AppConfig.CACHE_READ_DOCUMENT, mIsReadDocument);
        }
        });

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BaiduFaceSDKUtil.REQUEST_FACELIFENESS) {
                if (data != null) {
                    boolean success = data.getBooleanExtra("success", false);
                    if (success) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        ActivityManager.getAppInstance().finishActivity(this);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_login:
                mUserName = editUsername.getText().toString().trim();
                mUserPassword = editPassword.getText().toString().trim();
                if (mPresenter.checkInput(mUserName, mUserPassword, mIsReadDocument)) {
                    mPresenter.actionLogin(LoginPresenter.REQUEST_POST_USER_LOGIN, mUserName, mUserPassword);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onServerSuccess(int whichRequest, String result) {
        super.onServerSuccess(whichRequest, result);
        switch (whichRequest) {
            case LoginPresenter.REQUEST_POST_USER_LOGIN://登录成功回调
                //缓存账号密码
                SPUtils.getInstance().put(AppConfig.CACHE_USERNAME, mUserName);
                SPUtils.getInstance().put(AppConfig.CACHE_USERPWD, mUserPassword);
                if (!StringUtils.isEmpty(result)) {
                    mLoginUserInfo = new Gson().fromJson(result, LoginUserInfo.class);
                    if (mLoginUserInfo != null) {
                        //缓存用户信息
                        SPUtils.getInstance().saveObject(mLoginUserInfo, AppConfig.CACHE_USERINFO);
                        //获取用户照片
                        mPresenter.actionGetUserPhoto(LoginPresenter.REQUEST_GET_USER_PHOTO, mLoginUserInfo.getUserId());
                    }
                }
                break;
            case LoginPresenter.REQUEST_GET_USER_PHOTO://获取人员照片成功回调
                if (!StringUtils.isEmpty(result) && mLoginUserInfo != null) {
                    //下载人员照片并存储到本地
                    mPresenter.actionDownLoadUserPhoto(LoginPresenter.REQUEST_DOWNLOAD_USER_PHOTO, mLoginUserInfo.getUserId(), result);
                }
                break;
            case LoginPresenter.REQUEST_DOWNLOAD_USER_PHOTO:
                if (!StringUtils.isEmpty(result)) {
//                    PermissionUtils.requestPermissionsResult(this,
//                            1,
//                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
//                            new PermissionUtils.OnPermissionListener() {
//                                @Override
//                                public void onPermissionGranted() {
//                                    //跳转到人脸识别界面进行人脸识别
//                                    BaiduFaceSDKUtil.startItemActivity(LoginActivity.this, FaceLivenessExpActivity.class);
//                                }
//
//                                @Override
//                                public void onPermissionDenied() {
//                                    PermissionUtils.showTipsDialog(LoginActivity.this);
//                                }
//                            });
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    ActivityManager.getAppInstance().finishActivity(this);
                }
                break;
            default:
                break;
        }
    }
}
