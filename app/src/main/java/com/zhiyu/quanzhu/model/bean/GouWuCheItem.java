package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class GouWuCheItem {
    private boolean isSelected;

    private List<GouWuCheItemItem> list;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<GouWuCheItemItem> getList() {
        return list;
    }

    public void setList(List<GouWuCheItemItem> list) {
        this.list = list;
    }
}
