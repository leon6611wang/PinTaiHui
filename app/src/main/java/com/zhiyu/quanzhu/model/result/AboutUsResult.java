package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.AboutUs;

public class AboutUsResult extends BaseResult {
    private AboutUs data;

    public AboutUs getData() {
        return data;
    }

    public void setData(AboutUs data) {
        this.data = data;
    }
}
