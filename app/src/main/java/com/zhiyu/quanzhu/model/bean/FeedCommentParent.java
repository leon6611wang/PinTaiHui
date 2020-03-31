package com.zhiyu.quanzhu.model.bean;

import java.util.List;

/**
 * 动态/文章/视频
 * 父级评论
 */
public class FeedCommentParent {
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
    private boolean is_author;
    private boolean is_circle_own;
    private boolean is_manger;
    private boolean is_own;
    private boolean reply_more;
    private int reply_total;
    private int is_del;
    private List<FeedCommentChild> reply;
    private int feeds_id;
    private int last_id;
    private int page;

    public int getFeeds_id() {
        return feeds_id;
    }

    public void setFeeds_id(int feeds_id) {
        this.feeds_id = feeds_id;
    }

    public int getLast_id() {
        return last_id;
    }

    public void setLast_id(int last_id) {
        this.last_id = last_id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getIs_del() {
        return is_del;
    }

    public void setIs_del(int is_del) {
        this.is_del = is_del;
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

    public boolean isReply_more() {
        return reply_more;
    }

    public void setReply_more(boolean reply_more) {
        this.reply_more = reply_more;
    }

    public int getReply_total() {
        return reply_total;
    }

    public void setReply_total(int reply_total) {
        this.reply_total = reply_total;
    }

    public List<FeedCommentChild> getReply() {
        return reply;
    }

    public void setReply(List<FeedCommentChild> reply) {
        this.reply = reply;
    }
}
