package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 提现
 */
public class TiXianActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout backLayout;
    private TextView titleText;
    private LinearLayout tixianfangshilayout;
    private TextView tixianfangshitextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tixian);
        ScreentUtils.getInstance().setStatusBarLightMode(this,true);
        initViews();
        initDialogs();
    }
    private void initDialogs(){

    }

    private void initViews(){
        backLayout=findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleText=findViewById(R.id.titleTextView);
        titleText.setText("提现");
        tixianfangshilayout=findViewById(R.id.tixianfangshilayout);
        tixianfangshilayout.setOnClickListener(this);
        tixianfangshitextview=findViewById(R.id.tixianfangshitextview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backLayout:
                finish();
                break;
            case R.id.tixianfangshilayout:

                break;
        }
    }
}
