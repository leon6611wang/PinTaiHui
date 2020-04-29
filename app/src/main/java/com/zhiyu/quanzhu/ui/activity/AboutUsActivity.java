package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.result.AboutUsResult;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.AppUpdateUtils;
import com.zhiyu.quanzhu.utils.AppUtils;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.CopyBoardUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, yonghufankuilayout;
    private TextView titleTextView, versionTextView, qqTextView, wxTextView, emailTextView, checkVersionTextView,
            copyQQTextView, copyWxTextView, copyEmailTextView;
    private YNDialog ynDialog;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<AboutUsActivity> activityWeakReference;

        public MyHandler(AboutUsActivity aboutUsActivity) {
            activityWeakReference = new WeakReference<>(aboutUsActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            AboutUsActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    if (200 == activity.aboutUsResult.getCode()) {
                        activity.qqTextView.setText(activity.aboutUsResult.getData().getQq());
                        activity.wxTextView.setText(activity.aboutUsResult.getData().getWx());
                        activity.emailTextView.setText(activity.aboutUsResult.getData().getEmail());
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initDialogs();
        initViews();
        aboutUsInfo();
    }

    private void initDialogs() {
        ynDialog = new YNDialog(this, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                AppUpdateUtils.getInstance().updateApp(AboutUsActivity.this, aboutUsResult.getData().getVersion().getAndroid().getUrl());
            }
        });
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("关于我们");
        versionTextView = findViewById(R.id.versionTextView);
        versionTextView.setText("当前版本 v" + AppUtils.getInstance().getVersionName(this));
        qqTextView = findViewById(R.id.qqTextView);
        wxTextView = findViewById(R.id.wxTextView);
        emailTextView = findViewById(R.id.emailTextView);
        yonghufankuilayout = findViewById(R.id.yonghufankuilayout);
        yonghufankuilayout.setOnClickListener(this);
        checkVersionTextView = findViewById(R.id.checkVersionTextView);
        checkVersionTextView.setOnClickListener(this);
        copyQQTextView = findViewById(R.id.copyQQTextView);
        copyQQTextView.setOnClickListener(this);
        copyWxTextView = findViewById(R.id.copyWxTextView);
        copyWxTextView.setOnClickListener(this);
        copyEmailTextView = findViewById(R.id.copyEmailTextView);
        copyEmailTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.yonghufankuilayout:
                Intent yonghufankuiIntent = new Intent(this, FeedBackActivity.class);
                startActivity(yonghufankuiIntent);
                break;
            case R.id.checkVersionTextView:
                if (null != aboutUsResult) {
                    checkVersion();
                } else {
                    MessageToast.getInstance(AboutUsActivity.this).show("请稍后...");
                }
                break;
            case R.id.copyQQTextView:
                CopyBoardUtils.getInstance().copy(this, qqTextView.getText().toString().trim());
                MessageToast.getInstance(this).show("QQ群号复制成功");
                break;
            case R.id.copyWxTextView:
                CopyBoardUtils.getInstance().copy(this, wxTextView.getText().toString().trim());
                MessageToast.getInstance(this).show("微信公众号复制成功");
                break;
            case R.id.copyEmailTextView:
                CopyBoardUtils.getInstance().copy(this, emailTextView.getText().toString().trim());
                MessageToast.getInstance(this).show("邮箱地址复制成功");
                break;
        }
    }

    private AboutUsResult aboutUsResult;

    private void aboutUsInfo() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ABOUT_US);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                aboutUsResult = GsonUtils.GsonToBean(result, AboutUsResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("about us: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void checkVersion() {
        String localVersion = AppUtils.getInstance().getVersionName(this);
        String version = aboutUsResult.getData().getVersion().getAndroid().getVersion();
        localVersion = localVersion.replace(".", "");
        version = version.replace(".", "");
        int localVersionInt = Integer.parseInt(localVersion);
        int versionInt = Integer.parseInt(version);
        if (localVersionInt < versionInt) {
            ynDialog.show();
            ynDialog.setTitle("发现新版本，是否立即更新？");
        } else {
            MessageToast.getInstance(this).show("当前已是最新版本~");
        }
    }

}
