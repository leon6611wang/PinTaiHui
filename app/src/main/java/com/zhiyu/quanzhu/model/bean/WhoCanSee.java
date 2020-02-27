package com.zhiyu.quanzhu.model.bean;

/**
 * 谁可以看
 */
public class WhoCanSee {
    private String title;
    private String desc;
    private boolean isSelected;

    public WhoCanSee(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return title+" , "+desc+" , "+isSelected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
