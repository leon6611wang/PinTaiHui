package com.zhiyu.quanzhu.model.bean;

public class ZhiFuTongZhi {
    private int action;
    private String action_desc;
    private long money;
    private long servermoney;
    private String account;
    private String type;
    private String add_time;
    private String pay_time;
    private String cash_time;
    private String time;
    private String order_sn;
    private String result_msg;
    private String money_title;
    private String goods;

    public String getCash_time() {
        return cash_time;
    }

    public void setCash_time(String cash_time) {
        this.cash_time = cash_time;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getAction_desc() {
        switch (action){
            case 1:
                this.action_desc="付款通知";
                break;
            case 2:
                this.action_desc="退款通知";
                break;
            case 3:
                this.action_desc="提现通知";
                break;
        }
        return action_desc;
    }

    public String getMoney_title() {
        switch (action){
            case 1:
                this.money_title="付款金额";
                break;
            case 2:
                this.money_title="退款金额";
                break;
            case 3:
                this.money_title="提现金额";
                break;
        }
        return money_title;
    }

    public void setMoney_title(String money_title) {
        this.money_title = money_title;
    }

    public void setAction_desc(String action_desc) {
        this.action_desc = action_desc;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getServermoney() {
        return servermoney;
    }

    public void setServermoney(long servermoney) {
        this.servermoney = servermoney;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }
}
