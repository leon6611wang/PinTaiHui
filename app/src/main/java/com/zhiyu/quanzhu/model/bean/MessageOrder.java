package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class MessageOrder {
   private String name;
   private String mobile;
   private String address;
   private int num;
   private long price;
   private boolean is_confirm;
   private int oid;
    private List<MessageGoods> order_goods;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public boolean isIs_confirm() {
        return is_confirm;
    }

    public void setIs_confirm(boolean is_confirm) {
        this.is_confirm = is_confirm;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public List<MessageGoods> getOrder_goods() {
        return order_goods;
    }

    public void setOrder_goods(List<MessageGoods> order_goods) {
        this.order_goods = order_goods;
    }
}
