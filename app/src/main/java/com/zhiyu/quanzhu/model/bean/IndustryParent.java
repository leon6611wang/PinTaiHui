package com.zhiyu.quanzhu.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

/**
 * 行业
 */
@Table(name = "industry_parent")
public class IndustryParent {
    @Column(name = "industry_parent_id", isId = true,autoGen=true)
    private int industry_parent_id;
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;

    private List<IndustryChild> child;

    @Override
    public String toString() {
        return "industry_parent_id: "+industry_parent_id+" , id: "+id+" , name: "+name;
    }

    public int getIndustry_parent_id() {
        return industry_parent_id;
    }

    public void setIndustry_parent_id(int industry_parent_id) {
        this.industry_parent_id = industry_parent_id;
    }

    public List<IndustryChild> getChild() {
        return child;
    }

    public void setChild(List<IndustryChild> child) {
        this.child = child;
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
}
