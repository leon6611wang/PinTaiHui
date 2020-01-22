package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.GuiGePiPeiData;

public class GuiGePiPeiResult extends BaseResult {
    private GuiGePiPeiData data;

    public GuiGePiPeiData getData() {
        return data;
    }

    public void setData(GuiGePiPeiData data) {
        this.data = data;
    }
}
