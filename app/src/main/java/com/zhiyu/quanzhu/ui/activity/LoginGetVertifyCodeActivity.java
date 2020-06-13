package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.chic.utils.H5Utils;
import com.leon.chic.utils.SPUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.LoginTokenResult;
import com.zhiyu.quanzhu.model.result.PaymentResult;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PhoneNumberUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;
import com.zhiyu.quanzhu.utils.WXUtils;
import com.zhiyu.quanzhu.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import io.vov.vitamio.utils.Log;

public class LoginGetVertifyCodeActivity extends BaseActivity implements View.OnClickListener, WXEntryActivity.OnWxLoginSuccessListener {
    private TextView getVertifyCodeTextView, loginByPwdTextView, yoonghuxieyiTextView, yinsizhengceTextView;
    private EditText phoneNumberEdit;
    private ImageView closeImageView, wxLoginImageView, qqLoginImageView;
    private RelativeLayout clearPhoneNumberLayout;
    private String phoneNumber;
    private Timer timer;
    private TimerTask task;
    private final int COUNT = 60;
    private int timeCount = COUNT;
    private Tencent mTencent;
    private MyHandler myHandler = new MyHandler(this);
    private ShareDialog shareDialog;
    private String xieyi_url = H5Utils.getInstance().yongHuXieYi(),//用户协议
            yinsi_url = H5Utils.getInstance().yinSiZhengCe();//隐私政策

    private static class MyHandler extends Handler {
        WeakReference<LoginGetVertifyCodeActivity> loginGetVertifyCodeActivityWeakReference;

        public MyHandler(LoginGetVertifyCodeActivity activity) {
            loginGetVertifyCodeActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginGetVertifyCodeActivity activity = loginGetVertifyCodeActivityWeakReference.get();
            switch (msg.what) {
                case 0:
                    if (activity.timeCount > 0) {
                        activity.getVertifyCodeTextView.setClickable(false);
                        activity.getVertifyCodeTextView.setBackground(activity.getResources().getDrawable(R.drawable.shape_oval_solid_bg_gray));
                        activity.getVertifyCodeTextView.setText(activity.timeCount + " s");
                    } else {
                        activity.getVertifyCodeTextView.setClickable(true);
                        activity.getVertifyCodeTextView.setBackground(activity.getResources().getDrawable(R.mipmap.verify_code_bg));
                        activity.getVertifyCodeTextView.setText("获取短信验证码");
                    }

                    break;
                case 1:
                    if (activity.baseResult.getCode() == 200) {
                        activity.goToInputCode();
                        activity.finish();
                    }
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.loginTokenResult.getMsg());
                    if (activity.loginTokenResult.getCode() == 200) {
                        SPUtils.getInstance().setUserBindType(BaseApplication.applicationContext, SPUtils.QQ);
                        activity.pageChange();
                        activity.finish();
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
        setContentView(R.layout.activity_login_get_vertifycode);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        mTencent = Tencent.createInstance("101762258", getApplicationContext());
        WXEntryActivity.setOnWxLoginSuccessListener(this);
        initViews();
        initTimerTask();
        shareDialog = new ShareDialog(this, this, R.style.dialog, new ShareDialog.OnShareListener() {
            @Override
            public void onShare(int position, String desc) {

            }
        });
    }

    private void initViews() {
        getVertifyCodeTextView = findViewById(R.id.getVertifyCodeTextView);
        getVertifyCodeTextView.setOnClickListener(this);
        phoneNumberEdit = findViewById(R.id.phoneNumberEdit);
        phoneNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneNumber = phoneNumberEdit.getText().toString();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    clearPhoneNumberLayout.setVisibility(View.VISIBLE);
                    if (PhoneNumberUtils.getInstance().isMobileNO(phoneNumber)) {
                        getVertifyCodeTextView.setBackground(getResources().getDrawable(R.mipmap.create_shangquan_btn_bg));
                        getVertifyCodeTextView.setClickable(true);
                    } else {
                        getVertifyCodeTextView.setBackground(getResources().getDrawable(R.drawable.shape_oval_solid_bg_bbbbbbb));
                        getVertifyCodeTextView.setClickable(false);
                    }
                } else {
                    clearPhoneNumberLayout.setVisibility(View.INVISIBLE);
                    getVertifyCodeTextView.setBackground(getResources().getDrawable(R.drawable.shape_oval_solid_bg_bbbbbbb));
                    getVertifyCodeTextView.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        closeImageView = findViewById(R.id.closeImageView);
        closeImageView.setOnClickListener(this);
        clearPhoneNumberLayout = findViewById(R.id.clearPhoneNumberLayout);
        clearPhoneNumberLayout.setOnClickListener(this);
        loginByPwdTextView = findViewById(R.id.loginByPwdTextView);
        loginByPwdTextView.setOnClickListener(this);
        wxLoginImageView = findViewById(R.id.wxLoginImageView);
        wxLoginImageView.setOnClickListener(this);
        qqLoginImageView = findViewById(R.id.qqLoginImageView);
        qqLoginImageView.setOnClickListener(this);
        yoonghuxieyiTextView = findViewById(R.id.yoonghuxieyiTextView);
        yoonghuxieyiTextView.setOnClickListener(this);
        yinsizhengceTextView = findViewById(R.id.yinsizhengceTextView);
        yinsizhengceTextView.setOnClickListener(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getVertifyCodeTextView:
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
            case R.id.closeImageView:
                Intent homeIntent = new Intent(this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
                break;
            case R.id.clearPhoneNumberLayout:
                phoneNumberEdit.setText(null);
                break;
            case R.id.loginByPwdTextView:
                Intent intent = new Intent(LoginGetVertifyCodeActivity.this, LoginByPwdActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.wxLoginImageView:
                WXUtils.WxLogin(LoginGetVertifyCodeActivity.this);
                break;
            case R.id.qqLoginImageView:
                qqLogin();
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
            mTencent.login(this, "all", loginListener);
        }
    }

    //授权登录监听（最下面是返回结果）
    private IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
//            System.out.println(GsonUtils.GsonString(o));
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

    private BaseResult baseResult;

    private void getVertifiyCode() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GET_VERTIFY_CODE);
        params.addBodyParameter("mobile", phoneNumber);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("验证码: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("验证码: " + ex.toString());
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

    private void goToInputCode() {
        SPUtils.getInstance().saveUserPhoneNum(BaseApplication.applicationContext, phoneNumber);
        Intent intent = new Intent(this, LoginInputVertifyCodeActivity.class);
        intent.putExtra("phonenumber", phoneNumber);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResult(requestCode, resultCode, data);
        mTencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE ||
                    resultCode == Constants.REQUEST_QZONE_SHARE ||
                    resultCode == Constants.REQUEST_OLD_SHARE) {
                mTencent.handleResultData(data, loginListener);
            }
        }
    }

    private String payment = null;
    private PaymentResult paymentResult;

    /**
     * 服务器获取支付payment
     */
    private void requestPayment() {
        final RequestParams params = MyRequestParams.getInstance(this).getRequestParams("http://app.pintaihui.cn/api/v1/pay/order/add");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("onsuccess : " + result);
                Log.i("payment", "onSuccess " + result);
                paymentResult = GsonUtils.GsonToBean(result, PaymentResult.class);
                payment = GsonUtils.GsonString(paymentResult.getData().getPayment());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("payment", "onError " + ex.toString());
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
     * 第三方支付
     */
//    private void pay() {
//        AdaPay.doPay(this, payment, new PayCallback() {
//            @Override
//            public void onPayment(PayResult payResult) {
//                //处理支付结果
//                Log.i("payment", payResult.getResultCode() + " , " + payResult.getResultMsg() + " , " + payResult.getOrderNo());
//            }
//        });
//    }


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
//        Toast.makeText(this, "登录成功.", Toast.LENGTH_SHORT).show();
        SPUtils.getInstance().setUserBindType(BaseApplication.applicationContext, SPUtils.WX);
        pageChange();
        finish();
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
    }
}
