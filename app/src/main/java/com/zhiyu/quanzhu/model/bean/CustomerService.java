package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class CustomerService {
    private List<CustomerServiceMessage> list;
    private CustomerServiceShop shop;
    private CustomerServiceUser user;
    private int total;

    public List<CustomerServiceMessage> getList() {
        return list;
    }

    public void setList(List<CustomerServiceMessage> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public CustomerServiceShop getShop() {
        return shop;
    }

    public void setShop(CustomerServiceShop shop) {
        this.shop = shop;
    }

    public CustomerServiceUser getUser() {
        return user;
    }

    public void setUser(CustomerServiceUser user) {
        this.user = user;
    }
}
