package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.chic.utils.LogUtils;
import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.BindMobile;
import com.zhiyu.quanzhu.model.result.BindMobileResult;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PhoneNumberUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

public class BondPhoneNumberActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, clearPhoneNumberLayout, getVertifyCodeLayout;
    private TextView titleTextView, getVertifyCodeTextView, nextButtonTextView;
    private EditText phonenumEditText, vertifyCodeEdit, yaoqingmaEditText;
    private String phoneNumber, vertifyCode;
    private Timer timer;
    private TimerTask task;
    private final int COUNT = 60;
    private int timeCount = COUNT;
    private YNDialog ynDialog;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<BondPhoneNumberActivity> bindPhoneNumberActivityWeakReference;

        public MyHandler(BondPhoneNumberActivity activity) {
            bindPhoneNumberActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BondPhoneNumberActivity activity = bindPhoneNumberActivityWeakReference.get();
            switch (msg.what) {
                case 0:
                    if (activity.timeCount > 0) {
                        activity.getVertifyCodeLayout.setClickable(false);
                        activity.getVertifyCodeTextView.setText("获取验证码(" + activity.timeCount + ")");
                        activity.getVertifyCodeTextView.setTextColor(activity.getResources().getColor(R.color.text_color_gray));
                    } else {
                        activity.getVertifyCodeLayout.setClickable(true);
                        activity.getVertifyCodeTextView.setText("获取验证码");
                        activity.getVertifyCodeTextView.setTextColor(activity.getResources().getColor(R.color.text_color_yellow));
                    }
                    break;
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    break;
                case 2:
                    if (200 == activity.bindMobileResult.getCode()) {
                        activity.pageChange();
                    } else if (1003 == activity.bindMobileResult.getCode()) {
                        activity.ynDialog.show();
                        activity.ynDialog.setTitle(activity.bindMobileResult.getMsg());
                    } else if (500 == activity.bindMobileResult.getCode()) {
                        MessageToast.getInstance(activity).show(activity.bindMobileResult.getMsg());
                    } else if (1005 == activity.bindMobileResult.getCode()) {
                        MessageToast.getInstance(activity).show(activity.bindMobileResult.getMsg());
                    }
                    break;
                case 3:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.pageChange();
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
        setContentView(R.layout.activity_bond_phonenumber);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initTimerTask();
        initViews();
        initDialogs();
    }

    private void initDialogs() {
        ynDialog = new YNDialog(this, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                confirmBindMobile();
            }
        });
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("绑定手机号");
        clearPhoneNumberLayout = findViewById(R.id.clearPhoneNumberLayout);
        clearPhoneNumberLayout.setOnClickListener(this);
        getVertifyCodeLayout = findViewById(R.id.getVertifyCodeLayout);
        getVertifyCodeLayout.setOnClickListener(this);
        getVertifyCodeTextView = findViewById(R.id.getVertifyCodeTextView);
        nextButtonTextView = findViewById(R.id.nextButtonTextView);
        nextButtonTextView.setOnClickListener(this);
        phonenumEditText = findViewById(R.id.phonenumEditText);
        phonenumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneNumber = phonenumEditText.getText().toString().trim();
                if (!StringUtils.isNullOrEmpty(phoneNumber)) {
                    clearPhoneNumberLayout.setVisibility(View.VISIBLE);
                    if (PhoneNumberUtils.getInstance().isMobileNO(phoneNumber)) {
                        getVertifyCodeTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                        if (timeCount > 0 && timeCount < 60) {
                            getVertifyCodeLayout.setClickable(false);
                        } else {
                            getVertifyCodeLayout.setClickable(true);
                        }
                    } else {
                        getVertifyCodeTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
                        getVertifyCodeLayout.setClickable(false);
                    }

                } else {
                    clearPhoneNumberLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        vertifyCodeEdit = findViewById(R.id.vertifyCodeEdit);
        vertifyCodeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vertifyCode = vertifyCodeEdit.getText().toString().trim();
                if (!StringUtils.isNullOrEmpty(vertifyCode)) {
                    nextButtonTextView.setBackground(getResources().getDrawable(R.mipmap.button_yellow_bg));
                    nextButtonTextView.setClickable(true);
                } else {
                    nextButtonTextView.setBackground(getResources().getDrawable(R.drawable.shape_oval_solid_bg_bbbbbbb));
                    nextButtonTextView.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        yaoqingmaEditText = findViewById(R.id.yaoqingmaEditText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.clearPhoneNumberLayout:
                phonenumEditText.setText(null);
                break;
            case R.id.getVertifyCodeLayout:
                SoftKeyboardUtil.hideSoftKeyBoard(getWindow());
                if (PhoneNumberUtils.getInstance().isMobileNO(phoneNumber)) {
                    getVertifiyCode();
                    timeCount = COUNT;
                    Field field;
                    try {
                        field = TimerTask.class.getDeclaredField("state");
                        field.setAccessible(true);
                        field.set(task, 0);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    timer.schedule(task, 0, 1000);
                } else {
                    MessageToast.getInstance(this).show("请输入正确手机号");
                }
                break;
            case R.id.nextButtonTextView:
                SoftKeyboardUtil.hideSoftKeyBoard(getWindow());
                if (StringUtils.isNullOrEmpty(phoneNumber) || !PhoneNumberUtils.getInstance().isMobileNO(phoneNumber)) {
                    MessageToast.getInstance(this).show("请输入正确的手机号码");
                    break;
                }
                if (StringUtils.isNullOrEmpty(vertifyCode)) {
                    MessageToast.getInstance(this).show("请输入短信验证码");
                    break;
                }
                bindMobile();
                break;

        }
    }

    private void pageChange() {
        if (SPUtils.getInstance().getUserBindPhone(BaseApplication.applicationContext) &&
                SPUtils.getInstance().getUserFillProfile(BaseApplication.applicationContext) &&
                SPUtils.getInstance().getUserChooseInterest(BaseApplication.applicationContext)) {
            Intent homeIntent = new Intent(this, HomeActivity.class);
            startActivity(homeIntent);
        } else {
            if (!SPUtils.getInstance().getUserHasPwd(BaseApplication.applicationContext)) {
                Intent intent = new Intent(this, SetLoginPwdActivity.class);
                startActivity(intent);
            } else if (!SPUtils.getInstance().getUserFillProfile(BaseApplication.applicationContext)) {
                Intent intent = new Intent(this, CompleteUserProfileActivity.class);
                startActivity(intent);
            } else if (!SPUtils.getInstance().getUserChooseInterest(BaseApplication.applicationContext)) {
                Intent intent = new Intent(this, HobbySelectActivity.class);
                startActivity(intent);
            }
        }
        finish();
    }

    private void initTimerTask() {
        if (null == timer) {
            timer = new Timer();
        }
        if (null == task) {
            task = new TimerTask() {
                @Override
                public void run() {
                    timeCount--;
                    if (timeCount >= 0) {
                        Message message = myHandler.obtainMessage(0);
                        message.sendToTarget();
                    }
                }
            };
        }
    }

    private BaseResult baseResult;

    private void getVertifiyCode() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GET_VERTIFY_CODE);
        params.addBodyParameter("mobile", phoneNumber);
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

    private BindMobileResult bindMobileResult;

    private void bindMobile() {
        SPUtils.getInstance().saveUserPhoneNum(BaseApplication.applicationContext, phoneNumber);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.BIND_PHONE_NUMBER);
        params.addBodyParameter("mobile", phoneNumber);
        params.addBodyParameter("code", vertifyCode);
        params.addBodyParameter("invite_code", yaoqingmaEditText.getText().toString().trim());
        params.addBodyParameter("type", String.valueOf(SPUtils.getInstance().getUserBindType(BaseApplication.applicationContext)));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("绑定手机号：" + result);
                bindMobileResult = GsonUtils.GsonToBean(result, BindMobileResult.class);
                if (bindMobileResult.getCode() == 200) {
                    SPUtils.getInstance().userBindPhone(BaseApplication.applicationContext);
                }
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("绑定手机号：" + ex.toString());
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

    private void confirmBindMobile() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CONFIRM_BIND_MOBILE);
        params.addBodyParameter("id", String.valueOf(bindMobileResult.getData().getId()));
        params.addBodyParameter("type", String.valueOf(SPUtils.getInstance().getUserBindType(BaseApplication.applicationContext)));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("确定绑定: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                if (baseResult.getCode() == 200) {
                    SPUtils.getInstance().userBindPhone(BaseApplication.applicationContext);
                }
                Message message = myHandler.obtainMessage(3);
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
