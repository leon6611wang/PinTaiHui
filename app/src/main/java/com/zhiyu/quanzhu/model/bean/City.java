package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class City {
    private String name;
    private String code;
    private String ziMu;
    private boolean selected;
    private List<Area> area;

    @Override
    public String toString() {
        return "name: "+name;
    }

    public List<Area> getArea() {
        return area;
    }

    public void setArea(List<Area> area) {
        this.area = area;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZiMu() {
        return ziMu;
    }

    public void setZiMu(String ziMu) {
        this.ziMu = ziMu;
    }
}
