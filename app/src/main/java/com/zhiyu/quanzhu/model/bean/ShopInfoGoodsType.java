package com.zhiyu.quanzhu.model.bean;

import java.util.List;

/**
 * 店铺详情-商品分类
 */
public class ShopInfoGoodsType {
    private int id;
    private String name;
    private int pid;
    private int level;
    private List<ShopInfoGoodsType> child;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

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

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<ShopInfoGoodsType> getChild() {
        return child;
    }

    public void setChild(List<ShopInfoGoodsType> child) {
        this.child = child;
    }
}
