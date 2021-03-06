package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class FeedInformation {
    private int id;
    private String title;
    private String thumb;
    private String thumb_file;
    private ArticleThumb newthumb;
    private String desc;
    private int video_width;
    private int video_height;
    private String video_thumb;
    private String video_url;
    private int uid;
    private int view_num;
    private List<String> tags;
    private String time;
    private String username;
    private String avatar;
    private boolean is_follow;
    private  String follow_num;
    private boolean is_collect;
    private int collect_num;
    private boolean is_prise;
    private int prise_num;
    private boolean is_report;
    private String circle_name;
    private List<QuanZiGuanZhuImg> imgs;
    private DongTaiInfoCircle circle;
    private int comment_num;
    private List<FeedTag> feeds_tags;
    private String content;
    private String industry;
    private String p_industry;
    private String hobby;
    private String p_hobby;
    private int is_secret;
    private String secret_desc;
    private String format_view_num;

    public String getThumb_file() {
        return thumb_file;
    }

    public void setThumb_file(String thumb_file) {
        this.thumb_file = thumb_file;
    }

    public ArticleThumb getNewthumb() {
        return newthumb;
    }

    public void setNewthumb(ArticleThumb newthumb) {
        this.newthumb = newthumb;
    }

    public String getFormat_view_num() {
        return format_view_num;
    }

    public void setFormat_view_num(String format_view_num) {
        this.format_view_num = format_view_num;
    }

    public int getIs_secret() {
        return is_secret;
    }

    public void setIs_secret(int is_secret) {
        this.is_secret = is_secret;
    }

    public String getSecret_desc() {
        switch (this.is_secret){
            case 1:
                this.secret_desc="公开";
                break;
            case 2:
                this.secret_desc="名片圈友可见";
                break;
            case 3:
                this.secret_desc="私密";
                break;
            case 4:
                this.secret_desc="仅@圈子可见";
                break;
        }
        return secret_desc;
    }

    public void setSecret_desc(String secret_desc) {
        this.secret_desc = secret_desc;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getP_industry() {
        return p_industry;
    }

    public void setP_industry(String p_industry) {
        this.p_industry = p_industry;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getP_hobby() {
        return p_hobby;
    }

    public void setP_hobby(String p_hobby) {
        this.p_hobby = p_hobby;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public List<FeedTag> getFeeds_tags() {
        return feeds_tags;
    }

    public void setFeeds_tags(List<FeedTag> feeds_tags) {
        this.feeds_tags = feeds_tags;
    }

    public boolean isIs_report() {
        return is_report;
    }

    public void setIs_report(boolean is_report) {
        this.is_report = is_report;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getVideo_width() {
        return video_width;
    }

    public void setVideo_width(int video_width) {
        this.video_width = video_width;
    }

    public int getVideo_height() {
        return video_height;
    }

    public void setVideo_height(int video_height) {
        this.video_height = video_height;
    }

    public String getVideo_thumb() {
        return video_thumb;
    }

    public void setVideo_thumb(String video_thumb) {
        this.video_thumb = video_thumb;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getView_num() {
        return view_num;
    }

    public void setView_num(int view_num) {
        this.view_num = view_num;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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

    public String getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(String follow_num) {
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

    public String getCircle_name() {
        return circle_name;
    }

    public void setCircle_name(String circle_name) {
        this.circle_name = circle_name;
    }

    public List<QuanZiGuanZhuImg> getImgs() {
        return imgs;
    }

    public void setImgs(List<QuanZiGuanZhuImg> imgs) {
        this.imgs = imgs;
    }

    public DongTaiInfoCircle getCircle() {
        return circle;
    }

    public void setCircle(DongTaiInfoCircle circle) {
        this.circle = circle;
    }
}
