package com.zhiyu.quanzhu.model.bean;

import java.util.List;

/**
 * 文章详情
 */
public class ArticleInformation {
    private int id;
    private String title;
    private ArticleThumb thumb;
    private List<ArticleContent> content;
    private String desc;
    private int uid;
    private String view_num;
    private String time;
    private String username;
    private String avatar;
    private boolean is_follow;
    private int follow_num;
    private boolean is_collect;
    private int collect_num;
    private boolean is_prise;
    private int prise_num;
    private int comment_num;
    private String circle_name;
    private boolean is_report;
    private List<FeedsTag> feeds_tags;
    private ArticleInformationCircle circle;

    public List<FeedsTag> getFeeds_tags() {
        return feeds_tags;
    }

    public void setFeeds_tags(List<FeedsTag> feeds_tags) {
        this.feeds_tags = feeds_tags;
    }

    public ArticleInformationCircle getCircle() {
        return circle;
    }

    public void setCircle(ArticleInformationCircle circle) {
        this.circle = circle;
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

    public ArticleThumb getThumb() {
        return thumb;
    }

    public void setThumb(ArticleThumb thumb) {
        this.thumb = thumb;
    }

    public List<ArticleContent> getContent() {
        return content;
    }

    public void setContent(List<ArticleContent> content) {
        this.content = content;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getView_num() {
        return view_num;
    }

    public void setView_num(String view_num) {
        this.view_num = view_num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public boolean isIs_follow() {
        return is_follow;
    }

    public void setIs_follow(boolean is_follow) {
        this.is_follow = is_follow;
    }

    public int getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(int follow_num) {
        this.follow_num = follow_num;
    }

    public boolean isIs_collect() {
        return is_collect;
    }

    public void setIs_collect(boolean is_collect) {
        this.is_collect = is_collect;
    }

    public int getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(int collect_num) {
        this.collect_num = collect_num;
    }

    public boolean isIs_prise() {
        return is_prise;
    }

    public void setIs_prise(boolean is_prise) {
        this.is_prise = is_prise;
    }

    public int getPrise_num() {
        return prise_num;
    }

    public void setPrise_num(int prise_num) {
        this.prise_num = prise_num;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public String getCircle_name() {
        return circle_name;
    }

    public void setCircle_name(String circle_name) {
        this.circle_name = circle_name;
    }

    public boolean isIs_report() {
        return is_report;
    }

    public void setIs_report(boolean is_report) {
        this.is_report = is_report;
    }
}
