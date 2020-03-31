package com.zhiyu.quanzhu.model.bean;

/**
 * 基本信息
 */
public class User {
    private String avatar;
    private String truename;
    private String company;
    private String occupation;
    private String provice;
    private String city;
    private String district;
    //基础信息是否完善
    private int infostatus;
    //密码是否设置
    private int pwdstatus;
    //手机号是否绑定
    private int mobilestatus;
    //个人偏好是否完善
    private int industrystatus;
    private String username;
    private String regtoken;
    private int credit;
    private int score;
    private boolean cart_status;
    private int feeds_count;
    private boolean friends_status;
    private int friends_count;
    private int follow_count;
    private boolean prise_status;
    private int prise_count;
    private boolean cards_status;
    private boolean comment_status;
    private int order_pay;
    private int order_send;
    private int order_comment;
    private int order_revice;
    private int order_back;


    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getRegtoken() {
        return regtoken;
    }

    public void setRegtoken(String regtoken) {
        this.regtoken = regtoken;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isCart_status() {
        return cart_status;
    }

    public void setCart_status(boolean cart_status) {
        this.cart_status = cart_status;
    }

    public int getFeeds_count() {
        return feeds_count;
    }

    public void setFeeds_count(int feeds_count) {
        this.feeds_count = feeds_count;
    }

    public boolean isFriends_status() {
        return friends_status;
    }

    public void setFriends_status(boolean friends_status) {
        this.friends_status = friends_status;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public int getFollow_count() {
        return follow_count;
    }

    public void setFollow_count(int follow_count) {
        this.follow_count = follow_count;
    }

    public boolean isPrise_status() {
        return prise_status;
    }

    public void setPrise_status(boolean prise_status) {
        this.prise_status = prise_status;
    }

    public int getPrise_count() {
        return prise_count;
    }

    public void setPrise_count(int prise_count) {
        this.prise_count = prise_count;
    }

    public boolean isCards_status() {
        return cards_status;
    }

    public void setCards_status(boolean cards_status) {
        this.cards_status = cards_status;
    }

    public boolean isComment_status() {
        return comment_status;
    }

    public void setComment_status(boolean comment_status) {
        this.comment_status = comment_status;
    }

    public int getOrder_pay() {
        return order_pay;
    }

    public void setOrder_pay(int order_pay) {
        this.order_pay = order_pay;
    }

    public int getOrder_send() {
        return order_send;
    }

    public void setOrder_send(int order_send) {
        this.order_send = order_send;
    }

    public int getOrder_comment() {
        return order_comment;
    }

    public void setOrder_comment(int order_comment) {
        this.order_comment = order_comment;
    }

    public int getOrder_revice() {
        return order_revice;
    }

    public void setOrder_revice(int order_revice) {
        this.order_revice = order_revice;
    }

    public int getOrder_back() {
        return order_back;
    }

    public void setOrder_back(int order_back) {
        this.order_back = order_back;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getInfostatus() {
        return infostatus;
    }

    public void setInfostatus(int infostatus) {
        this.infostatus = infostatus;
    }

    public int getPwdstatus() {
        return pwdstatus;
    }

    public void setPwdstatus(int pwdstatus) {
        this.pwdstatus = pwdstatus;
    }

    public int getMobilestatus() {
        return mobilestatus;
    }

    public void setMobilestatus(int mobilestatus) {
        this.mobilestatus = mobilestatus;
    }

    public int getIndustrystatus() {
        return industrystatus;
    }

    public void setIndustrystatus(int industrystatus) {
        this.industrystatus = industrystatus;
    }
}
