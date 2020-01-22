package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
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

import com.chinapnr.android.adapay.AdaPay;
import com.chinapnr.android.adapay.PayCallback;
import com.chinapnr.android.adapay.bean.PayResult;
import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.LoginTokenResult;
import com.zhiyu.quanzhu.model.result.PaymentResult;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.listener.BaseUIListener;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PhoneNumberUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;
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

import static com.zhiyu.quanzhu.utils.WXUtils.WECHAT_FRIEND;

public class LoginGetVertifyCodeActivity extends BaseActivity implements View.OnClickListener, WXEntryActivity.OnWxLoginSuccessListener {

    private TextView getVertifyCodeTextView, loginByPwdTextView;
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
                    Toast.makeText(activity, activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
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
                } else {
                    clearPhoneNumberLayout.setVisibility(View.INVISIBLE);
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
                    Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.closeImageView:
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
//                requestPayment();
//                WXUtils.WxTextShare(this,"文本分享测试",WECHAT_FRIEND);
//                WXUtils.WxBitmapShare(this, BitmapFactory.decodeResource(getResources(),R.mipmap.timg),WECHAT_FRIEND);
//                ThreadPoolUtils.getInstance().init().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        WXUtils.WxUrlShare(LoginGetVertifyCodeActivity.this,"https://xw.qq.com/partner/gdtadf/20191220A0KBN4/20191220A0KBN400?ADTAG=gdtadf&pgv_ref=gdtadf","《庆余年》全集遭泄露，公安机关已立案，网友：这下不用超前点映了","热播剧《庆余年》46集全集在网上突然被泄露。有越来越多的网友，发现有人在社交网站上传播热播剧《庆余年》全集，甚至有人晒出了相关截图，而当前爱奇艺、腾讯视频两大平台才更新至33集。","https://inews.gtimg.com/newsapp_bt/0/11019914276/641",WECHAT_FRIEND);
//                    }
//                });

                break;
            case R.id.qqLoginImageView:
//                pay();
                qqLogin();
//                shareDialog.show();
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
            System.out.println(GsonUtils.GsonString(o));
            final String uniqueCode = ((JSONObject) o).optString("openid"); //QQ的openid
            String token = null;
            String expires_in = null;
            System.out.println("uniqueCode： " + uniqueCode);
            try {
                token = ((JSONObject) o).getString("access_token");
                expires_in = ((JSONObject) o).getString("expires_in");
                System.out.println("token: " + token + " , expires_in:" + expires_in);
                //在这里直接可以处理登录
            } catch (JSONException e) {
                System.out.println("exception: " + e.toString());
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
                    System.out.println("nickname:" + nickname + " , headImg: " + headImg + " , sexStr: " + sexStr);
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

    private void goToInputCode() {
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
    private void pay() {
        AdaPay.doPay(this, payment, new PayCallback() {
            @Override
            public void onPayment(PayResult payResult) {
                //处理支付结果
                Log.i("payment", payResult.getResultCode() + " , " + payResult.getResultMsg() + " , " + payResult.getOrderNo());
            }
        });
    }


    private LoginTokenResult loginTokenResult;
    private void qqLogin(String openid, String avatar, String nickname) {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.QQ_LOGIN);
        params.addBodyParameter("openid", openid);
        params.addBodyParameter("avatar", avatar);
        params.addBodyParameter("nickname", nickname);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                loginTokenResult = GsonUtils.GsonToBean(result, LoginTokenResult.class);
                Message message=myHandler.obtainMessage(2);
                message.sendToTarget();
                System.out.println("qqLogin: " + result);
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

    @Override
    public void onWxLoginSuccess() {
        Toast.makeText(this, "登录成功.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
