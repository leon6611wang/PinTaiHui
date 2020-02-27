package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.AddTagData;

public class AddTagResult extends BaseResult {
    private AddTagData data;

    public AddTagData getData() {
        return data;
    }

    public void setData(AddTagData data) {
        this.data = data;
    }
}
