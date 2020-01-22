package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Tag;

import java.util.List;

public class TagResult extends BaseResult {
    private List<Tag> data;

    public List<Tag> getData() {
        return data;
    }

    public void setData(List<Tag> data) {
        this.data = data;
    }
}
