package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 联系客服
 */
public class ContactCustomerServiceActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout,rightLayout;
    private TextView titleTextView, callServiceTextView,rightTextView;
    private final String phoneNumber = "17375081781";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_customer_service);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout=findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        rightTextView=findViewById(R.id.rightTextView);
        rightTextView.setText("确定");
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("联系客服");
        callServiceTextView = findViewById(R.id.callServiceTextView);
        callServiceTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.callServiceTextView:
                callService2();
                break;
            case R.id.rightLayout:

                break;
        }
    }

    /**
     * 立即拨打
     */
    private void callService1() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 进入拨号界面
     */
    private void callService2() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        startActivity(intent);
    }
}
