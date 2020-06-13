package com.zhiyu.quanzhu.model.bean;

public class UserInfo {
    private int infostatus;
    private int passwordstatus;
    private int mobilestatus;
    private int industrystatus;
    private String avatar;
    private String username;
    private boolean fill_profile;
    private boolean has_pwd;
    private boolean bind_mobile;
    private boolean choose_hobby;


    public int getInfostatus() {
        return infostatus;
    }

    public void setInfostatus(int infostatus) {
        this.infostatus = infostatus;
    }

    public int getPasswordstatus() {
        return passwordstatus;
    }

    public void setPasswordstatus(int passwordstatus) {
        this.passwordstatus = passwordstatus;
    }

    public int getMobilestatus() {
        return mobilestatus;
    }

    public void setMobilestatus(int mobilestatus) {
        this.mobilestatus = mobilestatus;
    }

    public int getIndustrystatus() {
        return industrystatus;
    }

    public void setIndustrystatus(int industrystatus) {
        this.industrystatus = industrystatus;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFill_profile() {
        return infostatus==1?true:false;
    }

    public void setFill_profile(boolean fill_profile) {
        this.fill_profile = fill_profile;
    }

    public boolean isHas_pwd() {
        return passwordstatus==1?true:false;
    }

    public void setHas_pwd(boolean has_pwd) {
        this.has_pwd = has_pwd;
    }

    public boolean isBind_mobile() {
        return mobilestatus==1?true:false;
    }

    public void setBind_mobile(boolean bind_mobile) {
        this.bind_mobile = bind_mobile;
    }

    public boolean isChoose_hobby() {
        return industrystatus==1?true:false;
    }

    public void setChoose_hobby(boolean choose_hobby) {
        this.choose_hobby = choose_hobby;
    }
}
