package com.zhiyu.quanzhu.model.bean;

import java.util.List;

/**
 * 动态评论-一级
 */
public class CommentParent {
    private int id;
    private String content;
    private String dateline;
    private int uid;
    private int type;
    private String username;
    private String avatar;
    private int tuid;
    private String reply_username;
    private String reply_avatar;
    private int pnum;
    private boolean is_prise;
    private boolean reply_more;
    private boolean is_author;
    private boolean is_circle_own;
    private boolean is_own;
    private List<CommentChild> reply;

    private FeedInformation information;
    private int adapter_type;


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

    public boolean isIs_own() {
        return is_own;
    }

    public void setIs_own(boolean is_own) {
        this.is_own = is_own;
    }

    public FeedInformation getInformation() {
        return information;
    }

    public void setInformation(FeedInformation information) {
        this.information = information;
    }

    public int getAdapter_type() {
        return adapter_type;
    }

    public void setAdapter_type(int adapter_type) {
        this.adapter_type = adapter_type;
    }

    public boolean isReply_more() {
        return reply_more;
    }

    public void setReply_more(boolean reply_more) {
        this.reply_more = reply_more;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getTuid() {
        return tuid;
    }

    public void setTuid(int tuid) {
        this.tuid = tuid;
    }

    public String getReply_username() {
        return reply_username;
    }

    public void setReply_username(String reply_username) {
        this.reply_username = reply_username;
    }

    public String getReply_avatar() {
        return reply_avatar;
    }

    public void setReply_avatar(String reply_avatar) {
        this.reply_avatar = reply_avatar;
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

    public List<CommentChild> getReply() {
        return reply;
    }

    public void setReply(List<CommentChild> reply) {
        this.reply = reply;
    }
}
