package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class Province {
    private String name;
    private String code;
    private List<City> city;

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

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }
}
