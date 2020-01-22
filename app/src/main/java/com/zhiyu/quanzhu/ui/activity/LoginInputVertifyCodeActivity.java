package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.LoginTokenResult;
import com.zhiyu.quanzhu.ui.dialog.WaitDialog;
import com.zhiyu.quanzhu.ui.widget.PhoneCode;
import com.zhiyu.quanzhu.utils.AppManager;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

public class LoginInputVertifyCodeActivity extends BaseActivity implements View.OnClickListener {
    private PhoneCode phoneCodeView;
    private WaitDialog waitDialog;
    private ImageView closeImageView;
    private String phoneNumber;
    private TextView getVertifyCodeTextView, backTextView;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<LoginInputVertifyCodeActivity> activityWeakReference;

        public MyHandler(LoginInputVertifyCodeActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginInputVertifyCodeActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    Toast.makeText(activity, activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(activity, activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (activity.loginTokenResult.getCode() == 200) {
                        SharedPreferencesUtils.getInstance(activity).saveUserToken(activity.loginTokenResult.getData().getToken());
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_input_vertifycode);
        ScreentUtils.getInstance().setStatusBarLightMode(this,false);
        phoneNumber = getIntent().getStringExtra("phonenumber");
        initViews();
        initDialog();
    }

    private void initViews() {
        phoneCodeView = findViewById(R.id.phoneCodeView);
        phoneCodeView.setOnInputListener(new PhoneCode.OnInputListener() {
            @Override
            public void onSucess(String code) {
                verifyLogin(code);
//                waitDialog.show();
            }

            @Override
            public void onInput() {
            }
        });
        closeImageView = findViewById(R.id.closeImageView);
        closeImageView.setOnClickListener(this);
        getVertifyCodeTextView = findViewById(R.id.getVertifyCodeTextView);
        getVertifyCodeTextView.setOnClickListener(this);
        backTextView = findViewById(R.id.backTextView);
        backTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeImageView:
                finish();
                break;
            case R.id.getVertifyCodeTextView:
                getVertifiyCode();
                break;
            case R.id.backTextView:
                Intent intent = new Intent(LoginInputVertifyCodeActivity.this, LoginGetVertifyCodeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void initDialog() {
        waitDialog = new WaitDialog(AppManager.getAppManager().currentActivity(), R.style.dialog);
    }

    private BaseResult baseResult;

    private void getVertifiyCode() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GET_VERTIFY_CODE);
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

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private LoginTokenResult loginTokenResult;

    private void verifyLogin(String code) {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.VERIFY_LOGIN);
        params.addBodyParameter("mobile", phoneNumber);
        params.addBodyParameter("code", code);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                loginTokenResult = GsonUtils.GsonToBean(result, LoginTokenResult.class);
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
