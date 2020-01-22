package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class QuanZiGuanZhuContent {
    private int feed_id;
    private int video_width;
    private int video_height;
    private int uid;
    private String username;
    private String avatar;
    private String time;
    private String title;
    private String content;
    private String description;
    private String video_url;
    private int share_num;
    private int collect_num;
    private int prise_num;
    private int comment_num;
    private boolean is_collect;
    private boolean is_prise;
    private String circle_name;
    private List<String> circle_tags;
    private String link_url;
    private List<QuanZiGuanZhuImg> imgs;
    private boolean is_video_play = false;//视频是否在播放
    private long currentPosition;//当前播放点
    private boolean is_video_url_has=false;//视频URL是否已经注入
    private List<FeedsTag> feeds_tags;


    public List<FeedsTag> getFeeds_tags() {
        return feeds_tags;
    }

    public void setFeeds_tags(List<FeedsTag> feeds_tags) {
        this.feeds_tags = feeds_tags;
    }

    public boolean isIs_video_url_has() {
        return is_video_url_has;
    }

    public void setIs_video_url_has(boolean is_video_url_has) {
        this.is_video_url_has = is_video_url_has;
    }

    public int getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(int feed_id) {
        this.feed_id = feed_id;
    }

    public boolean isIs_video_play() {
        return is_video_play;
    }

    public void setIs_video_play(boolean is_video_play) {
        this.is_video_play = is_video_play;
    }

    public long getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(long currentPosition) {
        this.currentPosition = currentPosition;
    }

    public List<QuanZiGuanZhuImg> getImgs() {
        return imgs;
    }

    public void setImgs(List<QuanZiGuanZhuImg> imgs) {
        this.imgs = imgs;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getShare_num() {
        return share_num;
    }

    public void setShare_num(int share_num) {
        this.share_num = share_num;
    }

    public int getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(int collect_num) {
        this.collect_num = collect_num;
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

    public boolean isIs_collect() {
        return is_collect;
    }

    public void setIs_collect(boolean is_collect) {
        this.is_collect = is_collect;
    }

    public boolean isIs_prise() {
        return is_prise;
    }

    public void setIs_prise(boolean is_prise) {
        this.is_prise = is_prise;
    }

    public String getCircle_name() {
        return circle_name;
    }

    public void setCircle_name(String circle_name) {
        this.circle_name = circle_name;
    }

    public List<String> getCircle_tags() {
        return circle_tags;
    }

    public void setCircle_tags(List<String> circle_tags) {
        this.circle_tags = circle_tags;
    }
}
