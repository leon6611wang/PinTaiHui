package com.zhiyu.quanzhu.model.bean;

public class Area {
    private String name;
    private String code;

    @Override
    public String toString() {
        return "name: "+name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
