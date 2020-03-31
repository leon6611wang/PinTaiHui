package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.OrderConfirmAddress;
import com.zhiyu.quanzhu.model.bean.OrderConfirmShop;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单确认实体类
 */
public class OrderConfirmData {
    private ArrayList<OrderConfirmShop> list;
    private OrderConfirmAddress address;

    public ArrayList<OrderConfirmShop> getList() {
        return list;
    }

    public void setList(ArrayList<OrderConfirmShop> list) {
        this.list = list;
    }

    public OrderConfirmAddress getAddress() {
        return address;
    }

    public void setAddress(OrderConfirmAddress address) {
        this.address = address;
    }
}
