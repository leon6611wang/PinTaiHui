package com.zhiyu.quanzhu.model.bean;

import com.qiniu.android.utils.StringUtils;

import java.util.List;

public class OrderDelivery {
    private String status;
    private String status_desc;
    private List<Delivery> data;
    private Delivery newDelivery;
    private String thumb;
    private int oid;

    public String getStatus_desc() {
        if (!StringUtils.isNullOrEmpty(status))
            switch (status) {
                case "0":
                    this.status_desc = "在途";
                    break;
                case "1":
                    this.status_desc = "揽收";
                    break;
                case "2":
                    this.status_desc = "疑难";
                    break;
                case "3":
                    this.status_desc = "签收";
                    break;
                case "4":
                    this.status_desc = "退签";
                    break;
                case "5":
                    this.status_desc = "派件";
                    break;
                case "6":
                    this.status_desc = "退回";
                    break;
            }
        return status_desc;
    }

    public Delivery getNewDelivery() {
        if (null != data && data.size() > 0) {
            newDelivery = data.get(0);
        }
        return newDelivery;
    }

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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }
}
