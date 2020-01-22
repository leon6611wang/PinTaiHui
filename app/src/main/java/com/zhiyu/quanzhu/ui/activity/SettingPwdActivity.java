package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 设置密码
 */
public class SettingPwdActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pwd);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
    }
}
