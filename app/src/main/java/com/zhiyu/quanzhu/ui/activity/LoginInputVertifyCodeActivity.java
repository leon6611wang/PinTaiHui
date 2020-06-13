package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leon.chic.utils.SPUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.LoginTokenResult;
import com.zhiyu.quanzhu.ui.dialog.WaitDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.PhoneCode;
import com.zhiyu.quanzhu.utils.AppManager;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

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
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.loginTokenResult.getMsg());
                    if (activity.loginTokenResult.getCode() == 200) {
                        activity.pageChange();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_input_vertifycode);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
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

    @Override
    public void finish() {
        super.finish();
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
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.VERIFY_LOGIN);
        params.addBodyParameter("mobile", phoneNumber);
        params.addBodyParameter("code", code);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("验证码登录: " + result);
                loginTokenResult = GsonUtils.GsonToBean(result, LoginTokenResult.class);
                if (200 == loginTokenResult.getCode()) {
                    SPUtils.getInstance().userLogin(BaseApplication.applicationContext);
                    SPUtils.getInstance().saveUserToken(BaseApplication.applicationContext, loginTokenResult.getToken());
                    SPUtils.getInstance().saveIMToken(BaseApplication.applicationContext, loginTokenResult.getData().getToken());
                    SPUtils.getInstance().saveUserAvatar(BaseApplication.applicationContext, loginTokenResult.getData().getUserinfo().getAvatar());
                    SPUtils.getInstance().saveUserName(BaseApplication.applicationContext, loginTokenResult.getData().getUserinfo().getUsername());
                    SPUtils.getInstance().saveUserInfoStatus(BaseApplication.applicationContext, loginTokenResult.getData().getUserinfo().isHas_pwd(),
                            loginTokenResult.getData().getUserinfo().isBind_mobile(),
                            loginTokenResult.getData().getUserinfo().isFill_profile(),
                            loginTokenResult.getData().getUserinfo().isChoose_hobby());
                }
                Message message = myHandler.obtainMessage(2);
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

    private void pageChange() {
        if (SPUtils.getInstance().getUserBindPhone(BaseApplication.applicationContext) &&
                SPUtils.getInstance().getUserFillProfile(BaseApplication.applicationContext) &&
                SPUtils.getInstance().getUserChooseInterest(BaseApplication.applicationContext)) {
            Intent homeIntent = new Intent(this, HomeActivity.class);
            startActivity(homeIntent);
        } else {
            if (!SPUtils.getInstance().getUserBindPhone(BaseApplication.applicationContext)) {
                Intent intent = new Intent(this, BondPhoneNumberActivity.class);
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


}
