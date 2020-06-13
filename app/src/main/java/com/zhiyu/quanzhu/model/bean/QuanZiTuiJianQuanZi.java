package com.zhiyu.quanzhu.model.bean;

/**
 * 圈子推荐-感兴趣的圈子
 */
public class QuanZiTuiJianQuanZi {
    private int id;
    private String name;
    private String descirption;
    private int uid;
    private String city_name;
    private String three_industry;
    private int pnum;
    private int fnum;
    private String created_at;
    private String avatar;
    private String username;
    private GuanZhuCirlceThumb thumb;
    private int days;
    private String industry;

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public GuanZhuCirlceThumb getThumb() {
        return thumb;
    }

    public void setThumb(GuanZhuCirlceThumb thumb) {
        this.thumb = thumb;
    }

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

    public String getDescirption() {
        return descirption;
    }

    public void setDescirption(String descirption) {
        this.descirption = descirption;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getThree_industry() {
        return three_industry;
    }

    public void setThree_industry(String three_industry) {
        this.three_industry = three_industry;
    }

    public int getPnum() {
        return pnum;
    }

    public void setPnum(int pnum) {
        this.pnum = pnum;
    }

    public int getFnum() {
        return fnum;
    }

    public void setFnum(int fnum) {
        this.fnum = fnum;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
