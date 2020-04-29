package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class DeliveryInfo {
    private String status;
    private List<Delivery> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Delivery> getData() {
        return data;
    }

    public void setData(List<Delivery> data) {
        this.data = data;
    }
}
