package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class OrderInformation {
    private int id;
    private String order_no;
    private String remark;
    private int pay_type;//1表示支付宝 2表示微信 3表示余额（支付宝）4表示余额（微信）
    private String pay_type_desc;
    private String addtime;
    private String paytime;
    private String address_name;
    private String address_phone;
    private String address;
    private int status;
    private String status_desc;
    private int shop_id;
    private String shop_name;
    private String shop_icon;
    private int num;
    private long paymoney;//支付订单金额
    private long discount_price;//折扣
    private long feight_price;//邮费
    private List<OrderInformationGoods> goods;


    public String getPay_type_desc() {
        return pay_type_desc;
    }

    public String getStatus_desc() {
        return status_desc;
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }

    public void setPay_type_desc(String pay_type_desc) {
        this.pay_type_desc = pay_type_desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getAddress_phone() {
        return address_phone;
    }

    public void setAddress_phone(String address_phone) {
        this.address_phone = address_phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_icon() {
        return shop_icon;
    }

    public void setShop_icon(String shop_icon) {
        this.shop_icon = shop_icon;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(long paymoney) {
        this.paymoney = paymoney;
    }

    public long getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(long discount_price) {
        this.discount_price = discount_price;
    }

    public long getFeight_price() {
        return feight_price;
    }

    public void setFeight_price(long feight_price) {
        this.feight_price = feight_price;
    }

    public List<OrderInformationGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<OrderInformationGoods> goods) {
        this.goods = goods;
    }
}
