package com.zhiyu.quanzhu.model.bean;

/**
 * 动态评论-二级
 */
public class CommentChild {
    private int id;
    private String content;
    private String dateline;
    private int uid;
    private String username;
    private String avatar;
    private int pnum;
    private int type;
    private String reply_username;
    private String pcontent;

    private boolean is_author;
    private boolean is_circle_own;
    private boolean is_manger;
    private boolean is_own;
    private boolean is_prise;

    public boolean isIs_prise() {
        return is_prise;
    }

    public void setIs_prise(boolean is_prise) {
        this.is_prise = is_prise;
    }

    public boolean isIs_author() {
        return is_author;
    }

    public void setIs_author(boolean is_author) {
        this.is_author = is_author;
    }

    public boolean isIs_circle_own() {
        return is_circle_own;
    }

    public void setIs_circle_own(boolean is_circle_own) {
        this.is_circle_own = is_circle_own;
    }

    public boolean isIs_manger() {
        return is_manger;
    }

    public void setIs_manger(boolean is_manger) {
        this.is_manger = is_manger;
    }

    public boolean isIs_own() {
        return is_own;
    }

    public void setIs_own(boolean is_own) {
        this.is_own = is_own;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
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

    public int getPnum() {
        return pnum;
    }

    public void setPnum(int pnum) {
        this.pnum = pnum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReply_username() {
        return reply_username;
    }

    public void setReply_username(String reply_username) {
        this.reply_username = reply_username;
    }

    public String getPcontent() {
        return pcontent;
    }

    public void setPcontent(String pcontent) {
        this.pcontent = pcontent;
    }
}
