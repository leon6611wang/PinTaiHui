package com.zhiyu.quanzhu.model.bean;

public class QianDao {
    private String title;
    private int jifen;

    public QianDao(String title, int jifen) {
        this.title = title;
        this.jifen = jifen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getJifen() {
        return jifen;
    }

    public void setJifen(int jifen) {
        this.jifen = jifen;
    }
}
