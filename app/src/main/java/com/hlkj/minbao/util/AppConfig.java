package com.hlkj.minbao.util;

import com.wxh.common4mvp.base.BaseConfig;

public class AppConfig extends BaseConfig {

    public static final boolean IS_DES_ENABLED = true;

    //----------------------------------------------------------------------------------------------

    //人脸识别sdklicense
    public static final String licenseID = "minbaoV2-face-android";
    public static final String licenseFileName = "idl-license.face-android";

    //----------------------------------------------------------------------------------------------

    public static final String DOMAIN = "http://192.168.1.82:7000/";

    public static final String URL_POST_USER_LOGIN = "api/v2/app/auth/login";
    public static final String URL_GET_USER_PHOTO = "api/v2/app/auth/{userId}/photo";
    public static final String URL_POST_HOME_LIST = "api/v2/app/deptIndex/user/{userId}";
    public static final String URL_GET_USER_IDENTITY_AND_BASE_DATA = "api/v2/app/dept/{deptType}/deptIndex/user/{userId}/list/{id}/{type}";
    public static final String URL_GET_TRANSPORT_ARRIVE = "api/v2/app/dept/user/{userId}/list/{id}/{type}";

    //----------------------------------------------------------------------------------------------

    public static final String CACHE_USERINFO = "cache_user_info";
    public static final String CACHE_USERNAME = "cache_username";
    public static final String CACHE_USERPWD = "cache_userpwd";
    public static final String CACHE_READ_DOCUMENT = "cache_read_document";
    public static final String CACHE_USER_PHOTO = "cache_user_photo";

    //----------------------------------------------------------------------------------------------

    public static final String FOLDER_USERPHOTO = "App_UserPhotoFile";

    //----------------------------------------------------------------------------------------------

}
