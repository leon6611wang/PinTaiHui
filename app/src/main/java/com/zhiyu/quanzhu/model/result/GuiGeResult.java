package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.GuiGeData;

public class GuiGeResult extends BaseResult {
    private GuiGeData data;

    public GuiGeData getData() {
        return data;
    }

    public void setData(GuiGeData data) {
        this.data = data;
    }
}
