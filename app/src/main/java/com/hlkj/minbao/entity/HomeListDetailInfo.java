package com.hlkj.minbao.entity;

import java.io.Serializable;

public class HomeListDetailInfo implements Serializable {

    /**
     * data : {"address":"运输地址","car":"浙BL1137","companyName":"测试监理公司","projectName":"测试监理公司","order":1,"transportTime":1535130061000}
     * id : 5b7f6ecc2dde29115c58efc9
     * status : 0
     * type : 2
     * use : true
     */

    private Object data;
    private String id;
    private int status;
    private int type;
    private boolean use;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public static class TransportDetailInfo implements Serializable {
        /**
         * address : 运输地址
         * car : 浙BL1137
         * companyName : 测试监理公司
         * projectName : 测试监理公司
         * order : 1
         * transportTime : 1535130061000
         */

        private String address;
        private String car;
        private String companyName;
        private String projectName;
        private int order;
        private long transportTime;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public long getTransportTime() {
            return transportTime;
        }

        public void setTransportTime(long transportTime) {
            this.transportTime = transportTime;
        }
    }
}
