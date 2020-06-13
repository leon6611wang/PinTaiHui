package com.zhiyu.quanzhu.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 圈子推荐-内容
 */
public class QuanZiTuiJianContent {
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
    private String video_thumb;
    private int share_num;
    private int collect_num;
    private int prise_num;
    private int comment_num;
    private String link_url;
    private boolean is_follow;
    private int id;
    private int feed_id;

    private String thumb;
    private List<CircleTuiJianArticle> thumb_content;
    private List<String> articleTxtList;
    private List<String> articleImgList;

    public List<String> getArticleTxtList() {
        if (null == articleTxtList) {
            articleTxtList = new ArrayList<>();
        }
        if (articleTxtList.size() == 0)
            if (null != thumb_content && thumb_content.size() > 0)
                for (CircleTuiJianArticle a : thumb_content) {
                    if (a.getType() == 1) {
                        articleTxtList.add(a.getContent());
                    }
                }

        return articleTxtList;
    }

    public List<String> getArticleImgList() {
        if (null == articleImgList) {
            articleImgList = new ArrayList<>();
        }
        if (articleImgList.size() == 0)
            if (null != thumb_content && thumb_content.size() > 0)
                for (CircleTuiJianArticle a : thumb_content) {
                    if (a.getType() == 2) {
                        articleImgList.add(a.getContent());
                    }
                }
        return articleImgList;
    }

    public List<CircleTuiJianArticle> getThumb_content() {
        return thumb_content;
    }

    public void setThumb_content(List<CircleTuiJianArticle> thumb_content) {
        this.thumb_content = thumb_content;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(int feed_id) {
        this.feed_id = feed_id;
    }

    public String getVideo_thumb() {
        return video_thumb;
    }

    public void setVideo_thumb(String video_thumb) {
        this.video_thumb = video_thumb;
    }

    public boolean isIs_follow() {
        return is_follow;
    }

    public void setIs_follow(boolean is_follow) {
        this.is_follow = is_follow;
    }

    private List<QuanZiTuiJianImg> imgs;

    public List<QuanZiTuiJianImg> getImgs() {
        return imgs;
    }

    public void setImgs(List<QuanZiTuiJianImg> imgs) {
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
}
