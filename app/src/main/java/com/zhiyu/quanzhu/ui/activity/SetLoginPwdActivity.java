package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.leon.chic.utils.SPUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

public class SetLoginPwdActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout showPwdLayout, clearPwdLayout;
    private EditText pwdEdit, inviteCodeEditText;
    private ImageView clearPwdImageView, showPwdImageView;
    private TextView nextButtonTextView;
    private boolean isShowPwd = false;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<SetLoginPwdActivity> activityWeakReference;

        public MyHandler(SetLoginPwdActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SetLoginPwdActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.pageChange();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_login_pwd);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
    }

    private void initViews() {
        showPwdLayout = findViewById(R.id.showPwdLayout);
        showPwdLayout.setOnClickListener(this);
        showPwdImageView = findViewById(R.id.showPwdImageView);
        pwdEdit = findViewById(R.id.pwdEdit);
        inviteCodeEditText = findViewById(R.id.inviteCodeEditText);
        clearPwdLayout = findViewById(R.id.clearPwdLayout);
        clearPwdLayout.setOnClickListener(this);
        clearPwdImageView = findViewById(R.id.clearPwdImageView);
        nextButtonTextView = findViewById(R.id.nextButtonTextView);
        nextButtonTextView.setOnClickListener(this);
        pwdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwdStr = pwdEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(pwdStr)) {
                    clearPwdImageView.setVisibility(View.VISIBLE);
                    nextButtonTextView.setBackground(getResources().getDrawable(R.mipmap.button_yellow_bg));
                    nextButtonTextView.setClickable(true);
                } else {
                    clearPwdImageView.setVisibility(View.GONE);
                    nextButtonTextView.setBackground(getResources().getDrawable(R.drawable.shape_oval_solid_bg_bbbbbbb));
                    nextButtonTextView.setClickable(false);
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
            case R.id.showPwdLayout:
                if (isShowPwd) {
                    isShowPwd = false;
                    pwdEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    Glide.with(this).load(R.mipmap.eye_close).into(showPwdImageView);
                } else {
                    isShowPwd = true;
                    pwdEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    Glide.with(this).load(R.mipmap.eye_open).into(showPwdImageView);
                }
                break;
            case R.id.clearPwdLayout:
                pwdEdit.setText(null);
                break;
            case R.id.nextButtonTextView:
                SoftKeyboardUtil.hideSoftKeyBoard(getWindow());
                if (pwdEdit.getText().toString().trim().length() < 6) {
                    MessageToast.getInstance(this).show("登录密码不小于6位");
                } else if (pwdEdit.getText().toString().trim().length() > 14) {
                    MessageToast.getInstance(this).show("登录密码不大于14位");
                } else {
                    setPwd();
                }

                break;
        }
    }

    private void pageChange() {
        if (!SPUtils.getInstance().getUserFillProfile(BaseApplication.applicationContext)) {
            Intent intent = new Intent(this, CompleteUserProfileActivity.class);
            startActivity(intent);
        } else if (!SPUtils.getInstance().getUserChooseInterest(BaseApplication.applicationContext)) {
            Intent intent = new Intent(this, HobbySelectActivity.class);
            startActivity(intent);
        } else if (SPUtils.getInstance().getUserFillProfile(BaseApplication.applicationContext) &&
                SPUtils.getInstance().getUserChooseInterest(BaseApplication.applicationContext)) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            Intent intent = new Intent(SetLoginPwdActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        //继续执行父类其他点击事件
        return super.onKeyUp(keyCode, event);
    }


    private BaseResult baseResult;

    private void setPwd() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SET_LOGIN_PWD);
        params.addBodyParameter("password", pwdEdit.getText().toString().trim());
        params.addBodyParameter("invite_code", inviteCodeEditText.getText().toString().trim());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("设置密码: "+result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                if (200 == baseResult.getCode()) {
                    SPUtils.getInstance().userHasPwd(BaseApplication.applicationContext);
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("设置密码: "+ex.toString());
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
