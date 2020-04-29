package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Hobby;

/**
 * 新增偏好
 */
public class AddHobbyResult extends BaseResult {
    private Hobby data;

    public Hobby getData() {
        return data;
    }

    public void setData(Hobby data) {
        this.data = data;
    }
}
