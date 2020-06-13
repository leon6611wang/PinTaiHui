package com.zhiyu.quanzhu.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.PurseResult;
import com.zhiyu.quanzhu.ui.dialog.BondWithdrawAccountDialog;
import com.zhiyu.quanzhu.ui.dialog.NotificationDialog;
import com.zhiyu.quanzhu.ui.dialog.PasswordCheckDialog;
import com.zhiyu.quanzhu.ui.dialog.WithdrawSelectDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

/**
 * 提现
 */
public class WithdrawActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private WithdrawSelectDialog withdrawSelectDialog;
    private NotificationDialog notificationDialog;
    private PasswordCheckDialog passwordCheckDialog;
    private YNDialog ynDialog;
    private BondWithdrawAccountDialog bondWithdrawAccountDialog;
    private TextView withdrawTextView, withdrawSelectTextView;
    private EditText moneyEditText;
    private long wechat_money, ali_money;
    private boolean is_pwd;//是否设置支付密码
    private boolean is_ali;
    private boolean is_wechar;

    private TextView wechatMoneyTextView, aliMoneyTextView, withdrawAllMoneyTextView;
    private int withdrawType = 0;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<WithdrawActivity> activityWeakReference;

        public MyHandler(WithdrawActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            WithdrawActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 0:
                    if (activity.purseResult.getCode() == 200) {
                        activity.wechat_money = activity.purseResult.getData().getWechat_money();
                        activity.ali_money = activity.purseResult.getData().getAli_money();
                        activity.is_pwd = activity.purseResult.getData().isIs_pwd();
                        activity.is_wechar = activity.purseResult.getData().isIs_wechar();
                        activity.is_ali = activity.purseResult.getData().isIs_ali();
                        activity.wechatMoneyTextView.setText(PriceParseUtils.getInstance().parsePrice(activity.wechat_money));
                        activity.aliMoneyTextView.setText(PriceParseUtils.getInstance().parsePrice(activity.ali_money));
                    }
                    break;
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    break;
                case 2:
                    FailureToast.getInstance(activity).show();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initDialogs();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        purseInformation();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("提现");
        wechatMoneyTextView = findViewById(R.id.wechatMoneyTextView);
        aliMoneyTextView = findViewById(R.id.aliMoneyTextView);
        withdrawTextView = findViewById(R.id.withdrawTextView);
        withdrawTextView.setOnClickListener(this);
        withdrawSelectTextView = findViewById(R.id.withdrawSelectTextView);
        withdrawSelectTextView.setOnClickListener(this);
        moneyEditText = findViewById(R.id.moneyEditText);
        withdrawAllMoneyTextView = findViewById(R.id.withdrawAllMoneyTextView);
        withdrawAllMoneyTextView.setOnClickListener(this);
    }

    private void initDialogs() {
        withdrawSelectDialog = new WithdrawSelectDialog(this, R.style.dialog, new WithdrawSelectDialog.OnWithdrawSelectListener() {
            @Override
            public void onWithdrawSelect(int index, String desc) {
                withdrawSelectTextView.setText(desc);
                withdrawType = index;
            }
        });
        notificationDialog = new NotificationDialog(this, R.style.dialog);
        ynDialog = new YNDialog(this, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                Intent payPwdIntent = new Intent(WithdrawActivity.this, EditPwdFirstActivity.class);
                payPwdIntent.putExtra("payOrLoginPwd", 1);
                startActivityForResult(payPwdIntent, 997);
            }
        });
        passwordCheckDialog = new PasswordCheckDialog(this, R.style.inputDialog, new PasswordCheckDialog.OnPayPwdListener() {
            @Override
            public void onPayPwd(String password) {
                withdraw(password);
            }
        });
        passwordCheckDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideSoftKeyBoard(getWindow());
            }
        });
        bondWithdrawAccountDialog = new BondWithdrawAccountDialog(this, R.style.dialog, new BondWithdrawAccountDialog.OnBondWithdrawAccountListener() {
            @Override
            public void onConfirm() {
                Intent bondIntent = new Intent(WithdrawActivity.this, WithdrawSettingActivity.class);
                bondIntent.putExtra("is_ali", is_ali);
                bondIntent.putExtra("is_wechar", is_wechar);
                startActivityForResult(bondIntent, 1031);
            }
        });
    }

    public void hideSoftKeyBoard(final Window window) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (window.getCurrentFocus() != null) {
                    InputMethodManager inputManager = (InputMethodManager) window.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(window.getCurrentFocus().getWindowToken(), 0);
                }
            }
        }, 200);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 997) {
            if (data.hasExtra("isPwd")) {
                boolean isPwd = data.getBooleanExtra("isPwd", false);
                is_pwd = isPwd;
            }
        }
        if (requestCode == 1031) {
            if (data.hasExtra("isAlipay"))
                is_ali = data.getBooleanExtra("isAlipay", false);
            if (data.hasExtra("isWechat"))
                is_wechar = data.getBooleanExtra("isWechat", false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.withdrawTextView:
//                notificationDialog.show();
////                notificationDialog.setContent("您的提现申请已提交，请等待到账~");
                if (withdrawType == 0) {
                    MessageToast.getInstance(this).show("请选择提现方式");
                    break;
                }
                if (StringUtils.isNullOrEmpty(moneyEditText.getText().toString().trim())) {
                    MessageToast.getInstance(this).show("请输入提现金额");
                    break;
                }
                switch (withdrawType) {
                    case 2:
                        if (!is_wechar) {
                            Intent bondIntent = new Intent(WithdrawActivity.this, WithdrawSettingActivity.class);
                            bondIntent.putExtra("is_ali", is_ali);
                            bondIntent.putExtra("is_wechar", is_wechar);
                            startActivityForResult(bondIntent, 1031);
                        } else {
                            if (is_pwd) {
                                passwordCheckDialog.show();
                            } else {
                                ynDialog.show();
                                ynDialog.setTitle("未设置支付密码，是否立即设置？");
                            }
                        }
                        break;
                    case 1:
                        if (!is_ali) {
                            Intent bondIntent = new Intent(WithdrawActivity.this, WithdrawSettingActivity.class);
                            bondIntent.putExtra("is_ali", is_ali);
                            bondIntent.putExtra("is_wechar", is_wechar);
                            startActivityForResult(bondIntent, 1031);
                        } else {
                            if (is_pwd) {
                                passwordCheckDialog.show();
                            } else {
                                ynDialog.show();
                                ynDialog.setTitle("未设置支付密码，是否立即设置？");
                            }
                        }
                        break;
                }
                break;
            case R.id.withdrawSelectTextView:
                withdrawSelectDialog.show();
                break;
            case R.id.withdrawAllMoneyTextView:
                if (withdrawType == 0) {
                    MessageToast.getInstance(this).show("请选择提现方式");
                } else {
                    switch (withdrawType) {
                        case 1:
                            moneyEditText.setText(PriceParseUtils.getInstance().parsePrice(ali_money));
                            break;
                        case 2:
                            moneyEditText.setText(PriceParseUtils.getInstance().parsePrice(wechat_money));
                            break;
                    }
                }
                break;
        }
    }


    private BaseResult baseResult;

    /**
     * 提现
     */
    private void withdraw(String password) {
        int money = Integer.parseInt(moneyEditText.getText().toString().trim());
        money = money * 100;
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MY_PURSE_WITHDRAW);
        params.addBodyParameter("pwd", password);
        params.addBodyParameter("type", String.valueOf(withdrawType));//1支付宝 2微信
        params.addBodyParameter("money", String.valueOf(money));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("提现: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
//                System.out.println("withdraw: "+ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private PurseResult purseResult;

    //钱包详情
    private void purseInformation() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MY_PURSE_INFORMATION);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                purseResult = GsonUtils.GsonToBean(result, PurseResult.class);
                Message message = myHandler.obtainMessage(0);
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
