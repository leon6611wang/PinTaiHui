package com.zhiyu.quanzhu.model.bean;

public class QuanZiGuanZhu {
    private int type;
    private String type_desc;
    private QuanZiGuanZhuContent content;
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

    public String getType_desc() {
        return type_desc;
    }

    public void setType_desc(String type_desc) {
        this.type_desc = type_desc;
    }

    public QuanZiGuanZhuContent getContent() {
        return content;
    }

    public void setContent(QuanZiGuanZhuContent content) {
        this.content = content;
    }
}
