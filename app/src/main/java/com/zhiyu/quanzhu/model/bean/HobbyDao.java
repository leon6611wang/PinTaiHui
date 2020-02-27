package com.zhiyu.quanzhu.model.bean;

import java.util.List;

public class HobbyDao {

    private List<HobbyDaoParent> child;

    public List<HobbyDaoParent> getChild() {
        return child;
    }

    public void setChild(List<HobbyDaoParent> child) {
        this.child = child;
    }
}
