package com.zhiyu.quanzhu.model.bean;

public class FullSearchFeed {
    private int feed_type;
    private int type;
    private FullSearchFeedContent content;

    public int getFeed_type() {
        return feed_type;
    }

    public void setFeed_type(int feed_type) {
        this.feed_type = feed_type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public FullSearchFeedContent getContent() {
        return content;
    }

    public void setContent(FullSearchFeedContent content) {
        this.content = content;
    }
}
