package com.zhiyu.quanzhu.model.bean;

/**
 * 发布文章、发布视频-关联商品的店铺
 */
public class LinkShop {
    private int quanzi_id;
    private String name;
    private String icon;
    private int id;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getQuanzi_id() {
        return quanzi_id;
    }

    public void setQuanzi_id(int quanzi_id) {
        this.quanzi_id = quanzi_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
