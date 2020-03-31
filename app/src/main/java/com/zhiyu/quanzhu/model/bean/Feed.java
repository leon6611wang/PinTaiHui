package com.zhiyu.quanzhu.model.bean;

public class Feed {
    private int feed_type;
    private int feeds_type;
    private int type;
    private FeedContent content;

    public int getFeeds_type() {
        return feeds_type;
    }

    public void setFeeds_type(int feeds_type) {
        this.feeds_type = feeds_type;
    }

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

    public FeedContent getContent() {
        return content;
    }

    public void setContent(FeedContent content) {
        this.content = content;
    }
}
