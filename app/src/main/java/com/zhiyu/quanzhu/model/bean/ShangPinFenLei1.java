package com.zhiyu.quanzhu.model.bean;

public class ShangPinFenLei1 {
    private String name;

    private boolean isSelected;


    public ShangPinFenLei1(String name) {
        this.name = name;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
