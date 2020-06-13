package com.zhiyu.quanzhu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.BindInfoResult;
import com.zhiyu.quanzhu.model.result.BindMobileResult;
import com.zhiyu.quanzhu.ui.dialog.PasswordCheckDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
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

public class AccountSafeActivity extends BaseActivity implements View.OnClickListener, WXEntryActivity.OnBondWechatAcountListener {
    private LinearLayout backLayout;
    private TextView titleTextView, phoneNumberTextView, bondQQTextView, bondWechatTextView;
    private LinearLayout updatePhoneNumberLayout, editLoginPwdLayout, editPayPwdLayout;
    private PasswordCheckDialog passwordCheckDialog;
    private YNDialog ynDialog, confirmBindDialog;
    private int bondType = 1;//绑定类型1:qq,2:wx
    private Tencent mTencent;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<AccountSafeActivity> accountSafeActivityWeakReference;

        public MyHandler(AccountSafeActivity activity) {
            accountSafeActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AccountSafeActivity activity = accountSafeActivityWeakReference.get();
            switch (msg.what) {
                case 1:
                    if (200 == activity.infoResult.getCode()) {
                        if (!StringUtils.isNullOrEmpty(activity.infoResult.getData().getPhone()))
                            activity.phoneNumberTextView.setText(activity.parsePhoneNumber(activity.infoResult.getData().getPhone()));
                        if (activity.infoResult.getData().isHas_qq()) {
                            activity.bondQQTextView.setText("解绑");
                            activity.bondQQTextView.setBackground(activity.getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
                            activity.bondQQTextView.setTextColor(activity.getResources().getColor(R.color.text_color_yellow));
                        } else {
                            activity.bondQQTextView.setText("绑定");
                            activity.bondQQTextView.setBackground(activity.getResources().getDrawable(R.mipmap.bond_button_bg));
                            activity.bondQQTextView.setTextColor(activity.getResources().getColor(R.color.white));
                        }
                        if (activity.infoResult.getData().isHas_wx()) {
                            activity.bondWechatTextView.setText("解绑");
                            activity.bondWechatTextView.setBackground(activity.getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
                            activity.bondWechatTextView.setTextColor(activity.getResources().getColor(R.color.text_color_yellow));
                        } else {
                            activity.bondWechatTextView.setText("绑定");
                            activity.bondWechatTextView.setBackground(activity.getResources().getDrawable(R.mipmap.bond_button_bg));
                            activity.bondWechatTextView.setTextColor(activity.getResources().getColor(R.color.white));
                        }
                    }
                    break;
                case 2:
                    switch (activity.bindMobileResult.getCode()) {
                        case 200:
                            MessageToast.getInstance(activity).show(activity.bindMobileResult.getMsg());
                            activity.bindInfo();
                            break;
                        case 1003:
                            activity.confirmBindDialog.show();
                            activity.confirmBindDialog.setTitle(activity.bindMobileResult.getMsg());
                            break;
                        default:
                            MessageToast.getInstance(activity).show(activity.bindMobileResult.getMsg());
                            break;
                    }
                    break;
                case 3:
                    FailureToast.getInstance(activity).show();
                    break;
                case 4:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safe);
        mTencent = Tencent.createInstance("101762258", getApplicationContext());
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        WXEntryActivity.setOnBondWechatAcountListener(this);
        initDialogs();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindInfo();
    }

    private String parsePhoneNumber(String phoneNumber) {
        String phonenumber = null;
        String phoneNumber1 = phoneNumber.substring(0, 2);
        String phoneNumber2 = phoneNumber.substring(7, 11);
        phonenumber = phoneNumber1 + "****" + phoneNumber2;
        return phonenumber;
    }

    private void initDialogs() {
        passwordCheckDialog = new PasswordCheckDialog(this, R.style.inputDialog, new PasswordCheckDialog.OnPayPwdListener() {
            @Override
            public void onPayPwd(String password) {


            }
        });
        passwordCheckDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                SoftKeyboardUtil.hideSoftKeyBoard(getWindow());
            }
        });
        ynDialog = new YNDialog(this, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                unbond();
            }
        });
        confirmBindDialog = new YNDialog(this, R.style.dialog, new YNDialog.OnYNListener() {
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
        titleTextView.setText("账号与安全");
        updatePhoneNumberLayout = findViewById(R.id.updatePhoneNumberLayout);
        updatePhoneNumberLayout.setOnClickListener(this);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        editLoginPwdLayout = findViewById(R.id.editLoginPwdLayout);
        editLoginPwdLayout.setOnClickListener(this);
        editPayPwdLayout = findViewById(R.id.editPayPwdLayout);
        editPayPwdLayout.setOnClickListener(this);
        bondQQTextView = findViewById(R.id.bondQQTextView);
        bondQQTextView.setOnClickListener(this);
        bondWechatTextView = findViewById(R.id.bondWechatTextView);
        bondWechatTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updatePhoneNumberLayout:
                Intent editPhonenumIntent = new Intent(this, UpdatePhoneNumberFirstActivity.class);
                startActivity(editPhonenumIntent);
                break;
            case R.id.editLoginPwdLayout:
                Intent editLoginPwdIntent = new Intent(this, EditPwdFirstActivity.class);
                editLoginPwdIntent.putExtra("payOrLoginPwd", 0);
                startActivity(editLoginPwdIntent);
                break;
            case R.id.editPayPwdLayout:
                Intent editPayPwdIntent = new Intent(this, EditPwdFirstActivity.class);
                editPayPwdIntent.putExtra("payOrLoginPwd", 1);
                startActivity(editPayPwdIntent);
                break;
            case R.id.bondQQTextView:
                if (infoResult.getData().isHas_qq()) {
                    bondType = 1;
                    ynDialog.show();
                    ynDialog.setTitle("确定解绑QQ ？");
                } else {
                    qqLogin();
                }

                break;
            case R.id.bondWechatTextView:
                if (infoResult.getData().isHas_wx()) {
                    bondType = 2;
                    ynDialog.show();
                    ynDialog.setTitle("确定解绑微信 ？");
                } else {
                    WXUtils.WxLogin(AccountSafeActivity.this);
                }

                break;
            case R.id.backLayout:
                finish();
                break;
        }
    }

    @Override
    public void onBondWechatAccount(String openid, String unionid, String nickname, String avatar) {
        this.openid = openid;
        this.unionid = unionid;
        this.nickname = nickname;
        this.avatar = avatar;
        bondWechat();
    }

    private BindInfoResult infoResult;

    private void bindInfo() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.BIND_INFO);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                infoResult = GsonUtils.GsonToBean(result, BindInfoResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(3);
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

    private BaseResult baseResult;

    private void unbond() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.UNBIND);
        params.addBodyParameter("type", String.valueOf(bondType));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(4);
                message.sendToTarget();
                if (200 == baseResult.getCode()) {
                    bindInfo();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(3);
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
    private int bind_type = 0;
    private String openid, unionid, nickname, avatar;

    private void bondQQ() {
        bind_type = 1;
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.BOND_QQ);
        params.addBodyParameter("openid", openid);
        params.addBodyParameter("unionid", unionid);
        params.addBodyParameter("nickname", nickname);
        params.addBodyParameter("avatar", avatar);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                bindMobileResult = GsonUtils.GsonToBean(result, BindMobileResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(3);
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

    private void bondWechat() {
        bind_type = 2;
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.BOND_WECHAT);
        params.addBodyParameter("openid", openid);
        params.addBodyParameter("unionid", unionid);
        params.addBodyParameter("nickname", nickname);
        params.addBodyParameter("avatar", avatar);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("bindwechat: " + result);
                bindMobileResult = GsonUtils.GsonToBean(result, BindMobileResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(3);
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
        params.addBodyParameter("openid", openid);
        params.addBodyParameter("unionid", unionid);
        params.addBodyParameter("nickname", nickname);
        params.addBodyParameter("avatar", avatar);
        params.addBodyParameter("type", String.valueOf(bind_type));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("确定绑定: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(4);
                message.sendToTarget();
                if (baseResult.getCode() == 200) {
                    bindInfo();
                }
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

    public void qqLogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", loginListener);
        }
    }

    //授权登录监听（最下面是返回结果）
    private IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            System.out.println("QQ绑定回调: " + o.toString());
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
                    System.out.println("qq 信息: " + o.toString());
                    String name = ((JSONObject) o).optString("nickname");
                    String sexStr = ((JSONObject) o).optString("sex");
                    String headImg = ((JSONObject) o).optString("figureurl_qq_2");
                    int sex = 0;
                    switch (sexStr) {
                        case "男":
                            sex = 1;
                            break;
                    }
                    //QQ第三方登录（5个参数）
                    System.out.println("nickname:" + nickname + " , headImg: " + headImg + " , sexStr: " + sexStr);
                    openid = uniqueCode;
                    unionid = uniqueCode;
                    nickname = name;
                    avatar = headImg;
                    bondQQ();
                }

                @Override
                public void onError(UiError uiError) {
                    System.out.println("qq绑定错误: " + uiError.toString());
                }

                @Override
                public void onCancel() {
                    System.out.println("qq绑定取消");
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
        mTencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE ||
                    resultCode == Constants.REQUEST_QZONE_SHARE ||
                    resultCode == Constants.REQUEST_OLD_SHARE) {
                mTencent.handleResultData(data, loginListener);
            }
        }
    }
}
