package com.zhiyu.quanzhu.model.bean;

public class FooterPrintComment {
    private int id;
    private String content;
    private String dateline;
    private int type;//1:文章，2：视频，3：动态
    private int feeds_type;
    private int feeds_id;
    private String feeds_title;
    private int is_read;

    public int getFeeds_type() {
        return feeds_type;
    }

    public void setFeeds_type(int feeds_type) {
        this.feeds_type = feeds_type;
    }

    public int getFeeds_id() {
        return feeds_id;
    }

    public void setFeeds_id(int feeds_id) {
        this.feeds_id = feeds_id;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFeeds_title() {
        return feeds_title;
    }

    public void setFeeds_title(String feeds_title) {
        this.feeds_title = feeds_title;
    }
}
