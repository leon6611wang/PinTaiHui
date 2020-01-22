package com.zhiyu.quanzhu.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

/**
 * 地区-省份
 */
@Table(name = "area_province")
public class AreaProvince {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    @Column(name = "code")
    private int code;
    @Column(name = "name")
    private String name;
    private List<AreaCity> child;
    private int pcode;

    public int getPcode() {
        return pcode;
    }

    public void setPcode(int pcode) {
        this.pcode = pcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AreaCity> getChild() {
        return child;
    }

    public void setChild(List<AreaCity> child) {
        this.child = child;
    }
}
