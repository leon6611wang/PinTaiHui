package com.zhiyu.quanzhu.model.bean;

/**
 * 积分兑换记录
 */
public class PointExchangeRecord {
    private String name;
    private String thumb;
    private int need_inegral;
    private int need_money;
    private String addtime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getNeed_inegral() {
        return need_inegral;
    }

    public void setNeed_inegral(int need_inegral) {
        this.need_inegral = need_inegral;
    }

    public int getNeed_money() {
        return need_money;
    }

    public void setNeed_money(int need_money) {
        this.need_money = need_money;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
