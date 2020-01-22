package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.LoginTokenResult;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.PhoneNumberUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

public class LoginByPwdActivity extends BaseActivity implements View.OnClickListener {
    private ImageView closeImageView, seePwdImageView;
    private EditText phoneNumberEdit, pwdEditText;
    private RelativeLayout clearPhoneNumberLayout, seePwdLayout;
    private TextView loginTextView, verifyCodeLoginTextView, forgetPwdTextView;
    private String phoneNumber, pwd;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<LoginByPwdActivity> activityWeakReference;

        public MyHandler(LoginByPwdActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginByPwdActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    Toast.makeText(activity, activity.loginTokenResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (activity.loginTokenResult.getCode() == 200) {
                        SharedPreferencesUtils.getInstance(activity).saveUserToken(activity.loginTokenResult.getData().getToken());
                        activity.finish();
                    }

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_pwd);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        initViews();
    }

    private void initViews() {
        closeImageView = findViewById(R.id.closeImageView);
        closeImageView.setOnClickListener(this);
        seePwdImageView = findViewById(R.id.seePwdImageView);
        phoneNumberEdit = findViewById(R.id.phoneNumberEdit);
        phoneNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneNumber = phoneNumberEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    clearPhoneNumberLayout.setVisibility(View.VISIBLE);
                } else {
                    clearPhoneNumberLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pwdEditText = findViewById(R.id.pwdEditText);
        pwdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pwd = pwdEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)) {
                    seePwdLayout.setVisibility(View.VISIBLE);
                } else {
                    seePwdLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clearPhoneNumberLayout = findViewById(R.id.clearPhoneNumberLayout);
        clearPhoneNumberLayout.setOnClickListener(this);
        seePwdLayout = findViewById(R.id.seePwdLayout);
        seePwdLayout.setOnClickListener(this);
        loginTextView = findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(this);
        verifyCodeLoginTextView = findViewById(R.id.verifyCodeLoginTextView);
        verifyCodeLoginTextView.setOnClickListener(this);
        forgetPwdTextView = findViewById(R.id.forgetPwdTextView);
        forgetPwdTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeImageView:
                finish();
                break;
            case R.id.clearPhoneNumberLayout:
                phoneNumberEdit.setText(null);
                break;
            case R.id.seePwdLayout:
                pwsShow();
                break;
            case R.id.loginTextView:
                if (PhoneNumberUtils.getInstance().isMobileNO(phoneNumber)) {
                    loginByPwd();
                } else {
                    Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.verifyCodeLoginTextView:
                Intent verifyLoginIntent = new Intent(LoginByPwdActivity.this, LoginGetVertifyCodeActivity.class);
                startActivity(verifyLoginIntent);
                break;
            case R.id.forgetPwdTextView:

                break;
        }
    }


    private boolean isPwdShow = false;

    private void pwsShow() {
        if (isPwdShow) {
            hidePwd();
            isPwdShow = false;
            seePwdImageView.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_unsee));
        } else {
            showPwd();
            isPwdShow = true;
            seePwdImageView.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_see));
        }
    }

    private void showPwd() {
        pwdEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }

    private void hidePwd() {
        pwdEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private LoginTokenResult loginTokenResult;

    private void loginByPwd() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.LOGIN_BY_PWD);
        params.addBodyParameter("mobile", phoneNumber);
        params.addBodyParameter("password", pwd);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                loginTokenResult = GsonUtils.GsonToBean(result, LoginTokenResult.class);
                System.out.println(loginTokenResult.getMsg());
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
}
