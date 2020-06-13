package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.ZhiFuTongZhiData;

public class ZhiFuTongZhiResult extends BaseResult {
    private ZhiFuTongZhiData data;

    public ZhiFuTongZhiData getData() {
        return data;
    }

    public void setData(ZhiFuTongZhiData data) {
        this.data = data;
    }
}
