package com.zhiyu.quanzhu.model.bean;

/**
 * 我的收藏tab
 */
public class MyCollectionTab {
    private String title;
    private boolean isSelected;

    public MyCollectionTab(String title, boolean isSelected) {
        this.title = title;
        this.isSelected = isSelected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
