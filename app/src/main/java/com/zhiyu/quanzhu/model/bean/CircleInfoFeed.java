package com.zhiyu.quanzhu.model.bean;

public class CircleInfoFeed {
    private int type;
    private CircleInfoFeedContent content;
    private int feeds_type;

    public int getFeeds_type() {
        return feeds_type;
    }

    public void setFeeds_type(int feeds_type) {
        this.feeds_type = feeds_type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CircleInfoFeedContent getContent() {
        return content;
    }

    public void setContent(CircleInfoFeedContent content) {
        this.content = content;
    }
}
