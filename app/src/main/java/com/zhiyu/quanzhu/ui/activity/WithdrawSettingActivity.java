package com.zhiyu.quanzhu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.ui.dialog.PasswordCheckDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;
import com.zhiyu.quanzhu.utils.WXUtils;
import com.zhiyu.quanzhu.wxapi.WXEntryActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

/**
 * 提现设置
 */
public class WithdrawSettingActivity extends BaseActivity implements View.OnClickListener, WXEntryActivity.OnWxBondListener {
    private LinearLayout backLayout, bondWithdrawWechatLayout, bondWithdrawAlipayLayout;
    private TextView titleTextView, wechatTextView, alipayTextView;
    private boolean is_ali, is_wechar;
    private PasswordCheckDialog passwordCheckDialog;
    private int bondType = 0;//1:wechat,2:alipay
    private MyHandler myHandler=new MyHandler(this);
    private static class MyHandler extends Handler{
        WeakReference<WithdrawSettingActivity> activityWeakReference;
        public MyHandler(WithdrawSettingActivity activity){
            activityWeakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            WithdrawSettingActivity activity=activityWeakReference.get();
            switch (msg.what){
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if(200==activity.baseResult.getCode()){
                        activity.wechatTextView.setText("已绑定");
                        activity.wechatTextView.setTextColor(activity.getResources().getColor(R.color.text_color_yellow));
                        Intent intent = new Intent();
                        intent.putExtra("isWechat", true);
                        activity.setResult(1031, intent);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_setting);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        WXEntryActivity.setOnWxBondListener(this);
        is_ali = getIntent().getBooleanExtra("is_ali", false);
        is_wechar = getIntent().getBooleanExtra("is_wechar", false);
        initDialogs();
        initViews();
    }

    private String pwd;
    private void initDialogs() {
        passwordCheckDialog = new PasswordCheckDialog(this, R.style.inputDialog, new PasswordCheckDialog.OnPayPwdListener() {
            @Override
            public void onPayPwd(String password) {
                pwd=password;
                switch (bondType) {
                    case 1:
                        WXUtils.WxLogin(WithdrawSettingActivity.this);
                        break;
                    case 2:
                        Intent bondaliIntent = new Intent(WithdrawSettingActivity.this, WithdrawBondAlipayActivity.class);
                        bondaliIntent.putExtra("payPassword", password);
                        startActivityForResult(bondaliIntent, 1030);
                        break;
                }
            }
        });
        passwordCheckDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                SoftKeyboardUtil.hideSoftKeyBoard(getWindow());
            }
        });
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("提现设置");
        bondWithdrawWechatLayout = findViewById(R.id.bondWithdrawWechatLayout);
        bondWithdrawWechatLayout.setOnClickListener(this);
        wechatTextView = findViewById(R.id.wechatTextView);
        bondWithdrawAlipayLayout = findViewById(R.id.bondWithdrawAlipayLayout);
        bondWithdrawAlipayLayout.setOnClickListener(this);
        alipayTextView = findViewById(R.id.alipayTextView);
        if (is_wechar) {
            wechatTextView.setText("已绑定");
            wechatTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
        } else {
            wechatTextView.setText("未绑定");
            wechatTextView.setTextColor(getResources().getColor(R.color.text_color_grey));
        }
        if (is_ali) {
            alipayTextView.setText("已绑定");
            alipayTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
        } else {
            alipayTextView.setText("未绑定");
            alipayTextView.setTextColor(getResources().getColor(R.color.text_color_grey));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.bondWithdrawWechatLayout:
                bondType = 1;
                passwordCheckDialog.show();
                break;
            case R.id.bondWithdrawAlipayLayout:
                bondType = 2;
                passwordCheckDialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1030) {
            alipayTextView.setText("已绑定");
            alipayTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
            Intent intent = new Intent();
            intent.putExtra("isAlipay", true);
            setResult(1031, intent);
        }
    }

    @Override
    public void onWxBondListener(String open_id) {
        System.out.println("wx open_id: " + open_id);
        bondWeChatAccount(open_id);
    }

    private BaseResult baseResult;

    /**
     * 绑定支付宝账号
     */
    private void bondWeChatAccount(String open_id) {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.BOND_PAY_ACCOUNT);
        params.addBodyParameter("account", open_id);
        params.addBodyParameter("password", pwd);
        params.addBodyParameter("type", "2");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("bondAlipayAccount: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message=myHandler.obtainMessage(1);
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
