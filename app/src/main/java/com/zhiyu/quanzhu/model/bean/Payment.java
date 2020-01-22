package com.zhiyu.quanzhu.model.bean;

public class Payment {
    private String app_id;
    private long created_time;
    private String description;
    private PayInfo expend;

    private String id;
    private String object;
    private String order_no;
    private String pay_amt;
    private String pay_channel;
    private String prod_mode;
    private String query_url;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getPay_amt() {
        return pay_amt;
    }

    public void setPay_amt(String pay_amt) {
        this.pay_amt = pay_amt;
    }

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public String getProd_mode() {
        return prod_mode;
    }

    public void setProd_mode(String prod_mode) {
        this.prod_mode = prod_mode;
    }

    public String getQuery_url() {
        return query_url;
    }

    public void setQuery_url(String query_url) {
        this.query_url = query_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public long getCreated_time() {
        return created_time;
    }

    public void setCreated_time(long created_time) {
        this.created_time = created_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PayInfo getExpend() {
        return expend;
    }

    public void setExpend(PayInfo expend) {
        this.expend = expend;
    }
}
