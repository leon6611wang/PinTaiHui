package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.CommentParent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态详情-评论
 */
public class CommentData {
    private ArrayList<CommentParent> list;
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<CommentParent> getList() {
        return list;
    }

    public void setList(ArrayList<CommentParent> list) {
        this.list = list;
    }
}
