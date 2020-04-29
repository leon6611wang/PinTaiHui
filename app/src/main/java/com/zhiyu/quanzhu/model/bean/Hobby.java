package com.zhiyu.quanzhu.model.bean;

import java.util.List;

/**
 * 偏好
 */
public class Hobby {
    private int id;
    private String name;
    private String sub_name;
    private int pid;
    private int type;
    private int level;
    private boolean is_choose;//右边最小层级是否选中
    private int seletedCount;//左边选中的数量
    private List<Hobby> child;


    public int getSeletedCount() {
        return seletedCount;
    }

    public void setSeletedCount(int seletedCount) {
        this.seletedCount = seletedCount;
    }

    public boolean isIs_choose() {
        return is_choose;
    }

    public void setIs_choose(boolean is_choose) {
        this.is_choose = is_choose;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public List<Hobby> getChild() {
        return child;
    }

    public void setChild(List<Hobby> child) {
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

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
