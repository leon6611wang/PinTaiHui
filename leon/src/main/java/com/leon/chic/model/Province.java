package com.leon.chic.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "province")
public class Province {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    @Column(name = "code")
    private int code;
    @Column(name = "name")
    private String name;
   @Column(name="citys")
    private String citys;

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

    public String getCitys() {
        return citys;
    }

    public void setCitys(String citys) {
        this.citys = citys;
    }
}
