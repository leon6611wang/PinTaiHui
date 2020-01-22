package com.zhiyu.quanzhu.model.bean;

public class DongTaiGuanZhu {
    private int type;
    private boolean isExpand=false;

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
