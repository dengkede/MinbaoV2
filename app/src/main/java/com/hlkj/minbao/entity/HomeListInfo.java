package com.hlkj.minbao.entity;

import java.io.Serializable;

public class HomeListInfo implements Serializable {

    /**
     * address : string
     * id : string
     * name : string
     * order : 0
     * status : 0
     * time : 0
     * type : 0
     */

    private String address;
    private String id;
    private String name;
    private int order;
    private int status;
    private long time;
    private int type;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
