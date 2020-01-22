package com.zhiyu.quanzhu.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品评价
 */
public class GoodsComment {
    private long id;
    private String content;
    private long uid;
    private String username;
    private String avatar;
    private String source;
    private String date;
    private String norms_name;
    private String reply;
    private int pnum;
    private boolean is_prise;
    private ArrayList<String> img;
    private boolean isExpand;

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNorms_name() {
        return norms_name;
    }

    public void setNorms_name(String norms_name) {
        this.norms_name = norms_name;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getPnum() {
        return pnum;
    }

    public void setPnum(int pnum) {
        this.pnum = pnum;
    }

    public boolean isIs_prise() {
        return is_prise;
    }

    public void setIs_prise(boolean is_prise) {
        this.is_prise = is_prise;
    }

    public ArrayList<String> getImg() {
        return img;
    }

    public void setImg(ArrayList<String> img) {
        this.img = img;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
