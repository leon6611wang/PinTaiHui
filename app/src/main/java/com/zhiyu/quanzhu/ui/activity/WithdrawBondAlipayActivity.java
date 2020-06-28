package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.BondAlipayAccountResult;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

public class WithdrawBondAlipayActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView, saveTextView;
    private EditText realNameEditText, alipayAccountEditText;
    private String payPassword;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<WithdrawBondAlipayActivity> activityWeakReference;

        public MyHandler(WithdrawBondAlipayActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            WithdrawBondAlipayActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        Intent intent = new Intent();
                        activity.setResult(1030, intent);
                        activity.finish();
                    }
                    break;
                case 2:
                    if (activity.accountResult.getCode() == 200) {
                        if(null!=activity.accountResult&&null!=activity.accountResult.getData()){
                            activity.realNameEditText.setText(activity.accountResult.getData().getTruename());
                            activity.alipayAccountEditText.setText(activity.accountResult.getData().getAccount());
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_bond_alipay);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        payPassword = getIntent().getStringExtra("payPassword");
        initViews();
        bondAlipayDetail();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("提现设置");
        realNameEditText = findViewById(R.id.realNameEditText);
        alipayAccountEditText = findViewById(R.id.alipayAccountEditText);
        saveTextView = findViewById(R.id.saveTextView);
        saveTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.saveTextView:
                if (StringUtils.isNullOrEmpty(realNameEditText.getText().toString().trim())) {
                    MessageToast.getInstance(this).show("请输入账户真实姓名");
                    break;
                }
                if (StringUtils.isNullOrEmpty(alipayAccountEditText.getText().toString().trim())) {
                    MessageToast.getInstance(this).show("请输入支付宝账户");
                    break;
                }
                bondAlipayAccount();
                break;
        }
    }

    private BaseResult baseResult;

    /**
     * 绑定支付宝账号
     */
    private void bondAlipayAccount() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.BOND_PAY_ACCOUNT);
        params.addBodyParameter("account", alipayAccountEditText.getText().toString().trim());
        params.addBodyParameter("true_name", realNameEditText.getText().toString().trim());
        params.addBodyParameter("password", payPassword);
        params.addBodyParameter("type", "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("bondAlipayAccount: " + result);
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

    private BondAlipayAccountResult accountResult;

    /**
     * 绑定的支付宝账号详情
     */
    private void bondAlipayDetail() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.BOND_ALIPAY_DETAIL);
        params.addBodyParameter("type", "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("绑定的支付宝账号详情: " + result);
                accountResult = GsonUtils.GsonToBean(result, BondAlipayAccountResult.class);
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
}
