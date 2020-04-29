package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class VIP {
    private int id;
    private String name;
    private String icon;
    private int price;
    private List<VIPEquity> equity_list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<VIPEquity> getEquity_list() {
        return equity_list;
    }

    public void setEquity_list(List<VIPEquity> equity_list) {
        this.equity_list = equity_list;
    }
}
