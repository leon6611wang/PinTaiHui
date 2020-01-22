package com.zhiyu.quanzhu.model.bean;

/**
 * 商品详情-规格-子项
 */
public class GuiGeChild {
    private int norms_id;
    private String norms_name;
    private int stock;
    private String img;
    private boolean selectable;
    private boolean selected;


    @Override
    public String toString() {
        return "norms_id: "+norms_id+" ,norms_name: "+norms_name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getNorms_id() {
        return norms_id;
    }

    public void setNorms_id(int norms_id) {
        this.norms_id = norms_id;
    }

    public String getNorms_name() {
        return norms_name;
    }

    public void setNorms_name(String norms_name) {
        this.norms_name = norms_name;
    }
}
