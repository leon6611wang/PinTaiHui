package com.zhiyu.quanzhu.model.bean;

import java.util.List;

/**
 * 商品分类
 */
public class GoodsFenLei {
    private long id;
    private String name;
    private String img;
    private List<GoodsFenLei> child;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<GoodsFenLei> getChild() {
        return child;
    }

    public void setChild(List<GoodsFenLei> child) {
        this.child = child;
    }

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
