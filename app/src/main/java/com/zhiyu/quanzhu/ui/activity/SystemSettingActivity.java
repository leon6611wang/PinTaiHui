package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class SystemSettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout zhanghaoanquanlayout, xiaoxitongzhilayout, yinsilayout, qingchuhuancunlayout, guanyuwomenlayout, pingjiawomenlayout;
    private LinearLayout backLayout;
    private TextView titleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemsetting);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
    }

    private void initViews() {
        backLayout=findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView=findViewById(R.id.titleTextView);
        titleTextView.setText("系统设置");
        zhanghaoanquanlayout = findViewById(R.id.zhanghaoanquanlayout);
        zhanghaoanquanlayout.setOnClickListener(this);
        xiaoxitongzhilayout = findViewById(R.id.xiaoxitongzhilayout);
        xiaoxitongzhilayout.setOnClickListener(this);
        yinsilayout = findViewById(R.id.yinsilayout);
        yinsilayout.setOnClickListener(this);
        qingchuhuancunlayout = findViewById(R.id.qingchuhuancunlayout);
        qingchuhuancunlayout.setOnClickListener(this);
        guanyuwomenlayout = findViewById(R.id.guanyuwomenlayout);
        guanyuwomenlayout.setOnClickListener(this);
        pingjiawomenlayout = findViewById(R.id.pingjiawomenlayout);
        pingjiawomenlayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.zhanghaoanquanlayout:
                Intent zhanghaoanquanIntent=new Intent(this,ZhangHaoYuAnQuanActivity.class);
                startActivity(zhanghaoanquanIntent);
                break;
            case R.id.xiaoxitongzhilayout:

                break;
            case R.id.yinsilayout:

                break;
            case R.id.qingchuhuancunlayout:

                break;
            case R.id.guanyuwomenlayout:
                Intent guanyuwomenIntent=new Intent(this,GuanYuWoMenActivity.class);
                startActivity(guanyuwomenIntent);
                break;
            case R.id.pingjiawomenlayout:

                break;
        }
    }
}
