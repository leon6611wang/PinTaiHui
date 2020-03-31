package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.Feed;

import java.util.List;

public class FeedData {
    private List<Feed> mycircles;
    private List<Feed> list;

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
