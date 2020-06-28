package com.leon.chic.model;

public class IMUserResult {
    private int code;
    private String msg;
    private String token;
    private IMUserData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public IMUserData getData() {
        return data;
    }

    public void setData(IMUserData data) {
        this.data = data;
    }
}
