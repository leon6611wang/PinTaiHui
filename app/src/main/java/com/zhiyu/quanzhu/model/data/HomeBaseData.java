package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.User;

public class HomeBaseData {
    private AppVersionData version;
    private int uid;
    private boolean update_industry;
    private boolean update_city;
    private String token;
    private User user;
    private String business_url;
    private boolean is_rz;
    private int card_id;
    private String mobile;

    public boolean isIs_rz() {
        return is_rz;
    }

    public void setIs_rz(boolean is_rz) {
        this.is_rz = is_rz;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBusiness_url() {
        return business_url;
    }

    public void setBusiness_url(String business_url) {
        this.business_url = business_url;
    }

    public AppVersionData getVersion() {
        return version;
    }

    public void setVersion(AppVersionData version) {
        this.version = version;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public boolean isUpdate_industry() {
        return update_industry;
    }

    public void setUpdate_industry(boolean update_industry) {
        this.update_industry = update_industry;
    }

    public boolean isUpdate_city() {
        return update_city;
    }

    public void setUpdate_city(boolean update_city) {
        this.update_city = update_city;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
