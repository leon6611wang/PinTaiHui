package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class GoodsNormGroup {
    private long id;
    private String name;
    private List<GoodsNorm> list;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GoodsNorm> getList() {
        return list;
    }

    public void setList(List<GoodsNorm> list) {
        this.list = list;
    }
}
