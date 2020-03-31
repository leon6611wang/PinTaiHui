package com.zhiyu.quanzhu.model.bean;

public class OrderConfirmAddress {
    private String _id;
    private String name;
    private String phone;
    private String address;
    private int is_def;
    private String province_name;
    private String city_name;

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIs_def() {
        return is_def;
    }

    public void setIs_def(int is_def) {
        this.is_def = is_def;
    }
}
