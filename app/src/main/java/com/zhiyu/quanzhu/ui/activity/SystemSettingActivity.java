package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.dialog.AppraiseUsDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.utils.GlideCacheUtil;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

import io.rong.imlib.RongIMClient;

public class SystemSettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout zhanghaoanquanlayout, xiaoxitongzhilayout, yinsilayout, qingchuhuancunlayout, guanyuwomenlayout, pingjiawomenlayout;
    private LinearLayout backLayout;
    private TextView titleTextView, logoutTextView;
    private TextView cacheSizeTextView;
    private YNDialog ynDialog;
    private AppraiseUsDialog appraiseUsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemsetting);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
        initDialogs();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
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
        logoutTextView = findViewById(R.id.logoutTextView);
        logoutTextView.setOnClickListener(this);
        cacheSizeTextView = findViewById(R.id.cacheSizeTextView);
        String cacheSize = GlideCacheUtil.getInstance().getCacheSize(this);
        cacheSizeTextView.setText(cacheSize);
    }

    private void initDialogs() {
        ynDialog = new YNDialog(this, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                logout();
            }
        });
        appraiseUsDialog=new AppraiseUsDialog(this,R.style.dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.zhanghaoanquanlayout:
                Intent zhanghaoanquanIntent = new Intent(this, AccountSafeActivity.class);
                startActivity(zhanghaoanquanIntent);
                break;
            case R.id.xiaoxitongzhilayout:
                Intent messageIntent = new Intent(this, MessageNotificationActivity.class);
                startActivity(messageIntent);
                break;
            case R.id.yinsilayout:
                Intent privacyIntent = new Intent(this, PrivacySettingActivity.class);
                startActivity(privacyIntent);
                break;
            case R.id.qingchuhuancunlayout:
                GlideCacheUtil.getInstance().clearImageAllCache(this);
                cacheSizeTextView.setText("0.0MB");
                break;
            case R.id.guanyuwomenlayout:
                Intent guanyuwomenIntent = new Intent(this, AboutUsActivity.class);
                startActivity(guanyuwomenIntent);
                break;
            case R.id.pingjiawomenlayout:
                appraiseUsDialog.show();
                break;
            case R.id.logoutTextView:
                ynDialog.show();
                ynDialog.setTitle("确定退出登录？");
                break;
        }
    }

    private void logout() {
        SharedPreferencesUtils.getInstance(this).clearUser();
        RongIMClient.getInstance().logout();
        Intent loginIntent = new Intent(this, LoginByPwdActivity.class);
        startActivity(loginIntent);
        finish();
    }


}
