package com.zhiyu.quanzhu.model.bean;

/**
 * 用户绑定信息
 */
public class BindInfo {
    private boolean has_qq;
    private boolean has_wx;
    private String phone;

    public boolean isHas_qq() {
        return has_qq;
    }

    public void setHas_qq(boolean has_qq) {
        this.has_qq = has_qq;
    }

    public boolean isHas_wx() {
        return has_wx;
    }

    public void setHas_wx(boolean has_wx) {
        this.has_wx = has_wx;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
