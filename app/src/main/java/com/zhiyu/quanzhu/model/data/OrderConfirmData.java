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
    private int count;
    private long sum_price;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getSum_price() {
        return sum_price;
    }

    public void setSum_price(long sum_price) {
        this.sum_price = sum_price;
    }

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
