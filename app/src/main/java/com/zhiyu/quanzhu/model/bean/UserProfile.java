package com.zhiyu.quanzhu.model.bean;

/**
 * 用户资料/信息
 */
public class UserProfile {
    private int id;
    private String username;
    private String avatar;
    //性别 0未知 1男 2女
    private int sex;
    private String sex_desc;
    private int province_id;
    private String province_name;
    private int city;
    private String city_name;
    private String industry;
    //认证状态 0未认证 1审核中 2审核不通过 3已认证
    private int is_rz;
    private String vertify_desc;

    public String getSex_desc() {
        return sex_desc;
    }

    public String getVertify_desc() {
        return vertify_desc;
    }

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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
        switch (sex) {
            case 0:
                this.sex_desc = "未选择";
                break;
            case 1:
                this.sex_desc = "男";
                break;
            case 2:
                this.sex_desc = "女";
                break;
        }
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getIs_rz() {
        return is_rz;
    }

    public void setIs_rz(int is_rz) {
        this.is_rz = is_rz;
        switch (this.is_rz) {
            case 0:
                this.vertify_desc = "未认证";
                break;
            case 1:
                this.vertify_desc = "审核中";
                break;
            case 2:
                this.vertify_desc = "审核不通过";
                break;
            case 3:
                this.vertify_desc = "已认证";
                break;
        }
    }
}
