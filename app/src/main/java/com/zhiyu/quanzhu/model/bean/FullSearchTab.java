package com.zhiyu.quanzhu.model.bean;

/**
 * 全局搜索-tab实体类
 */
public class FullSearchTab {
    private String tab;
    private boolean isSelected;

    public FullSearchTab(String tab, boolean isSelected) {
        this.tab = tab;
        this.isSelected = isSelected;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
