package com.zhiyu.quanzhu.model.bean;

/**
 * 我的卡券
 */
public class MyCoupon {
    private int id;
    private String title;
    private int status;
    private String amount;
    private String mark;
    private String exp_start;
    private String exp_end;
    private String desc;
    private String usedesc;

    public String getUsedesc() {
        return usedesc;
    }

    public void setUsedesc(String usedesc) {
        this.usedesc = usedesc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
