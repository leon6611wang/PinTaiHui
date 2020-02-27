package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Circle;

public class CircleDetailResult extends BaseResult {
    private Circle data;

    public Circle getData() {
        return data;
    }

    public void setData(Circle data) {
        this.data = data;
    }
}
