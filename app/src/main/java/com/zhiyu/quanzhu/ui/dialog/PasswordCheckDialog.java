package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.ui.activity.EditPwdFirstActivity;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.PwdEditText;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;


/**
 * 密码校验
 * 1:登录密码，2:支付密码
 */
public class PasswordCheckDialog extends Dialog implements PwdEditText.OnTextChangeListeven {
    private PwdEditText pwdEdit;
    private int screenWidth, dp_15, dp_22, dp_32;
    private YNDialog ynDialog;
    private LoadingDialog loadingDialog;
    private int passwordType = 2;//1登录密码 2支付密码

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<PasswordCheckDialog> dialogWeakReference;

        public MyHandler(PasswordCheckDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            PasswordCheckDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 1:
                    dialog.loadingDialog.dismiss();
                    if (dialog.baseResult.getCode() == 200) {
                        if (null != dialog.onPayPwdListener) {
                            dialog.onPayPwdListener.onPayPwd(dialog.password);
                        }
                        dialog.dismiss();
                    } else {
                        MessageToast.getInstance(dialog.getContext()).show(dialog.baseResult.getMsg());
                        dialog.pwdEdit.clearText();
                        if (dialog.baseResult.getCode() == 1005) {
                            dialog.ynDialog.show();
                            String title = "";
                            switch (dialog.passwordType) {
                                case 1:
                                    title = "您暂未设置登录密码，是否立即设置？";
                                    break;
                                case 2:
                                    title = "您暂未设置支付密码，是否立即设置？";
                                    break;
                            }
                            dialog.ynDialog.setTitle(title);
                        }
                    }
                    break;
                case 2:
                    dialog.loadingDialog.dismiss();
                    FailureToast.getInstance(dialog.getContext()).show();
                    break;
            }
        }
    }

    public PasswordCheckDialog(Context context, int themeResId, OnPayPwdListener listener) {
        super(context, themeResId);
        this.onPayPwdListener = listener;
    }

    public void setPasswordType(int type) {
        this.passwordType = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay_password_input);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
//        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 3;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initLoadingDialog();
        initViews();
    }

    private void initLoadingDialog() {
        loadingDialog = new LoadingDialog(getContext(), R.style.dialog);
        ynDialog = new YNDialog(getContext(), R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                Intent intent = new Intent(getContext(), EditPwdFirstActivity.class);
                intent.putExtra("payOrLoginPwd", passwordType - 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
    }

    private void initViews() {
        screenWidth = ScreentUtils.getInstance().getScreenWidth(getContext());
        dp_15 = (int) getContext().getResources().getDimension(R.dimen.dp_15);
        dp_22 = (int) getContext().getResources().getDimension(R.dimen.dp_22);
        dp_32 = (int) getContext().getResources().getDimension(R.dimen.dp_32);
        int editWidth = screenWidth - dp_15 * 2;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(editWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params.topMargin = dp_22;
        params.bottomMargin = dp_32;
        pwdEdit = findViewById(R.id.pwdEdit);
        pwdEdit.setLayoutParams(params);
        pwdEdit.setOnTextChangeListeven(this);

    }

    private String password;

    @Override
    public void onTextChange(String pwd) {
        password = pwd;
        int length = 0;
        if (!StringUtils.isNullOrEmpty(password)) {
            length = password.length();
        }
        if (length == 6) {
            checkPayPwd();
        }
    }

    @Override
    public void show() {
        super.show();
        pwdEdit.clearText();
        SoftKeyboardUtil.showSoftKeyBoard(getWindow());
    }

    private OnPayPwdListener onPayPwdListener;

    public interface OnPayPwdListener {
        void onPayPwd(String password);
    }

    private BaseResult baseResult;

    /**
     * 校验密码
     */
    private void checkPayPwd() {
        loadingDialog.show();
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CHECK_PASSWORD);
        params.addBodyParameter("password", password);
        params.addBodyParameter("type", String.valueOf(passwordType));//1登录密码 2支付密码
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("校验支付密码: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(2);
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

}
