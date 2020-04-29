package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class MyVisitor {
    private boolean is_vip;
    private List<Visitor> list;

    public boolean isIs_vip() {
        return is_vip;
    }

    public void setIs_vip(boolean is_vip) {
        this.is_vip = is_vip;
    }

    public List<Visitor> getList() {
        return list;
    }

    public void setList(List<Visitor> list) {
        this.list = list;
    }
}
