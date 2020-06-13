package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.ReportDetail;

public class ReportDetailResult extends BaseResult {
    private ReportDetail data;

    public ReportDetail getData() {
        return data;
    }

    public void setData(ReportDetail data) {
        this.data = data;
    }
}
