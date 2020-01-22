package com.zhiyu.quanzhu.model.bean;

/**
 * 商品规格
 */
public class GoodsNorm {
    private long id;
    private long norms_id;
    private String norms_name;
    private long p_id;
    //是否可选
    private boolean selectable=false;
    //是否选中
    private boolean isSelected=false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getP_id() {
        return p_id;
    }

    public void setP_id(long p_id) {
        this.p_id = p_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public long getNorms_id() {
        return norms_id;
    }

    public void setNorms_id(long norms_id) {
        this.norms_id = norms_id;
    }

    public String getNorms_name() {
        return norms_name;
    }

    public void setNorms_name(String norms_name) {
        this.norms_name = norms_name;
    }
}
