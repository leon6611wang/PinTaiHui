package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 提现方式选择
 */
public class WithdrawSelectDialog extends Dialog implements View.OnClickListener {
    private TextView wechatpayTextView, alipayTextView, cancelTextView;

    public WithdrawSelectDialog(@NonNull Context context, int themeResId, OnWithdrawSelectListener listener) {
        super(context, themeResId);
        this.onWithdrawSelectListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_withdraw_select);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
    }

    private void initViews() {
        wechatpayTextView = findViewById(R.id.wechatpayTextView);
        wechatpayTextView.setOnClickListener(this);
        alipayTextView = findViewById(R.id.alipayTextView);
        alipayTextView.setOnClickListener(this);
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wechatpayTextView:
                if (null != onWithdrawSelectListener) {
                    onWithdrawSelectListener.onWithdrawSelect(2, "微信");
                }
                dismiss();
                break;
            case R.id.alipayTextView:
                if (null != onWithdrawSelectListener) {
                    onWithdrawSelectListener.onWithdrawSelect(1, "支付宝");
                }
                dismiss();
                break;
            case R.id.cancelTextView:
                dismiss();
                break;
        }
    }

    private OnWithdrawSelectListener onWithdrawSelectListener;

    public interface OnWithdrawSelectListener {
        void onWithdrawSelect(int index, String desc);
    }

}
