package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.Feed;

import java.util.List;

public class FeedData {
    private List<Feed> mycircles;
    private List<Feed> list;
    private List<Feed> feeds;

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

    public List<Feed> getList() {
        return list;
    }

    public void setList(List<Feed> list) {
        this.list = list;
    }

    public List<Feed> getMycircles() {
        return mycircles;
    }

    public void setMycircles(List<Feed> mycircles) {
        this.mycircles = mycircles;
    }
}
