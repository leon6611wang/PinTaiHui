package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;

public class YinSiSheZhiActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout haoyouyanzhenglayout, gongkailayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yinsishezhi);
        initViews();
    }

    private void initViews() {
        haoyouyanzhenglayout = findViewById(R.id.haoyouyanzhenglayout);
        haoyouyanzhenglayout.setOnClickListener(this);
        gongkailayout = findViewById(R.id.gongkailayout);
        gongkailayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.haoyouyanzhenglayout:

                break;

            case R.id.gongkailayout:

                break;
        }
    }
}
