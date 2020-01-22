package com.zhiyu.quanzhu.model.bean;

public class BikeShop {

    private long id;
    private String shop_name;
    private String shop_address;
    private String shopkeeper_name;
    private String shopkeeper_phone_number;
    private float longitude, latitude;
    private long create_time;
    private float salesValume;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getShopkeeper_name() {
        return shopkeeper_name;
    }

    public void setShopkeeper_name(String shopkeeper_name) {
        this.shopkeeper_name = shopkeeper_name;
    }

    public String getShopkeeper_phone_number() {
        return shopkeeper_phone_number;
    }

    public void setShopkeeper_phone_number(String shopkeeper_phone_number) {
        this.shopkeeper_phone_number = shopkeeper_phone_number;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public float getSalesValume() {
        return salesValume;
    }

    public void setSalesValume(float salesValume) {
        this.salesValume = salesValume;
    }
}
