package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class BondPhoneNumberActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bond_phonenumber);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
    }
}
