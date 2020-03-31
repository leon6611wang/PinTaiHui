package com.zhiyu.quanzhu.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 名片好友
 */
@Table(name = "card_fend")
public class CardFrend {
    @Column(name = "card_frend_id", isId = true, autoGen=true)
    private int card_frend_id;
    @Column(name = "id")
    private int id;
    @Column(name = "notename")
    private String notename;
    @Column(name = "card_name")
    private String card_name;
    @Column(name = "card_thumb")
    private String card_thumb;
    @Column(name = "occupation")
    private String occupation;
    @Column(name = "company")
    private String company;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "email")
    private String email;
    @Column(name = "wx")
    private String wx;
    private int share;
    private int view;
    private int prise;
    private int collect;

    private boolean isSelected;


    @Column(name = "letter")
    private String letter;//排序的字母(A,B,C)

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getCard_frend_id() {
        return card_frend_id;
    }

    public void setCard_frend_id(int card_frend_id) {
        this.card_frend_id = card_frend_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotename() {
        return notename;
    }

    public void setNotename(String notename) {
        this.notename = notename;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getCard_thumb() {
        return card_thumb;
    }

    public void setCard_thumb(String card_thumb) {
        this.card_thumb = card_thumb;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getPrise() {
        return prise;
    }

    public void setPrise(int prise) {
        this.prise = prise;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }
}
