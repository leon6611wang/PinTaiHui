package com.zhiyu.quanzhu.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name="SearchHistory")
public class SearchHistory {
    @Column(isId = true,name="id",autoGen = true,property = "NOT NULL")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name="time")
    private long time;

    @Override
    public String toString() {
        return "id: "+id+" , name: "+name+" , time: "+time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
