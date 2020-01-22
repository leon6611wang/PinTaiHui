package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.User;

public class UserData {
    private int uid;
    private String token;
    private User user;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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
