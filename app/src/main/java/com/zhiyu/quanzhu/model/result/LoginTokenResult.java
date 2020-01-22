package com.zhiyu.quanzhu.model.result;

import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.LoginToken;

public class LoginTokenResult extends BaseResult {
    private LoginToken data;

    public LoginToken getData() {
        return data;
    }

    public void setData(LoginToken data) {
        this.data = data;
    }
}
