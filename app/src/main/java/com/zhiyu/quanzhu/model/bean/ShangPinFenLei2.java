package com.zhiyu.quanzhu.model.bean;

public class ShangPinFenLei2 {
    private String name;
    private boolean isDecoration;

    public ShangPinFenLei2(String name,boolean isDecoration) {
        this.name=name;
        this.isDecoration = isDecoration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDecoration() {
        return isDecoration;
    }

    public void setDecoration(boolean decoration) {
        isDecoration = decoration;
    }
}
