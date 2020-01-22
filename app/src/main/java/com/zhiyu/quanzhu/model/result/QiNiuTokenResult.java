package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.QiNiuToken;

public class QiNiuTokenResult extends BaseResult {
    private QiNiuToken data;

    public QiNiuToken getData() {
        return data;
    }

    public void setData(QiNiuToken data) {
        this.data = data;
    }
}
