package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.SystemSettingInfoResult;
import com.zhiyu.quanzhu.ui.dialog.PrivacySelectDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.SwitchButton;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

public class PrivacySettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private SwitchButton mSwitchButton;
    private LinearLayout openLayout;
    private TextView openTextView;
    private ImageView openImageView;
    private PrivacySelectDialog privacySelectDialog;
    private int contactstatus;
    private int friendstatus;
    private boolean isFirst = true;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<PrivacySettingActivity> activityWeakReference;

        public MyHandler(PrivacySettingActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PrivacySettingActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    if (activity.infoResult.getCode() == 200) {
                        activity.friendstatus = activity.infoResult.getData().getFriendstatus();
                        if (activity.infoResult.getData().getFriendstatus() == 1) {
                            activity.mSwitchButton.open();
                        } else {
                            activity.mSwitchButton.close();
                        }
                        activity.contactstatus = activity.infoResult.getData().getContactstatus();
                        activity.openTextView.setText(activity.infoResult.getData().getContactstatus_desc());
                    }
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_setting);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initDialogs();
        initViews();
        systemSetInfo();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("隐私设置");
        mSwitchButton = findViewById(R.id.mSwitchButton);
        mSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                friendstatus = isOpen ? 1 : 0;
                if (!isFirst) {
                    systemSetting();
                }
                isFirst = false;

            }
        });
        openLayout = findViewById(R.id.openLayout);
        openLayout.setOnClickListener(this);
        openTextView = findViewById(R.id.openTextView);
        openImageView = findViewById(R.id.openImageView);
    }

    private void initDialogs() {
        privacySelectDialog = new PrivacySelectDialog(this, R.style.dialog, new PrivacySelectDialog.OnPrivacySelectListener() {
            @Override
            public void onPrivacySelect(String privacy, int privacyIndex) {
                contactstatus = privacyIndex;
                openTextView.setText(privacy);
                systemSetting();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.openLayout:
                privacySelectDialog.show();
                break;
        }
    }

    private SystemSettingInfoResult infoResult;

    private void systemSetInfo() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SYSTEM_SET_INFO);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("systemSetInfo: " + result);
                infoResult = GsonUtils.GsonToBean(result, SystemSettingInfoResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private BaseResult baseResult;

    private void systemSetting() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SET_SYSTEM_SETTING);
        params.addBodyParameter("friendstatus", String.valueOf(friendstatus));
        params.addBodyParameter("contactstatus", String.valueOf(contactstatus));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("系统消息设置: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("系统消息设置: " + ex.toString());
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
