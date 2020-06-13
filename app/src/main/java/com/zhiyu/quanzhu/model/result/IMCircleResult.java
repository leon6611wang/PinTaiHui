package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.IMCircle;

import java.util.List;

public class IMCircleResult extends BaseResult {
    private List<IMCircle> data;

    public List<IMCircle> getData() {
        return data;
    }

    public void setData(List<IMCircle> data) {
        this.data = data;
    }
}
