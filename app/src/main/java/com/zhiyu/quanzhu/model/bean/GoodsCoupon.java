package com.zhiyu.quanzhu.model.bean;

public class GoodsCoupon {
    private long id;
    private String title;
    private long amount;
    private String desc;
    private String limit;
    private String exp_start;
    private String exp_end;
    private String discount;
    private boolean is_has;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getExp_start() {
        return exp_start;
    }

    public void setExp_start(String exp_start) {
        this.exp_start = exp_start;
    }

    public String getExp_end() {
        return exp_end;
    }

    public void setExp_end(String exp_end) {
        this.exp_end = exp_end;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public boolean isIs_has() {
        return is_has;
    }

    public void setIs_has(boolean is_has) {
        this.is_has = is_has;
    }
}
