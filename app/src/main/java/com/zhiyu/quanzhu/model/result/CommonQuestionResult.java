package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.data.CommonQuesionData;

public class CommonQuestionResult extends BaseResult {
    private CommonQuesionData data;

    public CommonQuesionData getData() {
        return data;
    }

    public void setData(CommonQuesionData data) {
        this.data = data;
    }
}
