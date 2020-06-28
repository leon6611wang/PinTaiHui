package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.ShareData;

public class ShareResult extends BaseResult {
    private ShareData data;

    public ShareData getData() {
        return data;
    }

    public void setData(ShareData data) {
        this.data = data;
    }
}
