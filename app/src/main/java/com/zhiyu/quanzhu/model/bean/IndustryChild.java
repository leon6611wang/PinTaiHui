package com.zhiyu.quanzhu.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "industry_child")
public class IndustryChild {
    @Column(name = "industry_child_id", isId = true,autoGen = true)
    private int industry_child_id;
    @Column(name = "id")
    private int id;
    @Column(name = "pid")
    private int pid;
    @Column(name = "name")
    private String name;

    @Override
    public String toString() {
        return "industry_child_id: "+industry_child_id+" , id: "+id+" , pid: "+pid+" , name: "+name;
    }

    public int getIndustry_child_id() {
        return industry_child_id;
    }

    public void setIndustry_child_id(int industry_child_id) {
        this.industry_child_id = industry_child_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
