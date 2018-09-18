package com.hlkj.minbao.entity;

import java.io.Serializable;

public class LoginUserInfo implements Serializable {

    /**
     * companyType : 0
     * deptName : string
     * token : string
     * userId : string
     * userName : string
     * userType : 0
     */

    private int companyType;
    private String deptName;
    private String token;
    private String userId;
    private String userName;
    private int userType;

    public int getCompanyType() {
        return companyType;
    }

    public void setCompanyType(int companyType) {
        this.companyType = companyType;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "LoginUserInfo{" +
                "companyType=" + companyType +
                ", deptName='" + deptName + '\'' +
                ", token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userType=" + userType +
                '}';
    }
}
