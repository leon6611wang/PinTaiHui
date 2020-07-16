package com.zhiyu.quanzhu.model.bean;

public class OrderAdd {
    private String oid;
    private int all_price;
    private int ali_banlance;
    private int wx_banlance;

    public int getAll_price() {
        return all_price;
    }

    public void setAll_price(int all_price) {
        this.all_price = all_price;
    }

    public int getAli_banlance() {
        return ali_banlance;
    }

    public void setAli_banlance(int ali_banlance) {
        this.ali_banlance = ali_banlance;
    }

    public int getWx_banlance() {
        return wx_banlance;
    }

    public void setWx_banlance(int wx_banlance) {
        this.wx_banlance = wx_banlance;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}
