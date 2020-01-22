package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class Industry {
    private List<IndustryParent> child;

    public List<IndustryParent> getChild() {
        return child;
    }

    public void setChild(List<IndustryParent> child) {
        this.child = child;
    }
}
