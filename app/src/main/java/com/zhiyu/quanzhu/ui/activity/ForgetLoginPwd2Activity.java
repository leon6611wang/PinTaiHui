package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ForgetLoginPwd2Activity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private EditText pwdEdit;
    private String password;
    private ImageView pwdShowImageView;
    private TextView goToHomeTextView;
    private String mobile, code;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ForgetLoginPwd2Activity> weakReference;

        public MyHandler(ForgetLoginPwd2Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ForgetLoginPwd2Activity activity = weakReference.get();
            switch (msg.what) {
                case 99:
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试");
                    break;
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_login_pwd_2);
        ScreentUtils.getInstance().setStatusBarLightMode(this,true);
        mobile = getIntent().getStringExtra("mobile");
        code = getIntent().getStringExtra("code");
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("更改密码");
        backLayout.setOnClickListener(this);
        pwdEdit = findViewById(R.id.pwdEdit);
        pwdShowImageView = findViewById(R.id.pwdShowImageView);
        pwdShowImageView.setOnClickListener(this);
        goToHomeTextView = findViewById(R.id.goToHomeTextView);
        goToHomeTextView.setOnClickListener(this);
        pwdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = pwdEdit.getText().toString().trim();
                if (!StringUtils.isNullOrEmpty(password)) {
                    goToHomeTextView.setBackground(getResources().getDrawable(R.mipmap.create_shangquan_btn_bg));
                    goToHomeTextView.setClickable(true);
                } else {
                    goToHomeTextView.setBackground(getResources().getDrawable(R.drawable.shape_oval_solid_bg_bbbbbbb));
                    goToHomeTextView.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.pwdShowImageView:
                pwsShow();
                break;
            case R.id.goToHomeTextView:
                if (pwdEdit.getText().toString().trim().length() < 6) {
                    MessageToast.getInstance(this).show("登录密码不能小于6位");
                } else if (pwdEdit.getText().toString().trim().length() > 14) {
                    MessageToast.getInstance(this).show("登录密码不能超过14位");
                } else {
                    updatePwd();
                }
                break;
        }
    }

    private boolean isPwdShow = false;

    private void pwsShow() {
        if (isPwdShow) {
            hidePwd();
            isPwdShow = false;
            pwdShowImageView.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_unsee));
        } else {
            showPwd();
            isPwdShow = true;
            pwdShowImageView.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_see));
        }
    }

    private void showPwd() {
        pwdEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }

    private void hidePwd() {
        pwdEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }


    private BaseResult baseResult;

    private void updatePwd() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.UPDATE_PWD);
        params.addBodyParameter("password", password);
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("code", code);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("updatepwd: "+result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("updatepwd: "+ex.toString());
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
