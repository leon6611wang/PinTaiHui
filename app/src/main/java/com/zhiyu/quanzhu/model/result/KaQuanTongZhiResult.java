package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.KaQuanTongZhiData;

public class KaQuanTongZhiResult extends BaseResult {
    private KaQuanTongZhiData data;

    public KaQuanTongZhiData getData() {
        return data;
    }

    public void setData(KaQuanTongZhiData data) {
        this.data = data;
    }
}
