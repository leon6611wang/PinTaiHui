package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FullSearchCardData;

public class FullSearchCardResult extends BaseResult {
    private FullSearchCardData data;

    public FullSearchCardData getData() {
        return data;
    }

    public void setData(FullSearchCardData data) {
        this.data = data;
    }
}
