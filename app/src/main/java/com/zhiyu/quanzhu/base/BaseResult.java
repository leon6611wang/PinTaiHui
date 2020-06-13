package com.zhiyu.quanzhu.base;

import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;

public class BaseResult {
    private int code;
    private String msg;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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
}
