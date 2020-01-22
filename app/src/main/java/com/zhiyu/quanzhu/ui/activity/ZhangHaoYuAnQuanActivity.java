package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;

public class ZhangHaoYuAnQuanActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout xiugaishoujihaolayout, xiugaimimalayout, bindqqlayout, bindwechatlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhanghaoyuanquan);
        initViews();
    }

    private void initViews() {
        xiugaishoujihaolayout = findViewById(R.id.xiugaishoujihaolayout);
        xiugaishoujihaolayout.setOnClickListener(this);
        xiugaimimalayout = findViewById(R.id.xiugaimimalayout);
        xiugaimimalayout.setOnClickListener(this);
        bindqqlayout = findViewById(R.id.bindqqlayout);
        bindqqlayout.setOnClickListener(this);
        bindwechatlayout = findViewById(R.id.bindwechatlayout);
        bindwechatlayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xiugaishoujihaolayout:
                Intent editPhonenumIntent=new Intent(this,EditPhoneNum1Activity.class);
                startActivity(editPhonenumIntent);
                break;
            case R.id.xiugaimimalayout:
                Intent editPwdIntent=new Intent(this,EditPwd1Activity.class);
                startActivity(editPwdIntent);
                break;
            case R.id.bindqqlayout:

                break;
            case R.id.bindwechatlayout:

                break;
        }
    }
}
