package com.zhiyu.quanzhu.model.bean;

import android.view.Gravity;
import android.widget.LinearLayout;

import java.util.List;

public class FullSearchVideoContent {
    private int id;
    private int uid;
    private String username;
    private String avatar;
    private String time;
    private String title;
    private String content;
    private String description;
    private String video_url;
    private int video_width;
    private int video_height;
    private String video_thumb;
    private int share_num;
    private int collect_num;
    private int prise_num;
    private int comment_num;
    private boolean is_follow;
    private boolean is_prise;
    private boolean is_collect;
    private List<FeedTag> feeds_tags;
    private String thumb;
    private ArticleThumb newthumb;
    private String circle_name;

    private LinearLayout.LayoutParams layoutParams;

    public LinearLayout.LayoutParams getLayoutParams(int width, int height) {
        if (null == layoutParams) {
            if (video_width >= video_height) {
                int height_ = Math.round(width * ((float) video_height / (float) video_width));
                layoutParams = new LinearLayout.LayoutParams(width, height_);
            } else {
                int width_ = Math.round(height * ((float) video_width / (float) video_height));
                layoutParams = new LinearLayout.LayoutParams(width_, height);
                layoutParams.gravity = Gravity.CENTER;
            }
        }
        return layoutParams;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isIs_follow() {
        return is_follow;
    }

    public void setIs_follow(boolean is_follow) {
        this.is_follow = is_follow;
    }

    public boolean isIs_prise() {
        return is_prise;
    }

    public void setIs_prise(boolean is_prise) {
        this.is_prise = is_prise;
    }

    public boolean isIs_collect() {
        return is_collect;
    }

    public void setIs_collect(boolean is_collect) {
        this.is_collect = is_collect;
    }

    public List<FeedTag> getFeeds_tags() {
        return feeds_tags;
    }

    public void setFeeds_tags(List<FeedTag> feeds_tags) {
        this.feeds_tags = feeds_tags;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getCircle_name() {
        return circle_name;
    }

    public void setCircle_name(String circle_name) {
        this.circle_name = circle_name;
    }

    public ArticleThumb getNewthumb() {
        return newthumb;
    }

    public void setNewthumb(ArticleThumb newthumb) {
        this.newthumb = newthumb;
    }
}
