package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.FooterPrintCommentData;

public class FooterPrintCommentResult extends BaseResult {
    private FooterPrintCommentData data;

    public FooterPrintCommentData getData() {
        return data;
    }

    public void setData(FooterPrintCommentData data) {
        this.data = data;
    }
}
