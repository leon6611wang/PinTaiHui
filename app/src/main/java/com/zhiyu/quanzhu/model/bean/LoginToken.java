package com.zhiyu.quanzhu.model.bean;

/**
 * 用户登录token
 */
public class LoginToken {
    private long expires;
    private String token;

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
