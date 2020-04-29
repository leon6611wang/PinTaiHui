package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.MyVisitor;

public class MyVisitorResult extends BaseResult {
    private MyVisitor data;

    public MyVisitor getData() {
        return data;
    }

    public void setData(MyVisitor data) {
        this.data = data;
    }
}
