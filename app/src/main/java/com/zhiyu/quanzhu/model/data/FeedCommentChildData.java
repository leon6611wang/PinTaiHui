package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.FeedCommentChild;

import java.util.List;

public class FeedCommentChildData {

    private List<FeedCommentChild> list;
    private int total;

    public List<FeedCommentChild> getList() {
        return list;
    }

    public void setList(List<FeedCommentChild> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
