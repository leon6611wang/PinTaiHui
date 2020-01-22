package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class GuanYuWoMenActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout backLayout,yonghufankuilayout;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guanyuwomen);
        ScreentUtils.getInstance().setStatusBarLightMode(this,true);
        initViews();
    }

    private void initViews(){
        backLayout=findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView=findViewById(R.id.titleTextView);
        titleTextView.setText("关于我们");
        yonghufankuilayout=findViewById(R.id.yonghufankuilayout);
        yonghufankuilayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backLayout:
                finish();
                break;
            case R.id.yonghufankuilayout:
                Intent yonghufankuiIntent=new Intent(this,YongHuFanKuiActivity.class);
                startActivity(yonghufankuiIntent);
                break;
        }
    }
}
