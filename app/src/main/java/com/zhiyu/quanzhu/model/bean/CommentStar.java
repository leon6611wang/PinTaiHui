package com.zhiyu.quanzhu.model.bean;

public class CommentStar {
    private boolean isSelected;
    private int count;

    public CommentStar(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
