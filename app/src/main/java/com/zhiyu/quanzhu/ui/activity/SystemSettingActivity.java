package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leon.chic.utils.SPUtils;
import com.tencent.tauth.Tencent;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.ui.dialog.AppraiseUsDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideCacheUtil;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

import io.rong.imlib.RongIMClient;

public class SystemSettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout zhanghaoanquanlayout, xiaoxitongzhilayout, yinsilayout, qingchuhuancunlayout, guanyuwomenlayout, pingjiawomenlayout;
    private LinearLayout backLayout;
    private TextView titleTextView, logoutTextView;
    private TextView cacheSizeTextView;
    private YNDialog ynDialog;
    private AppraiseUsDialog appraiseUsDialog;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<SystemSettingActivity> weakReference;

        public MyHandler(SystemSettingActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SystemSettingActivity activity = weakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (activity.baseResult.getCode() == 200) {
                        activity.logout();
                    }
                    break;
                case 99:
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试.");
                    break;
            }
        }
    }

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
                logoutService();
            }
        });
        appraiseUsDialog = new AppraiseUsDialog(this, R.style.dialog);
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
        Tencent mTencent = Tencent.createInstance("101762258", getApplicationContext());
        if (mTencent.isSessionValid()) {
            mTencent.logout(this);
        }
        SPUtils.getInstance().userLogout(BaseApplication.applicationContext);
        RongIMClient.getInstance().logout();
        Intent loginIntent = new Intent(this, LoginGetVertifyCodeActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private BaseResult baseResult;

    private void logoutService() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.LOGOUT);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
