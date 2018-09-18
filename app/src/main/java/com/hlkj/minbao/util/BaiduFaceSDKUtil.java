package com.hlkj.minbao.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class BaiduFaceSDKUtil {

    private static Context mContext;
    public static List<LivenessTypeEnum> livenessList = new ArrayList<LivenessTypeEnum>();
    public static boolean isLivenessRandom = true;

    public static final int REQUEST_FACELIFENESS = 0x00;

    public static void init(Context context) {
        mContext = context;
        FaceSDKManager.getInstance().initialize(context, AppConfig.licenseID, AppConfig.licenseFileName);
        // 根据需求添加活体动作
        livenessList.clear();
        livenessList.add(LivenessTypeEnum.Eye);
//        BaseApplication.livenessList.add(LivenessTypeEnum.Mouth);
//        BaseApplication.livenessList.add(LivenessTypeEnum.HeadUp);
//        BaseApplication.livenessList.add(LivenessTypeEnum.HeadDown);
//        BaseApplication.livenessList.add(LivenessTypeEnum.HeadLeft);
//        BaseApplication.livenessList.add(LivenessTypeEnum.HeadRight);
//        BaseApplication.livenessList.add(LivenessTypeEnum.HeadLeftOrRight);
    }

    public static void startItemActivity(Activity activity, Class itemClass) {
        setFaceConfig();
        Intent intent = new Intent(activity, itemClass);
        activity.startActivityForResult(intent, REQUEST_FACELIFENESS);
    }

    private static void setFaceConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整
        config.setLivenessTypeList(livenessList);
        config.setLivenessRandom(isLivenessRandom);
        config.setBlurnessValue(FaceEnvironment.VALUE_BLURNESS);
        config.setBrightnessValue(FaceEnvironment.VALUE_BRIGHTNESS);
        config.setCropFaceValue(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        config.setHeadPitchValue(FaceEnvironment.VALUE_HEAD_PITCH);
        config.setHeadRollValue(FaceEnvironment.VALUE_HEAD_ROLL);
        config.setHeadYawValue(FaceEnvironment.VALUE_HEAD_YAW);
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        config.setNotFaceValue(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        config.setOcclusionValue(FaceEnvironment.VALUE_OCCLUSION);
        config.setCheckFaceQuality(true);
        config.setLivenessRandomCount(1);
        config.setFaceDecodeNumberOfThreads(2);

        FaceSDKManager.getInstance().setFaceConfig(config);
    }
}
