package com.hlkj.minbao.ui.base;

import com.blankj.utilcode.util.Utils;
import com.hlkj.minbao.util.AppConfig;
import com.hlkj.minbao.util.BaiduFaceSDKUtil;
import com.wxh.common4mvp.base.BaseApplication;
import com.wxh.common4mvp.base.BaseRetrofitHelper;
import com.wxh.common4mvp.util.LogUtils;

public class MyApplication extends BaseApplication {
    @Override
    public void init() {
        super.init();
        //初始化LogUtils
        LogUtils.init("hlkj");
        //百度人脸识别SDK初始化
        BaiduFaceSDKUtil.init(getApplicationContext());
        //初始化公共接口URL
        BaseRetrofitHelper.getInstance().init(getApplicationContext(), AppConfig.DOMAIN);

        //工具包初始化
        Utils.init(this);
    }
}
