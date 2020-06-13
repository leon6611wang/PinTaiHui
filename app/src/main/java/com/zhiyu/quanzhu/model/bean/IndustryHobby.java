package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class IndustryHobby {
    private int id;
    private String name;
    private List<IndustryHobby> child;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IndustryHobby> getChild() {
        return child;
    }

    public void setChild(List<IndustryHobby> child) {
        this.child = child;
    }
}
