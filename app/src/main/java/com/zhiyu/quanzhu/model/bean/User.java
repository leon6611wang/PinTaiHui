package com.zhiyu.quanzhu.model.bean;

/**
 * 基本信息
 */
public class User {
    private String truename;
    private String company;
    private String occupation;
    private String provice;
    private String city;
    private String district;
    //基础信息是否完善
    private int infostatus;
    //密码是否设置
    private int pwdstatus;
    //手机号是否绑定
    private int mobilestatus;
    //个人偏好是否完善
    private int industrystatus;

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getInfostatus() {
        return infostatus;
    }

    public void setInfostatus(int infostatus) {
        this.infostatus = infostatus;
    }

    public int getPwdstatus() {
        return pwdstatus;
    }

    public void setPwdstatus(int pwdstatus) {
        this.pwdstatus = pwdstatus;
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
}
