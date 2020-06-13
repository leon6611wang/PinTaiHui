package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.QuanZhuAssistant;
import com.zhiyu.quanzhu.model.data.QuanZhuAssistantData;

import java.util.List;

public class QuanZhuAssistantResult extends BaseResult {
    private QuanZhuAssistantData data;

    public QuanZhuAssistantData getData() {
        return data;
    }

    public void setData(QuanZhuAssistantData data) {
        this.data = data;
    }
}
