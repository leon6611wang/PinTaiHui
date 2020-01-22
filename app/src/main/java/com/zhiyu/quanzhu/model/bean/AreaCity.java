package com.zhiyu.quanzhu.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 地区-城市
 */
@Table(name = "area_city")
public class AreaCity {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    @Column(name = "code")
    private long code;
    @Column(name = "pcode")
    private long pcode;
    @Column(name = "name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public long getPcode() {
        return pcode;
    }

    public void setPcode(long pcode) {
        this.pcode = pcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
