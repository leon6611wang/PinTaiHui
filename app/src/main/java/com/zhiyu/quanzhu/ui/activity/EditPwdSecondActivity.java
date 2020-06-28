package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

/**
 * 修改密码第二步
 */
public class EditPwdSecondActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private int payOrLoginPwd = 0;//0:登录密码；1:支付密码
    private boolean canSee;
    private LinearLayout eyeLayout;
    private ImageView eyeImageView;
    private EditText pwdEditText;
    private String pwd;
    private TextView confirmTextView;
    private String code, mobile;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<EditPwdSecondActivity> activityWeakReference;

        public MyHandler(EditPwdSecondActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            EditPwdSecondActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (activity.baseResult.getCode() == 200) {
                        Intent intent = new Intent();
                        intent.putExtra("isfinish", true);
                        activity.setResult(996, intent);
                        activity.finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pwd_second);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        payOrLoginPwd = getIntent().getIntExtra("payOrLoginPwd", 0);
        code = getIntent().getStringExtra("code");
        mobile = getIntent().getStringExtra("mobile");
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        eyeLayout = findViewById(R.id.eyeLayout);
        eyeLayout.setOnClickListener(this);
        eyeImageView = findViewById(R.id.eyeImageView);
        pwdEditText = findViewById(R.id.pwdEditText);
        switch (payOrLoginPwd) {
            case 0:
                titleTextView.setText("更改登录密码");
                pwdEditText.setHint("请输入新密码(6-14位组合)");
                pwdEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                pwdEditText.setMaxEms(6);
                pwdEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
                break;
            case 1:
                titleTextView.setText("更改支付密码");
                pwdEditText.setHint("请输入新密码(6位数字)");
                pwdEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                pwdEditText.setMaxEms(6);
                pwdEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                break;
        }
        pwdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pwd = pwdEditText.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.eyeLayout:
                if (!canSee) {
                    pwdEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pwdEditText.setSelection(pwdEditText.getText().toString().trim().length());
                    canSee = true;
                    eyeImageView.setImageDrawable(getResources().getDrawable(R.mipmap.eye_open));
                } else {
                    pwdEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    canSee = false;
                    eyeImageView.setImageDrawable(getResources().getDrawable(R.mipmap.eye_close));
                    pwdEditText.setSelection(pwdEditText.getText().toString().trim().length());
                }
                break;
            case R.id.confirmTextView:
                if (!StringUtils.isNullOrEmpty(pwd)) {
                    int length = pwd.length();
                    switch (payOrLoginPwd) {
                        case 0:
                            if (length < 6 || length > 14) {
                                MessageToast.getInstance(this).show("登录密码长度6-14");
                                break;
                            }
                            setLoginPwd();
                            break;
                        case 1:
                            if (length != 6) {
                                MessageToast.getInstance(this).show("请确认6位支付密码");
                                break;
                            }
                            setPayPwd();
                            break;
                    }
                } else {
                    MessageToast.getInstance(this).show("请设置密码!");
                }

                break;
        }
    }

    private BaseResult baseResult;

    /**
     * 设置支付密码
     */
    private void setPayPwd() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SET_PAY_PWD);
        params.addBodyParameter("password", pwd);
        params.addBodyParameter("code", code);
        params.addBodyParameter("mobile", mobile);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("支付密码: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("支付密码: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 设置登录密码
     */
    private void setLoginPwd() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SET_LOGIN_PWD);
        params.addBodyParameter("password", pwd);
        params.addBodyParameter("code", code);
        params.addBodyParameter("mobile", mobile);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("登录密码: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("登录密码: " + ex.toString());
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
