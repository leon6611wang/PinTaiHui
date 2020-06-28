package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class OrderShop {
    private int id;
    private int status;//订单状态 0待支付 1 表示取消 2已付款待发货 3已发货待收货 4 表示已收货待评价 5已评价,6售后中
    private int cancel_type;//1：用户到时未支付；2：用户主动取消
    private String status_desc;
    private long price;
    private int num;
    private int pay_type;//1表示支付宝 2表示微信 3表示余额（支付宝）4表示余额（微信）
    private int shop_id;
    private String shop_name;
    private List<OrderGoods> goods;
    private long over_time;
    private int is_pay;//1已付款 0未付款
    private boolean timeCountComplete;

    public int getCancel_type() {
        return cancel_type;
    }

    public void setCancel_type(int cancel_type) {
        this.cancel_type = cancel_type;
    }

    public boolean isTimeCountComplete() {
        return timeCountComplete;
    }

    public void setTimeCountComplete(boolean timeCountComplete) {
        this.timeCountComplete = timeCountComplete;
    }

    public int getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(int is_pay) {
        this.is_pay = is_pay;
    }

    public long getOver_time() {
        return over_time;
    }

    public void setOver_time(long over_time) {
        this.over_time = over_time;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_desc() {
        switch (this.status) {
            case 0:
                this.status_desc = "待支付";
                break;
            case 1:
                this.status_desc = "已取消";
                break;
            case 2:
                this.status_desc = "待发货";
                break;
            case 3:
                this.status_desc = "待收货";
                break;
            case 4:
                this.status_desc = "待评价";
                break;
            case 5:
                this.status_desc = "已完成";
                break;
            case 6:
                this.status_desc = "售后中";
                break;

        }
        return status_desc;
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
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

    public List<OrderGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<OrderGoods> goods) {
        this.goods = goods;
    }
}
