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
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leon.chic.utils.H5Utils;
import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.result.LoginTokenResult;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PhoneNumberUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyBoardListener;
import com.zhiyu.quanzhu.utils.WXUtils;
import com.zhiyu.quanzhu.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

public class LoginByPwdActivity extends BaseActivity implements View.OnClickListener, WXEntryActivity.OnWxLoginSuccessListener,SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
    private ImageView closeImageView, seePwdImageView, wxLoginImageView, qqLoginImageView;
    private EditText phoneNumberEdit, pwdEditText;
    private RelativeLayout clearPhoneNumberLayout, seePwdLayout;
    private TextView loginTextView, verifyCodeLoginTextView, forgetPwdTextView, yoonghuxieyiTextView, yinsizhengceTextView;
    private String phoneNumber, pwd;
    private Tencent mTencent;
    private CheckBox checkBox;
    private boolean isCheck = false;
    private String xieyi_url = H5Utils.getInstance().yongHuXieYi(),//用户协议
            yinsi_url = H5Utils.getInstance().yinSiZhengCe();//隐私政策
    private MyHandler myHandler = new MyHandler(this);
    private LinearLayout originLoginLayout;
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
                    MessageToast.getInstance(activity).show(activity.loginTokenResult.getMsg());
                    if (activity.loginTokenResult.getCode() == 200) {
                        SPUtils.getInstance().setUserBindType(BaseApplication.applicationContext, SPUtils.PHONE);
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
        setContentView(R.layout.activity_login_by_pwd);
        SoftKeyBoardListener softKeyBoardListener = new SoftKeyBoardListener(this);
        softKeyBoardListener.setOnSoftKeyBoardChangeListener(this);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        mTencent = Tencent.createInstance("101762258", getApplicationContext());
        WXEntryActivity.setOnWxLoginSuccessListener(this);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPwdLogin=false;
    }

    @Override
    public void keyBoardShow(int height) {
        originLoginLayout.setVisibility(View.GONE);
    }

    @Override
    public void keyBoardHide(int height) {
        originLoginLayout.setVisibility(View.VISIBLE);
    }

    private void initViews() {
        closeImageView = findViewById(R.id.closeImageView);
        closeImageView.setOnClickListener(this);
        seePwdImageView = findViewById(R.id.seePwdImageView);
        phoneNumberEdit = findViewById(R.id.phoneNumberEdit);
        originLoginLayout=findViewById(R.id.originLoginLayout);
        phoneNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                phoneNumber = phoneNumberEdit.getText().toString().trim();
//                if (!TextUtils.isEmpty(phoneNumber)) {
//                    clearPhoneNumberLayout.setVisibility(View.VISIBLE);
//                } else {
//                    clearPhoneNumberLayout.setVisibility(View.INVISIBLE);
//                }
                phoneNumber = phoneNumberEdit.getText().toString();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    clearPhoneNumberLayout.setVisibility(View.VISIBLE);
                    if (PhoneNumberUtils.getInstance().isMobileNO(phoneNumber)) {
                        if (!isCheck) {
                            MessageToast.getInstance(LoginByPwdActivity.this).show("同意《圈助用户协议》、《隐私政策》才可登录");
                        } else {
                            loginTextView.setBackground(getResources().getDrawable(R.mipmap.create_shangquan_btn_bg));
                            loginTextView.setClickable(true);
                        }
                    } else {
                        loginTextView.setBackground(getResources().getDrawable(R.drawable.shape_oval_solid_bg_bbbbbbb));
                        loginTextView.setClickable(false);
                    }
                } else {
                    clearPhoneNumberLayout.setVisibility(View.INVISIBLE);
                    loginTextView.setBackground(getResources().getDrawable(R.drawable.shape_oval_solid_bg_bbbbbbb));
                    loginTextView.setClickable(false);
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
        wxLoginImageView = findViewById(R.id.wxLoginImageView);
        wxLoginImageView.setOnClickListener(this);
        qqLoginImageView = findViewById(R.id.qqLoginImageView);
        qqLoginImageView.setOnClickListener(this);
        yoonghuxieyiTextView = findViewById(R.id.yoonghuxieyiTextView);
        yoonghuxieyiTextView.setOnClickListener(this);
        yinsizhengceTextView = findViewById(R.id.yinsizhengceTextView);
        yinsizhengceTextView.setOnClickListener(this);
        checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                isCheck = isChecked;
                if (!isCheck) {
                    loginTextView.setBackground(getResources().getDrawable(R.drawable.shape_oval_solid_bg_bbbbbbb));
                    loginTextView.setClickable(false);
                } else {
                    loginTextView.setBackground(getResources().getDrawable(R.mipmap.create_shangquan_btn_bg));
                    loginTextView.setClickable(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeImageView:
                Intent homeIntent = new Intent(this, HomeActivity.class);
                startActivity(homeIntent);
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
                    if (!isCheck) {
                        MessageToast.getInstance(this).show("同意《圈助用户协议》、《隐私政策》才可登录");
                    } else {
                        if (!StringUtils.isNullOrEmpty(pwd)) {
                            loginByPwd();
                        } else {
                            MessageToast.getInstance(this).show("请输入密码");
                        }
                    }
                } else {
                    MessageToast.getInstance(this).show("请输入正确的手机号码");
                }
                break;
            case R.id.verifyCodeLoginTextView:
                Intent verifyLoginIntent = new Intent(LoginByPwdActivity.this, LoginGetVertifyCodeActivity.class);
                startActivity(verifyLoginIntent);
                break;
            case R.id.forgetPwdTextView:
                Intent intent = new Intent(LoginByPwdActivity.this, ForgetLoginPwd1Activity.class);
                startActivity(intent);
                break;
            case R.id.wxLoginImageView:
                if (!isCheck) {
                    MessageToast.getInstance(this).show("同意《圈助用户协议》、《隐私政策》才可登录");
                } else {
                    WXUtils.WxLogin(LoginByPwdActivity.this);
                }
                break;
            case R.id.qqLoginImageView:
                if (!isCheck) {
                    MessageToast.getInstance(this).show("同意《圈助用户协议》、《隐私政策》才可登录");
                } else {
                    qqLogin();
                }
                break;
            case R.id.yoonghuxieyiTextView:
                Intent xieyiIntent = new Intent(this, H5PageActivity.class);
                xieyiIntent.putExtra("url", xieyi_url);
                startActivity(xieyiIntent);
                break;
            case R.id.yinsizhengceTextView:
                Intent yinsiIntent = new Intent(this, H5PageActivity.class);
                yinsiIntent.putExtra("url", yinsi_url);
                startActivity(yinsiIntent);
                break;
        }
    }


    public void qqLogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", qqListener);
        }
    }

    //授权登录监听（最下面是返回结果）
    private IUiListener qqListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            System.out.println(" ---> qqListener: " + GsonUtils.GsonString(o));
            final String uniqueCode = ((JSONObject) o).optString("openid"); //QQ的openid
            String token = null;
            String expires_in = null;
//            System.out.println("uniqueCode： " + uniqueCode);
            try {
                token = ((JSONObject) o).getString("access_token");
                expires_in = ((JSONObject) o).getString("expires_in");
//                System.out.println("token: " + token + " , expires_in:" + expires_in);
                //在这里直接可以处理登录
            } catch (JSONException e) {
//                System.out.println("exception: " + e.toString());
                e.printStackTrace();
            }
            QQToken qqtoken = mTencent.getQQToken();
            mTencent.setOpenId(uniqueCode);
            mTencent.setAccessToken(token, expires_in);
            UserInfo info = new UserInfo(getApplicationContext(), qqtoken);
            info.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    String nickname = ((JSONObject) o).optString("nickname");
                    String sexStr = ((JSONObject) o).optString("sex");
                    String headImg = ((JSONObject) o).optString("figureurl_qq_2");
                    int sex = 0;
                    switch (sexStr) {
                        case "男":
                            sex = 1;
                            break;
                    }
                    //QQ第三方登录（5个参数）
                    qqLogin(uniqueCode, headImg, nickname);
//                    System.out.println("nickname:" + nickname + " , headImg: " + headImg + " , sexStr: " + sexStr);
                }

                @Override
                public void onError(UiError uiError) {
                }

                @Override
                public void onCancel() {
                }
            });
        }

        @Override
        public void onError(UiError uiError) {
            System.out.println("onError: " + uiError.toString());
        }

        @Override
        public void onCancel() {
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResult(requestCode, resultCode, data);
        mTencent.onActivityResultData(requestCode, resultCode, data, qqListener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE ||
                    resultCode == Constants.REQUEST_QZONE_SHARE ||
                    resultCode == Constants.REQUEST_OLD_SHARE) {
                mTencent.handleResultData(data, qqListener);
            }
        }
    }

    private LoginTokenResult loginTokenResult;

    private void qqLogin(String openid, String avatar, String nickname) {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.QQ_LOGIN);
        params.addBodyParameter("openid", openid);
        params.addBodyParameter("avatar", avatar);
        params.addBodyParameter("nickname", nickname);
        params.addBodyParameter("unionid", openid);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("QQ登录接口回调: " + result);
                loginTokenResult = GsonUtils.GsonToBean(result, LoginTokenResult.class);
                if (200 == loginTokenResult.getCode()) {
                    SPUtils.getInstance().userLogin(BaseApplication.applicationContext);
                    SPUtils.getInstance().saveUserId(BaseApplication.applicationContext, loginTokenResult.getData().getUser_id());
                    SPUtils.getInstance().saveUserAvatar(BaseApplication.applicationContext, loginTokenResult.getData().getUserinfo().getAvatar());
                    SPUtils.getInstance().saveUserName(BaseApplication.applicationContext, loginTokenResult.getData().getUserinfo().getUsername());
                    SPUtils.getInstance().saveUserInfoStatus(BaseApplication.applicationContext, loginTokenResult.getData().getUserinfo().isHas_pwd(),
                            loginTokenResult.getData().getUserinfo().isBind_mobile(),
                            loginTokenResult.getData().getUserinfo().isFill_profile(),
                            loginTokenResult.getData().getUserinfo().isChoose_hobby());
                }

                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
                System.out.println("QQ登录接口回调: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onWxLoginSuccess() {
        pageChange();
        finish();
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

    private void loginByPwd() {
        SPUtils.getInstance().saveUserPhoneNum(BaseApplication.applicationContext, phoneNumber);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.LOGIN_BY_PWD);
        params.addBodyParameter("mobile", phoneNumber);
        params.addBodyParameter("password", pwd);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println(result);
                loginTokenResult = GsonUtils.GsonToBean(result, LoginTokenResult.class);
                if (200 == loginTokenResult.getCode()) {
                    isPwdLogin=true;
                    SPUtils.getInstance().userLogin(BaseApplication.applicationContext);
                    SPUtils.getInstance().saveUserId(BaseApplication.applicationContext, loginTokenResult.getData().getUser_id());
                    SPUtils.getInstance().saveUserToken(BaseApplication.applicationContext, loginTokenResult.getToken());
                    SPUtils.getInstance().saveIMToken(BaseApplication.applicationContext, loginTokenResult.getData().getToken());
                    SPUtils.getInstance().saveUserAvatar(BaseApplication.applicationContext, loginTokenResult.getData().getUserinfo().getAvatar());
                    SPUtils.getInstance().saveUserName(BaseApplication.applicationContext, loginTokenResult.getData().getUserinfo().getUsername());
                    SPUtils.getInstance().saveUserInfoStatus(BaseApplication.applicationContext, loginTokenResult.getData().getUserinfo().isHas_pwd(),
                            loginTokenResult.getData().getUserinfo().isBind_mobile(),
                            loginTokenResult.getData().getUserinfo().isFill_profile(),
                            loginTokenResult.getData().getUserinfo().isChoose_hobby());
                }
                System.out.println(loginTokenResult.getMsg());
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
                System.out.println(ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private boolean isPwdLogin=false;
    private void pageChange() {
        if (SPUtils.getInstance().getUserBindPhone(BaseApplication.applicationContext) &&
                SPUtils.getInstance().getUserFillProfile(BaseApplication.applicationContext) &&
                SPUtils.getInstance().getUserChooseInterest(BaseApplication.applicationContext)) {
            Intent homeIntent = new Intent(this, HomeActivity.class);
            startActivity(homeIntent);
        } else {
            if (!SPUtils.getInstance().getUserBindPhone(BaseApplication.applicationContext)&&!isPwdLogin) {
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
