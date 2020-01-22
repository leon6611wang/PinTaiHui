package com.zhiyu.quanzhu.model.bean;

import java.util.List;

/**
 * 商品详情-规格
 */
public class GuiGe {
    private int id;
    private String name;
    private List<GuiGeChild> list;

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

    public List<GuiGeChild> getList() {
        return list;
    }

    public void setList(List<GuiGeChild> list) {
        this.list = list;
    }
}
