package com.zhiyu.quanzhu.model.bean;

public class QuanZiGuanZhuUser {
    private int id;
    private String username;
    private String avatar;
    private boolean is_all;
    private int fans;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isIs_all() {
        return is_all;
    }

    public void setIs_all(boolean is_all) {
        this.is_all = is_all;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }
}
